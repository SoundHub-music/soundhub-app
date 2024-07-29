package com.soundhub.ui.pages.profile.components.sections.photos

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.soundhub.R
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.ui.pages.profile.components.SectionLabel
import com.soundhub.Route
import com.soundhub.utils.enums.ContentTypes

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserPhotoCarousel(
    images: List<String>,
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher 
) {
    val listState = rememberLazyListState()
    var newPhotos by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { newPhotos = it }

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            SectionLabel(text = stringResource(id = R.string.profile_screen_photo_section_caption))
            FilledTonalIconButton(onClick = {
                launcher.launch(ContentTypes.IMAGE_ALL.type)
            }) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "add_photo"
                )
            }
        }
        LazyRow(state = listState) {
            items(images.size) { index ->
                GlideImage(
                    modifier = Modifier
                        .fillMaxSize(0.25f)
                        .clickable {
                            uiStateDispatcher.setGalleryUrls(images)
                            navController.navigate("${Route.Gallery.route}/$index")
                        },
                    model = images[index],
                    failure = placeholder(R.drawable.circular_user),
                    contentDescription = null
                )
            }
        }
    }
}