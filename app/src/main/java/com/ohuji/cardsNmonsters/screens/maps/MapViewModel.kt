package com.ohuji.cardsNmonsters.screens.maps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterItem
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterManager
import com.ohuji.cardsNmonsters.screens.maps.clusters.calculateCameraViewPoints
import com.ohuji.cardsNmonsters.screens.maps.clusters.getCenterOfPolygon
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * ViewModel class for the map screen of the Cards 'n' Monsters app.
 *
 * @property state A mutable state object that contains the current state of the map.
 */
@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {

    var state: MutableState<MapState> = mutableStateOf(
        MapState(
            lastKnownLocation = null,
            clusterItems = listOf()
        )
    )

    /**
     * Adds a new cluster item to the current state of the map.
     *
     * @param clusterItem The cluster item to add to the map.
     */
    fun addClusterItem(clusterItem: ZoneClusterItem) {
        state.value = state.value.copy(
            clusterItems = state.value.clusterItems + clusterItem
        )
    }

    /**
     * Removes all cluster items from the current state of the map.
     */
    fun removeClusterItems() {
        state.value = state.value.copy(
            clusterItems = listOf()
        )
    }

    /**
     * Calculates a new latitude and longitude pair based on a starting pair, range, and bearing.
     *
     * @param latLng The starting latitude and longitude pair.
     * @param range The distance to move from the starting point, in meters.
     * @param bearing The direction to move in, in degrees.
     * @return A new latitude and longitude pair based on the starting pair, range, and bearing.
     */
    fun generateLatLng(
        latLng: LatLng,
        range: Double,
        bearing: Double
    ): LatLng {

        val earthRadius = 6378137.0
        val degreesToRadians = Math.PI / 180.0
        val radiansToDegrees = 180.0 / Math.PI

        val latA = latLng.latitude * degreesToRadians
        val lonA = latLng.longitude * degreesToRadians
        val angularDistance = range / earthRadius
        val trueCourse = bearing * degreesToRadians

        val lat = asin(
            sin(latA) * cos(angularDistance) +
                    cos(latA) * sin(angularDistance) * cos(trueCourse)
        )

        val dLon = atan2(
            sin(trueCourse) * sin(angularDistance) * cos(latA),
            cos(angularDistance) - sin(latA) * sin(lat)
        )

        val lon = (lonA + dLon + Math.PI) % (Math.PI * 2) - Math.PI
        return LatLng(lat * radiansToDegrees, lon * radiansToDegrees)
    }

    /**
     * Retrieves the last known location of the user's device.
     *
     * @param fusedLocationProviderClient The fused location provider client to use for retrieving the user's location.
     */
    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {

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

    /**
     * Retrieves the current, most accurate location of the user's device.
     *
     * @param fusedLocationProviderClient The fused location provider client to use for retrieving the user's location.
     * @return A mutable state object containing the user's current location as a latitude and longitude pair.
     */
    fun getDevicePreciseLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ): MutableState<LatLng> {
        /*
         * Get a fresher and more accurate location, which may be null in rare
         * cases when a location is not available.
         */
        val locationData = mutableStateOf(LatLng(0.0, 0.0))
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
                        locationData.value = LatLng(0.0, 0.0)
                    else {
                        locationData.value = LatLng(location.latitude, location.longitude)
                    }

                }

        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
        return locationData

    }


    /**
     * Sets up a cluster manager for the given map using the current state of the view model.
     *
     * @param context The context to use for setting up the cluster manager.
     * @param map The Google Map to set up the cluster manager for.
     * @return A new cluster manager object for the given map.
     */
    fun setupClusterManager(
        context: Context,
        map: GoogleMap,
    ): ZoneClusterManager {
        val clusterManager = ZoneClusterManager(context, map)
        clusterManager.addItems(state.value.clusterItems)
        return clusterManager
    }

    /**
     * Calculates the bounding box for all polygons in the current state of the view model.
     *
     * @return A bounding box object for all polygons in the current state of the view model.
     */
    fun calculateZoneLatLngBounds(): LatLngBounds {
        // Get all the points from all the polygons and calculate the camera view that will show them all.
        val latLngs = state.value.clusterItems.map { it.polygonOptions }
            .map { it -> it.points.map { LatLng(it.latitude, it.longitude) } }.flatten()
        return latLngs.calculateCameraViewPoints().getCenterOfPolygon()
    }


}