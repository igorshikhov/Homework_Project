package otus.project.mapapp.map

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER

import otus.project.mapapp.model.Place

object CheckLocation {
    private val timeout : Long = 30000L

    private var currentLoc: Location? = null

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

    private lateinit var locManager : LocationManager
    private val locListener : LocationListener by lazy { LocListener() }

    fun isEnabled() : Boolean {
        return locManager.isProviderEnabled(GPS_PROVIDER)
            || locManager.isProviderEnabled(NETWORK_PROVIDER)
    }

    fun create(ctx: Context) {
        locManager = ctx.getSystemService(LOCATION_SERVICE) as LocationManager
    }

    fun pause() {
        locManager.removeUpdates(locListener)
    }

    fun resume() {
        //locManager.requestLocationUpdates(provider = GPS_PROVIDER, minTimeMs = timeout, minDistanseM = 30f, listener = locListener)
        //locManager.requestLocationUpdates(provider = NETWORK_PROVIDER, minTimeMs = timeout, minDistanseM = 30f, listener = locListener)
    }
}