package com.soundhub.presentation.pages.profile.ui.sections.user_actions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.domain.model.User
import com.soundhub.domain.states.ProfileUiState
import com.soundhub.presentation.pages.profile.ProfileViewModel
import com.soundhub.presentation.shared.text.HighlightedText
import com.soundhub.utils.lib.DateFormatter
import java.time.LocalDate

@Composable
internal fun UserNameWithDescription(profileViewModel: ProfileViewModel) {
	val profileUiState: ProfileUiState by profileViewModel
		.profileUiState
		.collectAsState()
	val profileOwner: User? = profileUiState.profileOwner

	var isDescriptionButtonChecked: Boolean by rememberSaveable { mutableStateOf(false) }
	val iconAngle by animateFloatAsState(
		targetValue = if (isDescriptionButtonChecked) 180f else 0f,
		animationSpec = tween(400),
		label = "arrow_icon_angle"
	)

	Column(
		modifier = Modifier
			.animateContentSize()
			.padding(bottom = 5.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.clickable { isDescriptionButtonChecked = !isDescriptionButtonChecked },
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = profileOwner?.getFullName() ?: "",
				fontWeight = FontWeight.Bold,
				lineHeight = 16.sp,
				fontSize = 28.sp,
			)

			IconToggleButton(
				checked = isDescriptionButtonChecked,
				onCheckedChange = { isDescriptionButtonChecked = it },
			) {
				Icon(
					imageVector = Icons.Rounded.KeyboardArrowDown,
					contentDescription = "expand description",
					modifier = Modifier
						.size(35.dp)
						.rotate(iconAngle)
				)
			}
		}

		UserDetailsSection(
			profileViewModel = profileViewModel,
			isVisible = isDescriptionButtonChecked
		)
	}
}


@Composable
private fun UserDetailsSection(
	modifier: Modifier = Modifier,
	profileViewModel: ProfileViewModel,
	isVisible: Boolean = false
) {
	val profileUiState: ProfileUiState by profileViewModel.profileUiState.collectAsState()
	val profileOwner: User? = profileUiState.profileOwner

	AnimatedVisibility(
		visible = isVisible,
		enter = slideInVertically() + fadeIn(),
		exit = fadeOut(animationSpec = tween(250))
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(5.dp),
			modifier = modifier
				.background(
					color = MaterialTheme.colorScheme.secondaryContainer,
					shape = RoundedCornerShape(16.dp)
				)
				.fillMaxWidth()
				.padding(12.dp)
		) {
			UserBirthdayBlock(birthday = profileOwner?.birthday)
			UserLanguagesBlock(languages = profileOwner?.languages.orEmpty())
			UserDescriptionBlock(description = profileOwner?.description)
		}
	}
}

@Composable
private fun UserDescriptionBlock(description: String?) {
	if (description.isNullOrEmpty()) return

	HighlightedText(
		highlightedText = stringResource(id = R.string.profile_screen_user_description),
		text = description,
		style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp)
	)
}

@Composable
private fun UserBirthdayBlock(birthday: LocalDate?) {
	if (birthday == null) return

	HighlightedText(
		highlightedText = stringResource(id = R.string.profile_screen_user_birthday),
		text = DateFormatter.getStringDate(date = birthday, includeYear = true),
		style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp)
	)
}

@Composable
private fun UserLanguagesBlock(languages: List<String>) {
	if (languages.isEmpty()) return

	HighlightedText(
		highlightedText = stringResource(id = R.string.profile_screen_user_languages),
		text = languages.joinToString(", "),
		style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp)
	)
}
