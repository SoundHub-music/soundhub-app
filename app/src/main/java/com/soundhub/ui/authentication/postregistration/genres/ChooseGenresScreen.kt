package com.soundhub.ui.authentication.postregistration.genres


import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.soundhub.R
import com.soundhub.ui.authentication.postregistration.components.ChoosePageDesign

@Preview(showBackground = true)
@Composable
fun ChooseGenresScreen() {
    val list = List(12) {"Rap"}

    ChoosePageDesign(title = stringResource(id = R.string.choose_genre_title), itemsList = list)

}