package com.soundhub.ui.music.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R

@Composable
internal fun RecommendationPlate(
    modifier: Modifier = Modifier,
    gradientColor: Brush,
    text: String,
    onClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .size(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .background(
                brush = gradientColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(bottom = 20.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.nunito_bold))
        )
    }
}

@Composable
@Preview
private fun RecommendationPlatePreview() {
    RecommendationPlate(
        gradientColor = Brush.linearGradient(listOf(Color(0xFFd18787), Color(0xFFFF0000))),
        text = "Новинки недели"
    )
}