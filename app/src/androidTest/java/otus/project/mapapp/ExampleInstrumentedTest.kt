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
import otus.project.common.*
import otus.project.common.net.NetClient
import otus.project.common.net.PlaceData
import otus.project.feature.db.MarkerStore
import otus.project.feature.db.DatabaseModule
import otus.project.mapapp.model.MapViewModel

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
    fun testViewModel() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val client = NetClient()
        val store = MarkerStore(DatabaseModule.provideMarkerDao(DatabaseModule.provideMarkerDatabase(ctx)))
        val check = otus.project.feature.loc.CheckLocation(ctx)
        val viewModel = MapViewModel(ctx, client, store, check)
        val item = Item(1, "объект 1", "данные 1", "адрес 1")
        val place = Place(55.75f, 37.62f)
        viewModel.addItem(item, place)
        val ilist : List<Item> = viewModel.getItems()
        assertEquals(1, ilist.size)
    }

    @Test
    fun testViewModelItem() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val client = NetClient()
        val store = MarkerStore(DatabaseModule.provideMarkerDao(DatabaseModule.provideMarkerDatabase(ctx)))
        val check = otus.project.feature.loc.CheckLocation(ctx)
        val viewModel = MapViewModel(ctx, client, store, check)
        val item = Item(1, "объект 1", "данные 1", "адрес 1")
        val place = Place(55.75f, 37.62f)
        viewModel.addItem(item, place)
        val found : Item = viewModel.getItem(1)
        assertEquals(1, found.id)
    }

    @Test
    fun testViewModelPlace() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val client = NetClient()
        val store = MarkerStore(DatabaseModule.provideMarkerDao(DatabaseModule.provideMarkerDatabase(ctx)))
        val check = otus.project.feature.loc.CheckLocation(ctx)
        val viewModel = MapViewModel(ctx, client, store, check)
        val item = Item(1, "объект 1", "данные 1", "адрес 1")
        val place = Place(55.75f, 37.62f)
        viewModel.addItem(item, place)
        val found : Place = viewModel.getPlace(1)
        assertEquals(place.latitude, found.latitude)
        assertEquals(place.longitude, found.longitude)
    }

    @Test
    fun testViewModelFind() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val client = NetClient()
        val store = MarkerStore(DatabaseModule.provideMarkerDao(DatabaseModule.provideMarkerDatabase(ctx)))
        val check = otus.project.feature.loc.CheckLocation(ctx)
        val viewModel = MapViewModel(ctx, client, store, check)
        val item = Item(1, "объект 1", "данные 1", "адрес 1")
        val place = Place(55.75f, 37.62f)
        viewModel.addItem(item, place)
        val id = viewModel.findIdByPlace(place)
        assertEquals(1, id)
    }

    @Test
    fun testNetClient() {
        val client = NetClient()
        val center = Place(55.75f, 37.62f)
        val url = client.getImageUrl(center, 12, "main", 512, 512)
        var err : String? = null
        val bmp = client.getBitmapAsync(url) { err = it }
        assertNull(err)
        assertNotNull(bmp)
    }

    @Test
    fun testNetClientData() {
        val client = NetClient()
        val center = Place(55.75f, 37.62f)
        val filter = "monument"
        var idata : PlaceData? = null
        var err : String? = null
        runBlocking(Dispatchers.IO) {
            launch {
                val data: Deferred<List<PlaceData>> = async {
                    client.getDataAsync(filter, 1, center, 1000) { err = it }
                }
                val result = data.await()
                if (result.isNotEmpty())
                    idata = result.first()
            }
        }
        assertNull(err)
        assertNotNull(idata)
        assertNotNull(idata?.name)
        assertNotNull(idata?.type)
    }
}
