package com.soundhub.ui.settings.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.soundhub.R

@Composable
internal fun SettingsMenu() {
    val isDarkTheme = isSystemInDarkTheme()
    var themeChecked by remember { mutableStateOf(isDarkTheme) }

    val menuItems: List<SettingsMenuItemData> = listOf(
        SettingsMenuItemData(
            icon = R.drawable.moon,
            title = stringResource(id = R.string.dark_theme_settings),
            actionElement = {
                Switch(
                    checked = themeChecked,
                    onCheckedChange = { themeChecked = !themeChecked }
                )
            }
        ),
        SettingsMenuItemData(
            icon = R.drawable.user,
            title = stringResource(id = R.string.account_settings),
            onClick = { /* TODO: implement account settings onclick */ }
        ),
        SettingsMenuItemData(
            icon = R.drawable.magic_wand,
            title = stringResource(id = R.string.appearance_settings),
            onClick = { /* TODO: implement appearance settings onclick */ }
        ),
        SettingsMenuItemData(
            icon = R.drawable.bell,
            title = stringResource(id = R.string.notification_settings),
            onClick = { /* TODO: implement notifications settings onclick */ }
        ),
        SettingsMenuItemData(
            icon = R.drawable.information_icon,
            title = stringResource(id = R.string.about_app_settings),
            onClick = { /* TODO: implement about app onclick */ },
            hasTopDivider = true
        )
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = menuItems, key = { it.title }) { item ->
            var modifier: Modifier = Modifier
            if (item.hasTopDivider) {
                HorizontalDivider(thickness = 1.dp)
                modifier = modifier.padding(top = 5.dp)
            }


            SettingsMenuItem(
                modifier = modifier,
                icon = painterResource(id = item.icon),
                title = item.title,
                onClick = item.onClick,
                actionElement = item.actionElement,
                iconContentDescription = item.iconContentDescription
            )
        }
    }
}