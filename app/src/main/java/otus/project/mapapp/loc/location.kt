package otus.project.mapapp.loc

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import otus.project.mapapp.model.Place
import javax.inject.Inject

fun Location.toPlace() : Place = Place(latitude.toFloat(), longitude.toFloat())

@RequiresApi(Build.VERSION_CODES.S)
class CheckLocation @Inject constructor(@ApplicationContext private val ctx : Context) {

    companion object {
        //private const val timeout : Long = 30000L
        //private const val minDist : Float = 30f
        private var currentLoc: Location? = null
    }

    private val locManager: LocationManager by lazy { ctx.getSystemService(LOCATION_SERVICE) as LocationManager }

    private val locListener: LocationListener by lazy { LocListener() }

    fun isEnabled() : Boolean {
        return locManager.isProviderEnabled(GPS_PROVIDER)
            || locManager.isProviderEnabled(NETWORK_PROVIDER)
    }

    fun getLocation() : Place? = currentLoc?.toPlace()

    fun isLocationFound(showErr : Boolean = true) : Boolean {
        try {
            if (locManager.isProviderEnabled(NETWORK_PROVIDER)) {
                currentLoc = locManager.getLastKnownLocation(NETWORK_PROVIDER)
            }
            if (locManager.isProviderEnabled(GPS_PROVIDER)) {
                currentLoc = locManager.getLastKnownLocation(GPS_PROVIDER)
            }
        }
        catch (t: Throwable) {
            if (showErr)
                Toast.makeText(ctx, t.message ?: "unknown error", Toast.LENGTH_LONG).show()
        }
        return currentLoc != null
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

    fun pause() {
        locManager.removeUpdates(locListener)
    }

    fun resume() {
        // requestLocationUpdates(String provider, long minTimeMs, float minDistanceM, LocationListener listener)
        //locManager.requestLocationUpdates(provider = GPS_PROVIDER, minTimeMs = timeout, minDistanseM = minDist, listener = locListener)
        //locManager.requestLocationUpdates(provider = NETWORK_PROVIDER, minTimeMs = timeout, minDistanseM = minDist, listener = locListener)
    }
}

