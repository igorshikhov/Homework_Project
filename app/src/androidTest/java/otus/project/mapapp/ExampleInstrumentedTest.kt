package otus.project.mapapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import otus.project.mapapp.db.MarkerStore
import otus.project.mapapp.loc.CheckLocation
import otus.project.mapapp.model.Item
import otus.project.mapapp.model.MapViewModel
import otus.project.mapapp.model.Place
import otus.project.mapapp.net.NetClient
import otus.project.mapapp.net.PlaceData

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun testAppContext() {
        // Context of the app under test.
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("otus.project.mapapp", ctx.packageName)
    }

    @Test
    fun testCheckLocation() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val check = CheckLocation(ctx)
        assertEquals(true, check.isEnabled())
    }

    @Test
    fun testGetLocation() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val check = CheckLocation(ctx)
        assertEquals(true, check.isEnabled())
        if (check.isLocationFound(false)) {
            val place: Place? = check.getLocation()
            assertNotNull(place)
            assertNotSame(0f, place?.latitude ?: 0f)
            assertNotSame(0f, place?.longitude ?: 0f)
        }
    }

    @Test
    fun testNetClient() {
        val client = NetClient()
        val center = Place(55.75f,37.62f)
        val url = client.getImageUrl(center, 12, "main", 512, 512)
        var err : String? = null
        val bmp = client.getBitmapAsync(url, { err = it })
        assertNull(err)
        assertNotNull(bmp)
    }

    @Test
    fun testNetClientData() {
        val client = NetClient()
        val center = Place(55.75f,37.62f)
        val filter = "monument"
        var idata : PlaceData? = null
        var err : String? = null
        runBlocking(Dispatchers.IO) {
            launch {
                val data: Deferred<List<PlaceData>> = async {
                    client.getDataAsync(filter, 1, center, 1000, { err = it })
                }
                val result = data.await()
                if (result.size > 0)
                    idata = result.first()
            }
        }
        assertNull(err)
        assertNotNull(idata)
        assertNotNull(idata?.name)
        assertNotNull(idata?.type)
    }

    @Test
    fun testViewModel() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val viewModel = MapViewModel(ctx)
        val item = Item(1, "объект 1", "данные 1", "адрес 1")
        val place = Place(55.75f,37.62f)
        viewModel.addItem(item, place)
        val ilist : List<Item> = viewModel.getItems()
        assertEquals(1, ilist.size)
    }

    @Test
    fun testViewModelState() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val viewModel = MapViewModel(ctx)
        val place = viewModel.query.center
        assertNotSame(0f, place.latitude)
        assertNotSame(0f, place.longitude)
    }

    @Test
    fun testDataStore() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val store = MarkerStore(ctx)
        val item = Item(1, "объект 1", "данные 1", "адрес 1")
        val place = Place(55.75f,37.62f)
        val ilist : MutableList<Item> = mutableListOf()
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                store.addItem(item, place)
                ilist.addAll(store.getItems())
            }
        }
        assertEquals(1, ilist.size)
    }
}
