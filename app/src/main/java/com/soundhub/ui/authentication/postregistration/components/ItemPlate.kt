package com.soundhub.ui.authentication.postregistration.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R

@Composable
fun ItemPlate(
    modifier: Modifier = Modifier,
    caption: String, icon: Painter,
    onClick: (Boolean) -> Unit = {}
) {
    var isItemChosen by rememberSaveable { mutableStateOf(false) }
    var itemBoxModifier: Modifier = Modifier
        .width(72.dp)
        .height(72.dp)
        .clip(shape = RoundedCornerShape(16.dp))
        .clickable {
            isItemChosen = !isItemChosen
            onClick(isItemChosen)
        }

    if (isItemChosen)
        itemBoxModifier = itemBoxModifier
            .border(5.dp, MaterialTheme.colorScheme.primary)

    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box (
            modifier = itemBoxModifier
        ) {
            Image(
                painter = icon,
                contentDescription = "icon",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Text(
           text = caption,
            style = TextStyle(
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        )
    }


}

@Preview
@Composable
fun ItemPlatePreview() {
    ItemPlate(caption = "Rap", icon = painterResource(id = R.drawable.item))
}