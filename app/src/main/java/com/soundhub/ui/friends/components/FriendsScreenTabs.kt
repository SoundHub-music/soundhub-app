package com.soundhub.ui.friends.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.soundhub.ui.friends.enums.FriendListPage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun FriendsScreenTabs(selectedTabState: PagerState, tabs: List<FriendListPage>) {
    val scope = rememberCoroutineScope()

    PrimaryTabRow(
        selectedTabIndex = selectedTabState.currentPage,
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = index == selectedTabState.currentPage,
                onClick = {
                    scope.launch { selectedTabState.animateScrollToPage(index) }
                },
                text = {
                    Text(
                        text = stringResource(id = tab.titleId),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            )
        }
    }
}