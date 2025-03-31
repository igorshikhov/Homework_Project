package otus.project.feature.db

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

import otus.project.common.Item
import otus.project.common.Place

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("otus.project.feature.db.test", appContext.packageName)
    }

    @Test
    fun testDataStore() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val store = MarkerStore(DatabaseModule.provideMarkerDao(DatabaseModule.provideMarkerDatabase(ctx)))
        val item = Item(1, "объект 1", "данные 1", "адрес 1")
        val place = Place(55.75f, 37.62f)
        val ilist : MutableList<otus.project.common.Item> = mutableListOf()
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