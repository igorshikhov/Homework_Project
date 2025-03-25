
package otus.project.mapapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import otus.project.mapapp.map.CheckLocation
import otus.project.mapapp.model.MapViewModel
import otus.project.mapapp.ui.MapApp
import javax.inject.Inject

/**
 * Activity for application flow.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //private val viewModel : MapViewModel by viewModels()

    private val viewModel : MapViewModel by lazy { MapViewModel(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CheckLocation.create(applicationContext)
        setContent {
            MapApp(viewModel, { this.setTitle(it) }, { this.finish() })
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

