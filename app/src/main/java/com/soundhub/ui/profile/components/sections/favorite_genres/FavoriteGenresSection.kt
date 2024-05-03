package com.soundhub.ui.profile.components.sections.favorite_genres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.model.Genre
import com.soundhub.ui.profile.components.SectionLabel

@Composable
fun FavoriteGenresSection(
    genreList: List<Genre>,
    isOriginProfile: Boolean,
    navController: NavHostController
) {
    ElevatedCard {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            SectionLabel(
                text = stringResource(id = R.string.profile_screen_favorite_genres_section_caption),
                labelIcon = painterResource(id = R.drawable.round_music_note_24),
                iconTint = Color(0xFFE75555)
            )

            GenresFlowRow(
                genreList = genreList,
                isOriginProfile = isOriginProfile,
                navController = navController
            )
        }
    }
}