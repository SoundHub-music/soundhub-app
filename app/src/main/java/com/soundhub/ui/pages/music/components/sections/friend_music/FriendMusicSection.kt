package com.soundhub.ui.pages.music.components.sections.friend_music

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.data.model.User
import com.soundhub.data.states.UiState
import com.soundhub.ui.pages.music.MusicViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun FriendMusicSection(
    musicViewModel: MusicViewModel,
    uiStateDispatcher: UiStateDispatcher
) {
    val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val authorizedUser: User? = uiState.authorizedUser
    val friends = authorizedUser?.friends.orEmpty()

    val isSingle = friends.size == 1

    LaunchedEffect(key1 = friends) {
        Log.d("FriendMusicSection", friends.size.toString())
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.music_friend_music_section_title),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp
        )
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = friends, key = { it.id }) { friend ->
                FriendMusicCard(friend, isSingle)
            }
        }
    }
}