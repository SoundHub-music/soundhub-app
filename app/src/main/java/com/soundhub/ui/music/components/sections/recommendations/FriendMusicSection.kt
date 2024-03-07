package com.soundhub.ui.music.components.sections.recommendations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.soundhub.R

@Composable
fun FriendMusicSection() {
    Column() {
        Text(
            text = stringResource(id = R.string.music_friend_music_section_title),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp
        )
        LazyColumn {
            // TODO: implement design of friend's music preferences
        }
    }
}