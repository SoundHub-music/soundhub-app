package com.soundhub.ui.notifications

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun NotificationScreen(
    navController: NavHostController
) {
    Box() {
        LazyColumn() {

        }
    }
}

@Composable
@Preview
fun NotificationScreenPreview() {
    val navController = rememberNavController()
    NotificationScreen(navController)
}