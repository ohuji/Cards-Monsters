package com.ohuji.cardsNmonsters

import com.google.android.gms.maps.model.LatLng
import com.ohuji.cardsNmonsters.screens.maps.MapViewModel
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterItem
import io.mockk.MockKAnnotations
import io.mockk.mockk
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Test

class MapViewModelTest {

    private lateinit var viewModel: MapViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = MapViewModel()
    }

    @Test
    fun `addClusterItem adds new cluster item to state`() {
        // Given
        val clusterItem = mockk<ZoneClusterItem>()

        // When
        viewModel.addClusterItem(clusterItem)

        // Then
        val currentState = viewModel.state.value
        assertTrue(currentState.clusterItems.contains(clusterItem))
    }

    @Test
    fun `removeClusterItems removes all cluster items from state`() {
        // Given
        val clusterItem1 = mockk<ZoneClusterItem>()
        val clusterItem2 = mockk<ZoneClusterItem>()
        viewModel.addClusterItem(clusterItem1)
        viewModel.addClusterItem(clusterItem2)

        // When
        viewModel.removeClusterItems()

        // Then
        val currentState = viewModel.state.value
        assertTrue(currentState.clusterItems.isEmpty())
    }

    @Test
    fun `generateLatLng returns correct latitude and longitude`() {
        // Given
        val startingLatLng = LatLng(37.7749, -122.4194)
        val expectedLatLng = LatLng(37.774963520455614, -122.41926324607707)

        // When
        val resultLatLng = viewModel.generateLatLng(startingLatLng, 10.0, 45.0)

        // Then
        assertEquals(expectedLatLng.latitude, resultLatLng.latitude, 0.0001)
        assertEquals(expectedLatLng.longitude, resultLatLng.longitude, 0.0001)
    }
}

