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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

/**
* A composable function that displays a guide screen.
* @param navController The NavController used for navigating to different screens.
 */
@Composable
fun GuideScreen(navController: NavController) {
    Column {
        Box(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {
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
                contentDescription = stringResource(id = R.string.paper_image_desc),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier
                .padding(start = 5.dp, top = 5.dp, bottom = 10.dp, end = 0.dp)
                .verticalScroll(rememberScrollState())) {
                    Guide()
                    GuideDeckBuilding()
                    GuideMap()
                    GuideBattle()
                    GuideElement()
                }
        }
    }
}


@Composable
fun Guide() {
    Text(
        text = stringResource(R.string.how_to_play),
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    )
}

@Composable
fun GuideDeckBuilding() {
    Text(
        text = stringResource(R.string.deck_building),
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    )

    Text(
        text = stringResource(R.string.deck_building_guide),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    )
}

@Composable
fun GuideMap() {
    Text(
        text = stringResource(R.string.map),
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    )

    Text(
        text = stringResource(R.string.map_guide),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    )
}

@Composable
fun GuideBattle() {
    Text(
        text = stringResource(R.string.battle),
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    )

    Text(
        text = stringResource(R.string.battle_guide),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    )
}

@Composable
fun GuideElement() {
    Text(
        text = stringResource(R.string.elemental_strength_weakness),
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 8.dp)
    )

    Text(
        text = stringResource(R.string.elemental_attack_explained),
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
    )

    Text(
        text = stringResource(R.string.elements),
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
    )

    Row() {
        Image(
            painter = painterResource(R.drawable.light_icon),
            contentDescription = stringResource(id = R.string.light_icon_desc),
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp)
        )

        Text(
            text = stringResource(R.string.light_elemental),
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }

    Row() {
        Image(
            painter = painterResource(R.drawable.dark_icon),
            contentDescription = stringResource(id = R.string.dark_icon_desc),
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp)
        )

        Text(
            text = stringResource(R.string.dark_elemental),
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }

    Row() {
        Image(
            painter = painterResource(R.drawable.water_icon),
            contentDescription = stringResource(id = R.string.water_icon_desc),
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp)
        )

        Text(
            text = stringResource(R.string.water_elemental),
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }

    Row() {
        Image(
            painter = painterResource(R.drawable.fire_icon),
            contentDescription = stringResource(id = R.string.fire_icon_desc),
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp)
        )

        Text(
            text = stringResource(R.string.fire_elemental),
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }

    Row() {
        Image(
            painter = painterResource(R.drawable.wind_icon),
            contentDescription = stringResource(id = R.string.wind_icon_desc),
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp)
        )

        Text(
            text = stringResource(R.string.wind_elemental),
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }

    Row() {
        Image(
            painter = painterResource(R.drawable.earth_icon),
            contentDescription = stringResource(id = R.string.earth_icon_desc),
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp)
        )

        Text(
            text = stringResource(R.string.earth_elemental),
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }

    Row() {
        Image(
            painter = painterResource(R.drawable.electricity_icon),
            contentDescription = stringResource(id = R.string.electricity_icon_desc),
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp)
        )

        Text(
            text = stringResource(R.string.electricity_elemental),
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }

    Row() {
        Image(
            painter = painterResource(R.drawable.phys_icon),
            contentDescription = stringResource(id = R.string.element_icon_desc),
            modifier = Modifier
                .padding(start = 3.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                .size(20.dp)
        )

        Text(
            text = stringResource(R.string.physical_attack),
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}