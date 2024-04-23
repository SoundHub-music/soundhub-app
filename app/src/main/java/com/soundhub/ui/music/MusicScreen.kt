package com.soundhub.ui.music

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.music.components.MusicScreenPager
import com.soundhub.ui.music.components.MusicScreenTabRow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicScreen(
    musicViewModel: MusicViewModel,
    navController: NavHostController
) {
    val tabs: List<String> = listOf(
        stringResource(id = R.string.music_main_page),
        stringResource(id = R.string.music_library_page)
    )

    val selectedTabState: PagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    Column {
        MusicScreenTabRow(
            selectedTabState = selectedTabState,
            tabs = tabs
        )
        ContentContainer(
            modifier = Modifier
        ) {
            MusicScreenPager(
                selectedTabState = selectedTabState,
                navController = navController
            )

        }
    }
}