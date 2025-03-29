package otus.project.mapapp.net

import android.graphics.Bitmap
import com.squareup.picasso.Picasso
import otus.project.mapapp.model.Place
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

private const val apikey = "demo"
private const val fieldset = "address,name,pin,place_details,type"
private const val BASE_URL = "https://demo.maps.vk.com/api/"

// pins=55.75,37.61,blue_star|60.65,38.12,red_info
private const val pin_default = "blue" // or "purple"
private const val pin_central = "pink" // or "violet"
private const val MAP_URL = "https://demo.maps.vk.com/api/staticmap/png"

class NetClient @Inject constructor() {

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

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val service : DataService by lazy { retrofit.create(DataService::class.java) }

    suspend fun getDataAsync(filter : String, count : Int, center : Place, radius : Int, onError : (String) -> Unit = {}) : List<PlaceData> {
        try {
            val response =
                service.getPlaces("${center.latitude},${center.longitude}", "$radius", "$count", filter, fieldset, apikey)
            return response.results
        }
        catch (t: Throwable) {
            onError(t.message ?: "unknown error")
        }
        return listOf()
    }

//------

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

    fun getBitmapAsync(url : String, onError : (String) -> Unit = {}) : Bitmap? {
        try {
            return Picasso.get().load(url).get()
        }
        catch (t: Throwable) {
            onError(t.message ?: "unknown error")
        }
        return null
    }
}
