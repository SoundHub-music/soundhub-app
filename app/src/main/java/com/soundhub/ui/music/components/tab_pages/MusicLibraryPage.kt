package com.soundhub.ui.music.components.tab_pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.ui.music.components.LibraryItem

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

@Composable
fun AuthorizedLibraryPage(
    modifier: Modifier = Modifier,
    items: List<LibraryItemData>,
    navController: NavHostController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = items , key = { it.title }) { item ->
                LibraryItem(
                    title = item.title,
                    icon = item.icon,
                    contentDescription = item.contentDescription,
                    route = item.route,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun UnauthorizedLibraryPage(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.music_auth_service_text),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 16.sp
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            MusicServiceButton(
                painter = painterResource(id = R.drawable.last_fm)
            )
            MusicServiceButton(
               painter = painterResource(id = R.drawable.spotify)
            )
            MusicServiceButton(
                painter = painterResource(id = R.drawable.yandex_music)
            )
        }
    }
}

@Composable
fun MusicServiceButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    onClick: () -> Unit = {},

) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black
            )
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(72.dp)
        )
    }
}

@Composable
@Preview
fun UnauthorizedLibraryPagePreview() {
    UnauthorizedLibraryPage()
}

data class LibraryItemData(
    val title: String,
    val route: String,
    val icon: Painter,
    val contentDescription: String? = null
)