package com.ohuji.cardsNmonsters.screens.maps.compose

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.ohuji.cardsNmonsters.screens.maps.MapViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.maps.android.ktx.model.polygonOptions
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterItem

var i = 0
@Composable
fun MapScreen(
    mapViewModel: MapViewModel = viewModel(),
    navController: NavController,
    setupClusterManager: (Context, GoogleMap) -> ZoneClusterManager,
    calculateZoneViewCenter: () -> LatLngBounds,
    fusedLocationProviderClient: FusedLocationProviderClient,
) {
    val state by remember {
        mapViewModel.state
    }

    val location by remember {
        mapViewModel.getDevicePreciseLocation(fusedLocationProviderClient)
    }

    val cluster = ZoneClusterItem(
        id = "id",
        title = "title",
        snippet = "snippet",
        polygonOptions = polygonOptions {
            add(LatLng(60.2180, 24.7811))
            add(LatLng(60.2182, 24.7809))
            add(LatLng(60.2175, 24.7815))
            add(LatLng(60.2170, 24.7814))
            fillColor(MapViewModel.POLYGON_FILL_COLOR)
        }
    )

    Log.i("bugbug", "location: $location")
        //mapViewModel.addClusterItem(cluster)

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