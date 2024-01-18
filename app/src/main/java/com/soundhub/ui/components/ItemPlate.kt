package com.soundhub.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun ItemPlate(modifier: Modifier = Modifier, caption: String, icon: Painter) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box (
            modifier = Modifier
                .width(72.dp)
                .height(72.dp)
                .clip(shape = RoundedCornerShape(16.dp))
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
fun viewItemPlate() {
    ItemPlate(caption = "Rap", icon = painterResource(id = R.drawable.item))
}