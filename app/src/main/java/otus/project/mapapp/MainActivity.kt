
package otus.project.mapapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import otus.project.mapapp.model.MapViewModel
import otus.project.mapapp.ui.MapApp

/**
 * Activity for application flow.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel : MapViewModel by viewModels()

    private fun locationPermissions() {
        val requiredPermissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val request = registerForActivityResult(ActivityResultContracts.RequestPermission()) { it }
        requiredPermissions.forEach {
            if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)
                request.launch(it)
        }
        val isEnabled = requiredPermissions.any {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
        if (!isEnabled) {
            Toast.makeText(applicationContext, "Определение положения не разрешено", Toast.LENGTH_LONG).show()
        }
        MapViewModel.locationEnabled = isEnabled
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationPermissions()
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

