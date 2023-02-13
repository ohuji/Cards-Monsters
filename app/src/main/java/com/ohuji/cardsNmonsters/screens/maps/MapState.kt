package com.ohuji.cardsNmonsters.screens.maps

import android.location.Location
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterItem

data class MapState(
    val lastKnownLocation: Location?,
    val clusterItems: List<ZoneClusterItem>,
)