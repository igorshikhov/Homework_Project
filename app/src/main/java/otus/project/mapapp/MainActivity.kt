/*
 */
package otus.project.mapapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import otus.project.mapapp.db.MarkerStore
import otus.project.mapapp.map.CheckLocation
import otus.project.mapapp.model.MapViewModel
import otus.project.mapapp.ui.MapApp
import javax.inject.Inject

/**
 * Activity for application flow.
 */
//@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //@Inject lateinit var store : MarkerStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CheckLocation.create(applicationContext)
        setContent {
            MapApp(MapViewModel(applicationContext, MarkerStore(applicationContext)), { this.setTitle(it) }, { this.finish() })
        }
    }
/* */
    override fun onResume() {
        super.onResume()
        if (CheckLocation.isEnabled()) {
            CheckLocation.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (CheckLocation.isEnabled()) {
            CheckLocation.pause()
        }
    }
/* */
}

