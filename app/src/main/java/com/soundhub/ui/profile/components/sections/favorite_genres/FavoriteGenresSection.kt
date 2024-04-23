package com.soundhub.ui.profile.components.sections.favorite_genres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_music_note_24),
                    contentDescription = "genres",
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFFE75555)
                )
                SectionLabel(text = stringResource(id = R.string.profile_screen_favorite_genres_section_caption))
            }

            GenresFlowRow(
                genreList = genreList,
                isOriginProfile = isOriginProfile,
                navController = navController
            )
        }
    }
}