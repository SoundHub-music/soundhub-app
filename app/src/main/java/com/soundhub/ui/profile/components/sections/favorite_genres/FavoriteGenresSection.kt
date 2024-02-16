package com.soundhub.ui.profile.components.sections.favorite_genres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.model.Genre
import com.soundhub.ui.profile.components.SectionLabel
import com.soundhub.Route
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FavoriteGenresSection(
    genreList: List<Genre>,
    isOriginProfile: Boolean,
    navController: NavHostController
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionLabel(text = stringResource(id = R.string.profile_screen_favorite_genres_section_caption))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            maxItemsInEachRow = 3
        ) {
            genreList.forEach { genre ->
                genre.name?.let {
                    FavoriteGenreItem(
                        modifier = Modifier
                            .wrapContentWidth()
                            .weight(1f),
                        genreName = it,
                        genreColor = generateContrastColor(MaterialTheme.colorScheme.onPrimary)
                    )
                }
            }
    
            if (isOriginProfile) IconButton(
                onClick = {
                    /* TODO: make adding favorite genre logic */
                    navController.navigate(Route.Authentication.ChooseGenres.route)
                },
                modifier = Modifier.size(40.dp)
            ) { Icon(Icons.Rounded.Add, contentDescription = null) }
        }
    }
}


// TODO: remake color generation
private fun generateContrastColor(baseColor: Color): Color {
    val contrastFactor = 1f
    val r = (baseColor.red + (Random.nextFloat() - 0.5f) * contrastFactor).coerceIn(0f, 1f)
    val g = (baseColor.green + (Random.nextFloat() - 0.5f) * contrastFactor).coerceIn(0f, 1f)
    val b = (baseColor.blue + (Random.nextFloat() - 0.5f) * contrastFactor).coerceIn(0f, 1f)

    return Color(r, g, b)
}