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

/**
 * This function draws four decorative drawables in the four corners of the screen/view, according to the max constraint dimensions
*/
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
            //top left image
            Image(
                painter = painterResource(id = R.drawable.cm_deco_tl),
                contentDescription = null,
                modifier = Modifier.size(50.dp, 50.dp)
            )

            //top right image
            Image(
                painter = painterResource(id = R.drawable.cm_deco_tr),
                contentDescription = null,
                modifier = Modifier.size(50.dp, 50.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            //bottom left image
            Image(
                painter = painterResource(id = R.drawable.cm_deco_bl),
                contentDescription = null,
                modifier = Modifier.size(50.dp, 50.dp)
            )

            //bottom right image
            Image(
                painter = painterResource(id = R.drawable.cm_deco_br),
                contentDescription = null,
                modifier = Modifier.size(50.dp, 50.dp)
            )
        }
    }
}