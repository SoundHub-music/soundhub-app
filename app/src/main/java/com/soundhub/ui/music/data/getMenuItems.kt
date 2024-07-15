package com.soundhub.ui.music.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.model.LibraryItemData

@Composable
fun getMenuItems(): List<LibraryItemData> = listOf(
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
        icon = painterResource(id = R.drawable.rounded_genres_24),
        route = Route.Music.route
    ),
    LibraryItemData(
        title = stringResource(id = R.string.music_library_page_artists),
        icon = painterResource(id = R.drawable.rounded_artist_24),
        route = Route.Music.route
    )
)