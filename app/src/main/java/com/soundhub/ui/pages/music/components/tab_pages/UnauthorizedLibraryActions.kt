package com.soundhub.ui.pages.music.components.tab_pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.ui.pages.music.components.MusicServiceButton

@Composable
fun UnauthorizedLibraryActions(
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.music_auth_service_text),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 28.sp,
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
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
}

@Composable
@Preview
private fun UnauthorizedLibraryItemsPreview() {
    UnauthorizedLibraryActions()
}
