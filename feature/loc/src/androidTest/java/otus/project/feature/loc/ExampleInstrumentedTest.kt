package otus.project.feature.loc

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

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
        assertEquals("otus.project.feature.loc.test", appContext.packageName)
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
}