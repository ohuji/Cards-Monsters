/**
 * Unit tests for the ZoneClusterItem class.
 */
package com.ohuji.cardsNmonsters

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterItem
import com.ohuji.cardsNmonsters.screens.maps.clusters.getCenterOfPolygon
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ZoneClusterItemTest {
    // Test data
    private val id = "test_id"
    private val title = "Test Zone"
    private val snippet = "This is a test zone"
    private val latLng1 = LatLng(0.0, 0.0)
    private val latLng2 = LatLng(0.0, 1.0)
    private val latLng3 = LatLng(1.0, 1.0)
    private val latLng4 = LatLng(1.0, 0.0)
    private val polygonOptions = PolygonOptions()
        .add(latLng1, latLng2, latLng3, latLng4)
    private val clusterItem = ZoneClusterItem(id, title, snippet, polygonOptions)

    /**
     * Test getSnippet method.
     */
    @Test
    fun testGetSnippet() {
        // Assert that getSnippet returns the correct snippet
        assertEquals(snippet, clusterItem.snippet)
    }

    /**
     * Test getTitle method.
     */
    @Test
    fun testGetTitle() {
        // Assert that getTitle returns the correct title
        assertEquals(title, clusterItem.title)
    }

    /**
     * Test getPosition method.
     */
    @Test
    fun testGetPosition() {
        // Assert that getPosition returns the center of the polygon
        val expectedPosition = polygonOptions.points.getCenterOfPolygon().center
        assertEquals(expectedPosition, clusterItem.position)
    }

    /**
     * Test cluster item properties.
     */
    @Test
    fun testClusterItemProperties() {
        // Assert that the cluster item properties are set correctly
        assertEquals(id, clusterItem.id)
        assertEquals(title, clusterItem.title)
        assertEquals(snippet, clusterItem.snippet)
        assertEquals(polygonOptions, clusterItem.polygonOptions)
    }
}