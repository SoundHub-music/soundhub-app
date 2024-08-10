package com.soundhub.ui.pages.music.components.sections.recommendations

import android.util.Log
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.ui.pages.music.MusicViewModel
import com.soundhub.ui.pages.music.components.RecommendationPlate

@Composable
fun RecommendationSection(musicViewModel: MusicViewModel) {
	val lazyListState = rememberLazyListState()
	val isRowDragged by lazyListState.interactionSource.collectIsDraggedAsState()

	LaunchedEffect(key1 = isRowDragged) {
		Log.d("RecommendationSection", "isDragged $isRowDragged")
		musicViewModel.setPagerLock(isRowDragged)
	}

	val plates = listOf(
		RecommendationPlateData(
			route = Route.Music.NewOfTheWeek.route,
			gradientColor = Brush.linearGradient(
				listOf(Color(0xFFD18787), Color(0xFFFF0000))
			),
			title = stringResource(id = R.string.music_new_of_the_week)
		),
		RecommendationPlateData(
			route = Route.Music.NewOfTheMonth.route,
			gradientColor = Brush.linearGradient(
				listOf(
					Color(0xFF51DD4F),
					Color(red = 0.309f, green = 0.86f, blue = 0.73f, alpha = 0.5f)
				)
			),
			title = stringResource(id = R.string.music_new_of_the_month)
		),
		RecommendationPlateData(
			route = Route.Music.RecommendMusic.route,
			gradientColor = Brush.linearGradient(listOf(Color(0xFF35A7E7), Color(0xFF0000FF))),
			title = stringResource(id = R.string.music_recommendations)
		)
	)

	Column(
		verticalArrangement = Arrangement.spacedBy(10.dp),
	) {
		Text(
			text = stringResource(id = R.string.music_recommendation_section_title),
			fontWeight = FontWeight.ExtraBold,
			fontSize = 16.sp
		)
		LazyRow(
			horizontalArrangement = Arrangement.spacedBy(10.dp),
			state = lazyListState,
			modifier = Modifier
				.padding(10.dp)
				.pointerInput(Unit) {
					musicViewModel.horizontalDragHandler(this@pointerInput)
				}
		) {
			items(items = plates, key = { it.route }) { item ->
				RecommendationPlate(gradientColor = item.gradientColor, text = item.title)
			}
		}
	}
}

data class RecommendationPlateData(
	val route: String,
	val gradientColor: Brush,
	val title: String,
	val onClick: () -> Unit = {}
)