package otus.project.mapapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import otus.project.mapapp.R
import otus.project.mapapp.model.MapViewModel
import otus.project.mapapp.model.Place
import otus.project.mapapp.model.ViewType

private const val iWidth = 576
private const val iHeight = 1000

fun mapImage(model : MapViewModel) : ImageBitmap? {
    return model.getMapImage(iWidth, iHeight)?.asImageBitmap()
}

@Composable
fun MapScreen(model : MapViewModel, back : () -> Unit, toList : () -> Unit, toSetup : () -> Unit, onClick : (Place) -> Unit = {}) {
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
    model.currentViewType = ViewType.TypeMap
    val image = mapImage(model)
    var bitmap = remember { mutableStateOf(image) }
    Surface {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(1f)
        ) {
            TitleBar(stringResource(R.string.map_view) + " (${model.query.filter})")

            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth(1f)
                    .padding(dimensionResource(id = R.dimen.list_padding))
            ) {
                Image(
                    bitmap = bitmap.value ?: ImageBitmap.Companion.imageResource(id = R.drawable.map800),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.testTag("ViewMap-Image")
                )
            }

            Row(horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                Text(text = "zoom: " + model.query.zoom.toString(),
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
                    if (model.query.zoom < 17) {
                        ++model.query.zoom
                        bitmap.value = mapImage(model)
                    }
                },
                zoomOut = {
                    if (model.query.zoom > 1) {
                        --model.query.zoom
                        bitmap.value = mapImage(model)
                    }
                }
            )
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun MapScreenPreview() {
    MapScreen(viewModel(), {}, {}, {}, {})
}
