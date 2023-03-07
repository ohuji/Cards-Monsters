package com.ohuji.cardsNmonsters.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ohuji.cardsNmonsters.R
import com.ohuji.cardsNmonsters.utils.BorderDecor

@Composable
fun HomeScreen(navController: NavController, gotVM: GoTViewModel) {
    val name: String = gotVM.name
    val quote: String = gotVM.quote

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.cm_splash),
            contentDescription = stringResource(id = R.string.main_image_desc),
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillHeight,
        )

        BorderDecor()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.matchParentSize(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.cm_logo),
                contentDescription = stringResource(id = R.string.logo_image_desc),
                modifier = Modifier.size(300.dp, 300.dp),
            )

            Button(
                onClick = { navController.navigate("map_screen") },
                modifier = Modifier.height(160.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black)) {
                    CustomButtonContent(stringResource(R.string.home_start))
            }

            Button(onClick = { navController.navigate("guide_screen") },
                modifier = Modifier.height(80.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black)) {
                    CustomButtonContent(stringResource(R.string.home_guide))
            }

            Text(
                "$name: $quote",
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Color.Transparent,
                                Color.White,
                                Color.Transparent
                            )
                        )
                    )
                    .width(250.dp),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun CustomButtonContent(txt: String){
    Box(contentAlignment = Alignment.Center) {
        Text("", modifier = Modifier
            .size(120.dp, 50.dp)
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        Color.Transparent,
                        Color.White,
                        Color.Transparent
                    )
                )
            )
        )
        Image(
            painter = painterResource(id = R.drawable.cm_button),
            contentDescription = stringResource(id = R.string.button_image_desc),
            modifier = Modifier.size(220.dp, 60.dp),
        )

        Text(
            txt,
            textAlign = TextAlign.Center,
            fontSize = TextUnit(28F, TextUnitType.Sp)
        )
    }
}