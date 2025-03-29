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
import otus.project.mapapp.db.di.DatabaseModule
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
    fun testViewModel() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val client = NetClient()
        val store = MarkerStore(DatabaseModule.provideMarkerDao(DatabaseModule.provideMarkerDatabase(ctx)))
        val check = CheckLocation(ctx)
        val viewModel = MapViewModel(ctx, client, store, check)
        val item = Item(1, "объект 1", "данные 1", "адрес 1")
        val place = Place(55.75f,37.62f)
        viewModel.addItem(item, place)
        val ilist : List<Item> = viewModel.getItems()
        assertEquals(1, ilist.size)
    }

    @Test
    fun testViewModelState() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val client = NetClient()
        val store = MarkerStore(DatabaseModule.provideMarkerDao(DatabaseModule.provideMarkerDatabase(ctx)))
        val check = CheckLocation(ctx)
        val viewModel = MapViewModel(ctx, client, store, check)
        val place = viewModel.query.center
        assertNotSame(0f, place.latitude)
        assertNotSame(0f, place.longitude)
    }

    @Test
    fun testDataStore() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val store = MarkerStore(DatabaseModule.provideMarkerDao(DatabaseModule.provideMarkerDatabase(ctx)))
        val item = Item(1, "объект 1", "данные 1", "адрес 1")
        val place = Place(55.75f,37.62f)
        val ilist : MutableList<Item> = mutableListOf()
        var err : String? = null
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                store.addItem(item, place) { err = it }
                ilist.addAll(store.getItems { err = it })
            }
        }
        assertNull(err)
        assertEquals(1, ilist.size)
    }
}
