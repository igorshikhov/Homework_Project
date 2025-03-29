package otus.project.mapapp

import android.os.Parcel
import android.os.Parcelable
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import otus.project.mapapp.model.Place
import otus.project.mapapp.net.NetClient
import otus.project.mapapp.net.PlaceData

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

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
}