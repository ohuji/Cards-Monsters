package com.ohuji.cardsNmonsters.screens.guide

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ohuji.cardsNmonsters.R

@Composable
fun GuideScreen(navController: NavController) {

    Column {
        Box(modifier = Modifier.padding(top = 10.dp, bottom = 3.dp)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.clickable {
                    navController.navigate("home_screen")
                }
            )
        }
        Box(modifier = Modifier.fillMaxHeight(0.90f)) {
            Image(
                painter = painterResource(R.drawable.paper),
                contentDescription = "Guide background image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier
                .padding(start = 5.dp, top = 5.dp, bottom = 10.dp, end = 0.dp)) {

                Guide()
                GuideDeckBuilding()
                GuideMap()
                GuideBattle()
                GuideElement()
              //  BackButton(navController = navController)
            }
        }
    }
}

@Composable
fun Guide() {
    Text(text = stringResource(R.string.how_to_play), fontSize = 16.sp, textAlign = TextAlign.Center, modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp))
}

@Composable
fun GuideDeckBuilding() {
    Text(text = stringResource(R.string.deck_building), fontSize = 16.sp, textAlign = TextAlign.Center, modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp))
}

@Composable
fun GuideMap() {
    Text(text = stringResource(R.string.map), fontSize = 16.sp, textAlign = TextAlign.Center, modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp))
}

@Composable
fun GuideBattle() {
    Text(text = stringResource(R.string.battle), fontSize = 16.sp, textAlign = TextAlign.Center, modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp))
}

@Composable
fun GuideElement() {
    Text(text = stringResource(R.string.elemental_strength_weakness) , fontSize = 16.sp, textAlign = TextAlign.Center, modifier = Modifier
        .fillMaxWidth()
        .padding(top = 5.dp, bottom = 8.dp))

    Text(
        text = stringResource(R.string.elemental_attack_explained) ,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
    )

    Text(text = stringResource(R.string.elements),
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 12.sp
    )

    Row() {
        Image(painter = painterResource(R.drawable.light_icon), contentDescription = "light icon",
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp))
        Text(text = stringResource(R.string.light_elemental) , modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
            textAlign = TextAlign.Center)
    }

    Row() {
        Image(painter = painterResource(R.drawable.dark_icon), contentDescription = "Dark icon",
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp))
        Text(text = stringResource(R.string.dark_elemental), modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
            textAlign = TextAlign.Center)
    }
    Row() {
        Image(painter = painterResource(R.drawable.water_icon), contentDescription = "Water icon",
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp))
        Text(text = stringResource(R.string.water_elemental), modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
            textAlign = TextAlign.Center)
    }
    Row() {
        Image(painter = painterResource(R.drawable.fire_icon), contentDescription = "Fire icon",
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp))
        Text(text = stringResource(R.string.fire_elemental), modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
            textAlign = TextAlign.Center)
    }
    Row() {
        Image(painter = painterResource(R.drawable.wind_icon), contentDescription = "Wind icon",
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp))
        Text(text = stringResource(R.string.wind_elemental), modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
            textAlign = TextAlign.Center)
    }
    Row() {
        Image(painter = painterResource(R.drawable.earth_icon), contentDescription = "Earth icon",
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp))
        Text(text = stringResource(R.string.earth_elemental), modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
            textAlign = TextAlign.Center)
    }
    Row() {
        Image(painter = painterResource(R.drawable.electricity_icon), contentDescription = "Electricity icon",
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp))
        Text(text = stringResource(R.string.electricity_elemental), modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
            textAlign = TextAlign.Center)
    }
    Row() {
        Image(painter = painterResource(R.drawable.phys_icon), contentDescription = "Element icon",
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp))
        Text(text = stringResource(R.string.physical_attack), modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
            textAlign = TextAlign.Center)
    }
}

@Composable
fun BackButton(navController: NavController) {
    Button(onClick = { navController.navigate("home_screen") }) {
        Text(text = "Back")
    }
}