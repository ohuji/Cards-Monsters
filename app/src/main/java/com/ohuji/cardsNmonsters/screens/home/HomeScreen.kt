package com.ohuji.cardsNmonsters.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ohuji.cardsNmonsters.R
import java.util.Timer
import kotlin.concurrent.schedule

@Composable
fun HomeScreen(navController: NavController, gotVM: GoTViewModel) {
    var name by remember { mutableStateOf("?") }
    var quote by remember { mutableStateOf("?") }
    gotVM.getQuote()
    name = gotVM.name
    quote = gotVM.quote
    println("$name: $quote")
    Timer().schedule(30000){
        gotVM.getQuote()
        name = gotVM.name
        quote = gotVM.quote
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.cm_splash), contentDescription = "background image",
            modifier = Modifier.matchParentSize(), contentScale = ContentScale.FillHeight)
        BorderDecor()
        Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.matchParentSize()) {
            Image(painter = painterResource(id = R.drawable.cm_logo), contentDescription = "logo image",
            modifier = Modifier.size(300.dp, 300.dp))
            Button(onClick = { navController.navigate("map_screen") },
                modifier = Modifier.height(160.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black)) {
                CustomButtonContent("Start")
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
        Image(painter = painterResource(id = R.drawable.cm_button), contentDescription = "button image",
            modifier = Modifier.size(220.dp, 60.dp)
        )
        Text(txt, textAlign = TextAlign.Center, fontSize = TextUnit(28F, TextUnitType.Sp))
    }
}

@Composable
fun BorderDecor(){
    Column(verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Image(painter = painterResource(id = R.drawable.cm_deco_tl), contentDescription = "deco image",
                modifier = Modifier.size(50.dp, 50.dp))
            Image(painter = painterResource(id = R.drawable.cm_deco_tr), contentDescription = "deco image",
                modifier = Modifier.size(50.dp, 50.dp))
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Image(painter = painterResource(id = R.drawable.cm_deco_bl), contentDescription = "deco image",
                modifier = Modifier.size(50.dp, 50.dp))
            Image(painter = painterResource(id = R.drawable.cm_deco_br), contentDescription = "deco image",
                modifier = Modifier.size(50.dp, 50.dp))
        }
    }
}

@Preview @Composable fun HomePreview(){

}