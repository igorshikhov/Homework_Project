package otus.project.mapapp.net

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import otus.project.mapapp.model.Place
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object NetClient {

    private interface DataService {
        @GET("places")
        suspend fun getPlaces(
            @Query("location") loc: String,
            @Query("radius") radius: String,
            @Query("limit") limit: String,
            @Query("q") q: String,
            @Query("fields") fields: String,
            @Query("api_key") key: String
        ) : PlaceResponse
    }

    private const val apikey = "demo"
    private const val fieldset = "address,name,pin,place_details,type"
    private const val BASE_URL = "https://demo.maps.vk.com/api/"

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val service : DataService by lazy { retrofit.create(DataService::class.java) }

    fun getDataAsync(filter : String, count : Int, center : Place, radius : Int, ctx: Context) : List<PlaceData> {
        var err: String? = null
        var lst: List<PlaceData> = listOf()
        runBlocking(Dispatchers.IO) {
            launch {
                try {
                    val place: String = "${center.latitude},${center.longitude}"
                    val response =
                        service.getPlaces(place, "$radius", "$count", filter, fieldset, apikey)
                    lst = response.results
                }
                catch (t: Throwable) {
                    err = t.message ?: "unknown error"
                }
            }
        }

        if (err != null) {
            Toast.makeText(ctx, err, Toast.LENGTH_LONG).show()
        }
        return lst
    }

//------

    // pins=55.75,37.61,blue_star|60.65,38.12,red_info
    private const val pin_default = "blue" // "purple"
    private const val pin_central = "pink" // "violet"
    private const val MAP_URL = "https://demo.maps.vk.com/api/staticmap/png"

    fun getImageUrl(center : Place, zoom : Int, style : String, width : Int, height : Int, pins : Map<Long, Place> = mapOf(), selected : Long = 0) : String {
        var param = "?api_key=$apikey&latlon=${center.latitude},${center.longitude}&zoom=$zoom&style=$style&width=$width&height=$height&scale=2"
        if (!pins.isEmpty()) {
            var count = 0
            param += "&pins="
            for (p in pins) {
                val index = if (p.key < 10) "${p.key}" else "star"
                val color = if (p.key == selected) pin_central else pin_default
                if (count++ > 0) param += "|"
                param += "${p.value.latitude},${p.value.longitude},${color}_$index"
            }
        }
        return MAP_URL + param
    }

    fun getBitmapAsync(url : String, ctx : Context) : Bitmap? {
        var err : String? = null
        var bmp : Bitmap? = null
        runBlocking(Dispatchers.IO) {
            launch {
                try {
                    bmp = Picasso.get().load(url).get()
                }
                catch (t: Throwable) {
                    err = t.message ?: "unknown error"
                }
            }
        }
        if (err != null) {
            Toast.makeText(ctx, err, Toast.LENGTH_LONG).show()
        }
        return bmp
    }
}
