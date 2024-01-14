package com.soundhub.ui.authentication.postregistration.singers

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.soundhub.R
import com.soundhub.ui.authentication.postregistration.components.ChoosePageDesign

@Preview(showBackground = true)
@Composable
fun ChooseSingersScreen() {
    val list = List(12) {"BTS"}

    ChoosePageDesign(title = stringResource(id = R.string.cheese_singer_title), itemsList = list)
}