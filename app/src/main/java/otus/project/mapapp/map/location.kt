package otus.project.mapapp.map

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import android.os.Build.VERSION_CODES.S
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import otus.project.mapapp.model.Place
import javax.inject.Inject

@RequiresApi(S)
class CheckLocation @Inject constructor(@ApplicationContext private val ctx : Context) {

    companion object {
        private const val timeout : Long = 30000L
        private const val minDist : Float = 30f
        private var currentLoc: Location? = null
    }

    private val locManager : LocationManager by lazy { ctx.getSystemService(LOCATION_SERVICE) as LocationManager }

    private val locListener : LocationListener by lazy { LocListener() }

    fun isLocationFound() : Boolean = currentLoc != null

    fun getLocation() : Place {
        return Place(currentLoc?.latitude?.toFloat() ?: 0f, currentLoc?.latitude?.toFloat() ?: 0f)
    }

    private class LocListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            super.onLocationChanged(mutableListOf(location))
            currentLoc = location
        }
/*
        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }
*/
    }

    fun isEnabled() : Boolean {
        return locManager.isProviderEnabled(GPS_PROVIDER)
            || locManager.isProviderEnabled(NETWORK_PROVIDER)
    }

    fun pause() {
        locManager.removeUpdates(locListener)
    }

    fun resume() {
        //requestLocationUpdates(String provider, long minTimeMs, float minDistanceM, LocationListener listener)
        //locManager.requestLocationUpdates(provider = GPS_PROVIDER, minTimeMs = timeout, minDistanseM = minDist, listener = locListener)
        //locManager.requestLocationUpdates(provider = NETWORK_PROVIDER, minTimeMs = timeout, minDistanseM = minDist, listener = locListener)
    }
}

