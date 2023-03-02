package com.ohuji.cardsNmonsters.screens.maps.compose

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

    if (deckCheck?.let { it < 1 } == true) {
        showNoDeckAlert = true
    }

    val state by remember {
        mapViewModel.state
    }

    val location by remember {
        mapViewModel.getDevicePreciseLocation(fusedLocationProviderClient)
    }

    var arrOfMonstersLatLng: List<List<LatLng>> =
        state.clusterItems.map { it.polygonOptions.points }

    //tallena arrofcoords, vikana, tee siitä koko composen variable (tai launchedeffectin, en tiiä jos tallentuu data)
    //tee observable funktio lokaatioon, joka vertaa arrofcoordsiin
    // jos käyttäjä liian kaukana, mennään line 89 looppiin

    LaunchedEffect(location) {
        if (location == LatLng(0.0, 0.0)) {
            return@LaunchedEffect
        }

        //Distance checker is only false, if a user is minimum 3500 meters away from a monster
        var distanceChecker = true
        Log.i("distanceb", "numbero 1")
        for (list in arrOfMonstersLatLng) {
            Log.i("distanceb", "numbero 2")
            for (latLng in list) {
                Log.i("distanceb", "numbero 3")
                val result = FloatArray(1)
                Location.distanceBetween(
                    location.latitude,
                    location.longitude,
                    latLng.latitude,
                    latLng.longitude,
                    result
                )
                Log.d("distanceb", result[0].toString())
                if (result[0] > 3500) {
                    Log.i("distanceb", "numbero 4")

                    distanceChecker = false
                }
            }
        }

        if (distanceChecker) {
            return@LaunchedEffect
        }
        if (arrOfMonstersLatLng.size > 5) {
            return@LaunchedEffect
        }

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
                    //add(LatLng(60.2182, 24.7809))
                    //add(LatLng(60.2175, 24.7815))
                    //add(LatLng(60.2170, 24.7814))
                    //fillColor(MapViewModel.POLYGON_FILL_COLOR)
                }
            )
            mapViewModel.addClusterItem(cluster)
        }
        arrOfMonstersLatLng = state.clusterItems.map { it.polygonOptions.points }
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

            // NOTE: Some features of the MarkerInfoWindow don't work currently. See docs:
            // https://github.com/googlemaps/android-maps-compose#obtaining-access-to-the-raw-googlemap-experimental
            // So you can use clusters as an alternative to markers.
            //MarkerInfoWindow(
            //    state = rememberMarkerState(position = LatLng(49.1, -122.5)),
            //    snippet = "Some stuff",
            //    onClick = {
            //        // This won't work :(
            //        System.out.println("Mitchs_: Cannot be clicked")
            //        true
            //    },
            //    draggable = true
            //)
        }
    }
//    // Center camera to include all the Zones.
//    LaunchedEffect(state.clusterItems) {
//        if (state.clusterItems.isNotEmpty()) {
//            cameraPositionState.animate(
//                update = CameraUpdateFactory.newLatLngBounds(
//                    calculateZoneViewCenter(),
//                    0
//                ),
//            )
//        }
//    }


    if (showNoDeckAlert) {
        ShowDialog(
            title = "No deck created",
            message = "You must create a deck before you can fight monster",
            onDismiss = { noDeckDialogDismiss() }
        )
    }
}


/**
 * If you want to center on a specific location.
 */
private suspend fun CameraPositionState.centerOnLocation(
    location: Location
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        LatLng(location.latitude, location.longitude),
        15f
    ),
)
