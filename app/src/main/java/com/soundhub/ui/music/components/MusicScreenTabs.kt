package com.soundhub.ui.music.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.ui.music.components.tab_pages.MusicLibraryPage
import com.soundhub.ui.music.components.tab_pages.MusicMainPage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun MusicScreenTabs(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val tabs: List<String> = listOf(
        stringResource(id = R.string.music_main_page),
        stringResource(id = R.string.music_library_page)
    )

    val selectedTabState: PagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    Column(
        modifier = modifier
    ) {
        PrimaryTabRow(
            selectedTabIndex = selectedTabState.currentPage,
            modifier = Modifier
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabState.currentPage == index,
                    text = {
                        Text(
                            text = tab,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            lineHeight = 20.sp,
                            letterSpacing = 2.5.sp,
                            color = if (index == selectedTabState.currentPage) MaterialTheme.colorScheme.onBackground
                            else Color.Gray
                        )
                    },
                    onClick = {
                        scope.launch { selectedTabState.animateScrollToPage(index) }
                    }
                )
            }
        }

        HorizontalPager(
            state = selectedTabState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
        ) { page ->
            when (page) {
                0 -> MusicMainPage(navController = navController)
                1 -> MusicLibraryPage(navController = navController)
            }
        }
    }
}