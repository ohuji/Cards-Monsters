package com.ohuji.cardsNmonsters.screens.maps.compose

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterManager
import com.ohuji.cardsNmonsters.screens.maps.MapState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.ohuji.cardsNmonsters.screens.maps.MapViewModel
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    state: MapState,
    navController: NavController,
    setupClusterManager: (Context, GoogleMap) -> ZoneClusterManager,
    calculateZoneViewCenter: () -> LatLngBounds,
) {

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
            MarkerInfoWindow(
                state = rememberMarkerState(position = LatLng(49.1, -122.5)),
                snippet = "Some stuff",
                onClick = {
                    // This won't work :(
                    System.out.println("Mitchs_: Cannot be clicked")
                    true
                },
                draggable = true
            )
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