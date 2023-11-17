package com.soundhub.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.soundhub.utils.NamedScreens
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.soundhub.utils.Routes


@Composable
fun NavigationBarContainer(items: List<NavigationItemApp>, navController: NavController) {
    var selectedItem by rememberSaveable { mutableStateOf(NamedScreens.POSTLINE) }

    NavigationBar(
        modifier = Modifier
            .padding(bottom = 15.dp)
            .shadow(
                elevation = 4.dp,
                spotColor = Color(0x40000000),
                ambientColor = Color(0x40000000)
            )
            .clip(RoundedCornerShape(16.dp)),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = item.icon,
                selected = selectedItem == item.route,
                onClick = {
                    selectedItem = item.route
                    navController.navigate(item.route.name) {
                        popUpTo(Routes.POSTLINE.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

data class NavigationItemApp(
    val route: NamedScreens = NamedScreens.POSTLINE,
    val icon: @Composable () -> Unit,
    val label: String? = "",
)