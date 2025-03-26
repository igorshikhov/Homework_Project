
package otus.project.mapapp

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import otus.project.mapapp.model.MapViewModel
import otus.project.mapapp.ui.MapApp

/**
 * Activity for application flow.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //private val viewModel : MapViewModel by lazy { MapViewModel(applicationContext) }
    private val viewModel : MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapApp(viewModel, { this.setTitle(it) }, { this.finish() })
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onResume() {
        super.onResume()
        if (viewModel.check.isEnabled()) {
            viewModel.check.resume()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onPause() {
        super.onPause()
        if (viewModel.check.isEnabled()) {
            viewModel.check.pause()
        }
    }
}

