package com.soundhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.ui.theme.SoundHubTheme
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.paint
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SoundHubTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginPage()
                }
            }
        }
    }
}


// верстка login page временно находится здесь
@Composable
fun LoginPage() {
    val backgroundImage: Painter = painterResource(R.drawable.login_page_background)
    Container(modifier = Modifier
        .fillMaxSize()
        .paint(backgroundImage, contentScale = ContentScale.Crop)) {
        Column(
            Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AppName()
            BottomLoginButton()
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomLoginButton() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(SheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipPartiallyExpanded = false,
        skipHiddenState = true
    ))

    var isButtonVisible by remember {
        mutableStateOf(true)
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 100.dp,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                isButtonVisible = scaffoldState.bottomSheetState.hasPartiallyExpandedState
                if (isButtonVisible)
                    FilledTonalButton(onClick = { scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }}) {
                        Text(text = stringResource(R.string.lets_start_login_btn))
                    }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sheet content")
            }
        }) {}

}

@Composable
fun AppName(modifier: Modifier = Modifier) {
    val appLogo: Painter = painterResource(R.drawable.soundhub_logo)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 30.dp, bottom = 30.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Image(painter = appLogo, contentDescription = "app logo")
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.app_name),
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 50.sp,
        )
    }
}

@Composable
fun Container(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        content()
    }
}
