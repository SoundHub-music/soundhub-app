package com.soundhub.ui.authentication.postregistration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.ui.components.ItemPlate
import com.soundhub.ui.components.NextButton

@Composable
fun ChoosePageDesign(
    title: String,
    itemsList: List<*>
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 20.dp, end = 15.dp, bottom = 30.dp),
                text = title,
                style = TextStyle(
                    fontSize = 32.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_extrabold)),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,

                    )
            )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                contentPadding = PaddingValues(
                    all = 10.dp
                ),

                content = {
                    itemsIndexed(itemsList) { index, plate ->
                        ItemPlate(
                            modifier = Modifier.padding(bottom = 20.dp),
                            caption = "Rap",
                            icon = painterResource(id = R.drawable.item)
                        )
                    }
                }
            )
        }

        NextButton (
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {}
    }
}