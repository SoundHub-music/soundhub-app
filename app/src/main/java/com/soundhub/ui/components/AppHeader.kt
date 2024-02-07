package com.soundhub.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.UiEventDispatcher

@Composable
fun AppHeader(
    pageName: String?,
    modifier: Modifier = Modifier,
    actionButton: @Composable () -> Unit,
    uiEventDispatcher: UiEventDispatcher = hiltViewModel()
) {
    val isTopAppBarPressed = uiEventDispatcher.isSearchBarActive.collectAsState().value
    var inputValue by rememberSaveable { mutableStateOf("") }

    if (isTopAppBarPressed)
        SearchTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            modifier = Modifier.padding(10.dp)
        )
    else
        Row(
            modifier = modifier
                .height(60.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .shadow(
                    elevation = 4.dp,
                    spotColor = Color(0x40000000),
                    ambientColor = Color(0x40000000)
                )
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Image(
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp),
                        painter = painterResource(id = R.drawable.soundhub_logo),
                        contentDescription = "app logo"
                    )
                    if (pageName != null)
                        Text(
                            text = pageName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        )
                }
                actionButton()
            }
        }
}