package com.soundhub.ui.profile.components.sections.user_actions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.data.model.User
import com.soundhub.utils.DateFormatter

@Composable
internal fun UserDescriptionBlock(modifier: Modifier = Modifier, user: User? = null) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        if (user?.description?.isNotEmpty() == true)
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        append(stringResource(id = R.string.profile_screen_user_description))
                    }
                    append(user.description)
                },
                fontWeight = FontWeight.Medium
            )
        user?.birthday?.let {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )) {
                        append(stringResource(id = R.string.profile_screen_user_birthday))
                    }
                    append(" " + DateFormatter.getStringDate(date = user.birthday!!, includeYear = true))
                },
                fontWeight = FontWeight.Medium,
            )
        }

        if (user?.languages?.isNotEmpty() == true)
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )) {
                        append(stringResource(id = R.string.profile_screen_user_languages))
                    }
                    append(user.languages.joinToString(", "))
                }
            )
    }
}