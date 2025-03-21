package otus.project.mapapp.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import otus.project.mapapp.R
import otus.project.mapapp.model.MapViewModel
import otus.project.mapapp.model.Place
import otus.project.mapapp.model.ViewType

@Composable
fun mapFixed() : Painter {
    return painterResource(id = R.drawable.map512)
}

@Composable
fun mapImage(getImage : (Int, Int) -> Bitmap?) : Bitmap? {
    Box { CircularProgressIndicator() }
    return getImage(576, 1000)
}

@Composable
fun ViewMap(getImage : (Int, Int) -> Bitmap?, back : () -> Unit, toList : () -> Unit, toSetup : () -> Unit, onClick : (Place) -> Unit = {}) {
/*
    val options = MapStartOptions(
        center = LatLon(center.latitude.toDouble(), center.longitude.toDouble()),
        zoomLevel = MapViewModel.currentZoom.toFloat(),
        style = MapStyle.Simple,
        CompassLocationMode.StableArrow,
        LogoConfig(LogoConfig.Alignment.TopRight)
    )
    MapGlobalConfig.setMapStartOptions(options)
*/
    MapViewModel.currentViewType = ViewType.TypeMap
    val image = mapImage(getImage)
    var bitmap = remember { mutableStateOf(image) }
    Surface {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(1f)
        ) {
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth(1f)
                    .padding(dimensionResource(id = R.dimen.list_padding))
            ) {
                val imageBmp = bitmap.value?.asImageBitmap()
                if (imageBmp != null) {
                    Image(bitmap = imageBmp, contentDescription = "", contentScale = ContentScale.Fit)
                }
                else {
                    Image(painter = mapFixed(), contentDescription = "", contentScale = ContentScale.Fit)
                }
            }

            Row(horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                Text(text = "zoom: " + MapViewModel.currentZoom.toString(),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.list_padding))
                )
            }

            Spacer(modifier = Modifier.weight(0.5f))

            ToolBar(
                tune = toolbar.Map,
                back = back,
                toList = toList,
                toSetup = toSetup,
                zoomIn = {
                    if (MapViewModel.currentZoom < 17) {
                        ++MapViewModel.currentZoom
                        bitmap.value = getImage(576, 1000)
                    }
                },
                zoomOut = {
                    if (MapViewModel.currentZoom > 1) {
                        --MapViewModel.currentZoom
                        bitmap.value = getImage(576, 1000)
                    }
                }
            )
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun ViewMapPreview() {
    ViewMap({ _, _ -> null }, {}, {}, {}, {})
}
