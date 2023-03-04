package com.ohuji.cardsNmonsters.screens.maps.compose

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.google.maps.android.ktx.model.polygonOptions
import com.ohuji.cardsNmonsters.screens.augmented_reality.ShowDialog
import com.ohuji.cardsNmonsters.screens.deck_building.DeckViewModel
import com.ohuji.cardsNmonsters.screens.maps.MapViewModel
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterItem
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterManager
import kotlinx.coroutines.launch
import kotlin.random.Random

var boolCheckerFlag = false

@Composable
fun MapScreen(

    mapViewModel: MapViewModel = viewModel(),
    navController: NavController,
    setupClusterManager: (Context, GoogleMap) -> ZoneClusterManager,
    calculateZoneViewCenter: () -> LatLngBounds,
    fusedLocationProviderClient: FusedLocationProviderClient,
    deckViewModel: DeckViewModel,
) {

    val deckCheck =
        deckViewModel.getAllDecks().observeAsState().value?.size //.observeAsState().value?.size
    var showNoDeckAlert by remember { mutableStateOf(false) }

    fun noDeckDialogDismiss() {
        showNoDeckAlert = false
        navController.navigate("deck_building_screen")
    }

    fun fightMonsterDismiss() {
        showNoDeckAlert = false
        navController.navigate("deck_building_screen")
    }

    if (deckCheck?.let { it < 1 } == true) {
        showNoDeckAlert = true
    }


    val state by remember {
        mapViewModel.state
    }

    val location by remember {
        mapViewModel.getDevicePreciseLocation(fusedLocationProviderClient)
    }
    var fightMonster by remember {
        mutableStateOf(false)
    }
    var fightMonsterNoNearby by remember {
        mutableStateOf(true)
    }


    var arrOfMonstersLatLng: List<List<LatLng>> =
        state.clusterItems.map { it.polygonOptions.points }

    LaunchedEffect(location) {
        Log.d("perkele", "launch effect")
        if (location == LatLng(0.0, 0.0)) {
            return@LaunchedEffect
        }

        /*var distanceChecker = true
        if (arrOfMonstersLatLng.isEmpty()) {
            distanceChecker = false
        }
        if (distanceChecker) {
            return@LaunchedEffect
        }
        if (arrOfMonstersLatLng.size > 5) {
            return@LaunchedEffect
        }*/

        mapViewModel.removeClusterItems()

        for (i in 1..10) {
            val newMonster = mapViewModel.generateLatLng(
                location,
                Random.nextDouble(0.0, 1500.0),
                Random.nextDouble(0.0, 365.0)
            )
            val cluster = ZoneClusterItem(
                id = "id",
                title = "title",
                snippet = "snippet",
                polygonOptions = polygonOptions {
                    add(newMonster)
                }
            )
            mapViewModel.addClusterItem(cluster)
        }
        arrOfMonstersLatLng = state.clusterItems.map { it.polygonOptions.points }
    }
    if (fightMonster) {
        for (list in arrOfMonstersLatLng) {
            for (latLng in list) {
                val result = FloatArray(1)
                Location.distanceBetween(
                    location.latitude,
                    location.longitude,
                    latLng.latitude,
                    latLng.longitude,
                    result
                )
                if (result[0] < 100.0) {
                    if (location != LatLng(0.0, 0.0)) {
                        fightMonsterNoNearby = false
                        ShowDialog(
                            title = "tappelu",
                            message = "FIGHT",
                            onDismiss = { fightMonsterDismiss() }
                        )
                    }
                }
            }
        }
        if (fightMonsterNoNearby) {
            ShowDialog(
                title = "no monsters",
                message = " nearby",
                onDismiss = { fightMonsterDismiss() }
            )
        }

    }


    // Set properties using MapProperties which you can use to recompose the map
    val mapProperties = MapProperties(
        // Only enable if user has accepted location permissions.
        isMyLocationEnabled = state.lastKnownLocation != null,
    )
    val cameraPositionState = rememberCameraPositionState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            cameraPositionState = cameraPositionState
        ) {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            MapEffect(state.clusterItems) { map ->
                if (state.clusterItems.isNotEmpty()) {
                    val clusterManager = setupClusterManager(context, map)
                    map.setOnCameraIdleListener(clusterManager)
                    map.setOnMarkerClickListener(clusterManager)
                    state.clusterItems.forEach { clusterItem ->
                        map.addPolygon(clusterItem.polygonOptions)
                    }
                    map.setOnMapLoadedCallback {
                        if (state.clusterItems.isNotEmpty()) {
                            scope.launch {
                                cameraPositionState.animate(
                                    update = CameraUpdateFactory.newLatLngBounds(
                                        calculateZoneViewCenter(),
                                        0
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
        Box {
            FloatingActionButton(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .align(alignment = Alignment.BottomEnd)
                    .zIndex(1f),
                onClick = {
                    fightMonster = true
                }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }


    }



    if (showNoDeckAlert) {
        ShowDialog(
            title = "No deck created",
            message = "You must create a deck before you can fight monster",
            onDismiss = { noDeckDialogDismiss() }
        )
    }
}


