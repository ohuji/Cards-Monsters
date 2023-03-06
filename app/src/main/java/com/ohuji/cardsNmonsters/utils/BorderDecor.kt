package com.ohuji.cardsNmonsters.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ohuji.cardsNmonsters.R

@Composable
fun BorderDecor(){
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.cm_deco_tl),
                contentDescription = "deco image",
                modifier = Modifier.size(50.dp, 50.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.cm_deco_tr),
                contentDescription = "deco image",
                modifier = Modifier.size(50.dp, 50.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.cm_deco_bl),
                contentDescription = "deco image",
                modifier = Modifier.size(50.dp, 50.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.cm_deco_br),
                contentDescription = "deco image",
                modifier = Modifier.size(50.dp, 50.dp)
            )
        }
    }
}