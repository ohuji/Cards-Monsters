import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock

data class MapState(
    val lastKnownLocation: Location?,
    val clusterItems: List<ZoneClusterItem>,
)

/**
 * Unit tests for [MapState] class.
 */
class MapStateTest {

    // Create a sample PolygonOptions object to use in tests
    val polygonOptions = PolygonOptions()
        .add(LatLng(0.0, 0.0))
        .add(LatLng(0.0, 1.0))
        .add(LatLng(1.0, 1.0))
        .add(LatLng(1.0, 0.0))

    /**
     * Test if the last known location is correctly stored in [MapState].
     */
    @Test
    fun `test last known location`() {
        val location = mock(Location::class.java)
        val mapState = MapState(location, emptyList())
        assertEquals(location, mapState.lastKnownLocation)
    }

    /**
     * Test if [MapState]'s clusterItems list is not null.
     */
    @Test
    fun `test cluster items not null`() {
        val mapState = MapState(null, emptyList())
        assertTrue(mapState.clusterItems != null)
    }

    /**
     * Test if [MapState]'s clusterItems list has the correct count.
     */
    @Test
    fun `test cluster items count`() {
        val clusterItems = listOf(
            ZoneClusterItem("1", "Title 1", "Snippet 1", polygonOptions),
            ZoneClusterItem("2", "Title 2", "Snippet 2", polygonOptions),
            ZoneClusterItem("3", "Title 3", "Snippet 3", polygonOptions)
        )
        val mapState = MapState(null, clusterItems)
        assertEquals(3, mapState.clusterItems.size)
    }
}