package com.ohuji.cardsNmonsters.screens.maps

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterItem
import com.ohuji.cardsNmonsters.screens.maps.clusters.calculateCameraViewPoints
import com.ohuji.cardsNmonsters.screens.maps.clusters.getCenterOfPolygon
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.maps.android.ktx.model.polygonOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {

    val state: MutableState<MapState> = mutableStateOf(
        MapState(
            lastKnownLocation = null,
            clusterItems = listOf(
                ZoneClusterItem(
                    id = "zone-1",
                    title = "Zone 1",
                    snippet = "This is Zone 1.",
                    polygonOptions = polygonOptions {
                        add(LatLng(60.2178, 24.7809))
                        add(LatLng(60.2175, 24.7806))
                        add(LatLng(60.2181, 24.7812))
                        add(LatLng(60.2180, 24.7811))
                        fillColor(POLYGON_FILL_COLOR)
                    }
                )
                //,
                //ZoneClusterItem(
                //    id = "zone-2",
                //    title = "Zone 2",
                //    snippet = "This is Zone 2.",
                //    polygonOptions = polygonOptions {
                //        add(LatLng(49.110, -122.554))
                //        add(LatLng(49.107, -122.559))
                //        add(LatLng(49.103, -122.551))
                //        add(LatLng(49.112, -122.549))
                //        fillColor(POLYGON_FILL_COLOR)
                //    }
                //)
            )
        )
    )
    val userLocation: MutableState<LatLng> = mutableStateOf(
        LatLng(60.2178, 24.7809)
    )

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        /*
         * Get a location which might be dated, which may be null in rare
         * cases when a location is not available.
         */
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state.value = state.value.copy(
                        lastKnownLocation = task.result,
                    )
                }
            }

        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    fun getDevicePreciseLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ): MutableState<Pair<Double, Double>> {
        /*
         * Get a fresher and more accurate location, which may be null in rare
         * cases when a location is not available.
         */
        val locationData = mutableStateOf(Pair(0.0, 0.0))
        try {
            fusedLocationProviderClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                    //Toast.makeText(context,"Cannot get location.", Toast.LENGTH_SHORT).show()
                        Log.i("MapViewModel", "Cannot get location.")
                    else {
                        locationData.value = Pair(location.latitude, location.longitude)
                    }

                }

        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
        return locationData

    }



    fun setupClusterManager(
        context: Context,
        map: GoogleMap,
    ): ZoneClusterManager {
        val clusterManager = ZoneClusterManager(context, map)
        clusterManager.addItems(state.value.clusterItems)
        return clusterManager
    }

    fun calculateZoneLatLngBounds(): LatLngBounds {
        // Get all the points from all the polygons and calculate the camera view that will show them all.
        val latLngs = state.value.clusterItems.map { it.polygonOptions }
            .map { it.points.map { LatLng(it.latitude, it.longitude) } }.flatten()
        return latLngs.calculateCameraViewPoints().getCenterOfPolygon()
    }


    companion object {
        private val POLYGON_FILL_COLOR = Color.parseColor("#ABF44336")
    }
}