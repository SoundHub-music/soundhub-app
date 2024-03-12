package com.soundhub.ui.music.components.tab_pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.ui.music.components.AuthorizedLibraryPage
import com.soundhub.ui.music.components.UnauthorizedLibraryPage

@Composable
internal fun MusicLibraryPage(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val authorized = false
    val items = listOf(
        LibraryItemData(
            title = stringResource(id = R.string.music_library_page_playlists),
            icon = painterResource(id = R.drawable.round_queue_music_24),
            route = Route.Music.route
        ),
        LibraryItemData(
            title = stringResource(id = R.string.music_library_page_albums),
            icon = painterResource(id = R.drawable.baseline_album_24),
            route = Route.Music.route
        ),
        LibraryItemData(
            title = stringResource(id = R.string.music_library_page_favorites),
            icon = painterResource(id = R.drawable.round_favorite_24),
            route = Route.Music.route
        ),
        LibraryItemData(
            title = stringResource(id = R.string.music_libray_page_artists),
            icon = painterResource(id = R.drawable.round_artist_24),
            route = Route.Music.route
        )
    )

    if (!authorized)
        UnauthorizedLibraryPage()
    else AuthorizedLibraryPage(
        items = items,
        navController = navController,
        modifier = modifier
    )
}

data class LibraryItemData(
    val title: String,
    val route: String,
    val icon: Painter,
    val contentDescription: String? = null
)