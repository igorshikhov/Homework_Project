package otus.project.mapapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import otus.project.common.*
import otus.project.mapapp.R
import otus.project.mapapp.model.MapViewModel

@Composable
fun Select(name : String, selected : Boolean, onClick : () -> Unit, modifier : Modifier, isEnabled : Boolean = true) {
    val color : Color = colorResource(id = if (isEnabled) R.color.black else R.color.medium_gray)
    RadioButton(selected = selected, onClick = { if (isEnabled) onClick() }, enabled = isEnabled)
    Text(name, fontSize = 18.sp, modifier = modifier.selectable(selected = selected, onClick = { if (isEnabled) onClick() }), color = color)
}

@Composable
fun InputNumber(title : String, value : Int, modifier : Modifier, tag : String) : Int {
    var data = remember { mutableStateOf(value) }

    Row(horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Text(text = title,
            fontSize = 18.sp,
            modifier = modifier.align(Alignment.CenterVertically)
        )
        TextField(value = data.value.toString(),
            onValueChange = { data.value = if (it.length == 0) 0 else it.toInt() },
            textStyle = TextStyle(fontSize = 20.sp),
            modifier = modifier
                .width(112.dp)
                .testTag("Settings-$tag"),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
    return data.value
}

@Composable
fun InputString(title : String, value : String, modifier : Modifier, tag : String) : String {
    var data = remember { mutableStateOf(value) }

    Row(horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Text(text = title,
            fontSize = 18.sp,
            modifier = modifier.align(Alignment.CenterVertically)
        )
        TextField(value = data.value,
            onValueChange = { data.value = it },
            textStyle = TextStyle(fontSize = 20.sp),
            modifier = modifier.testTag("Settings-$tag")
        )
    }
    return data.value
}

@Composable
fun ToggleFlag(title : String, value : Boolean, modifier : Modifier, isEnabled: Boolean = true) : Boolean {
    var data = remember { mutableStateOf(value) }

    Row(horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Text(text = title,
            fontSize = 18.sp,
            modifier = modifier.align(Alignment.CenterVertically)
        )
        Switch(checked = data.value,
            onCheckedChange = { data.value = it },
            modifier = modifier,
            enabled = isEnabled
        )
    }
    return data.value
}

@Composable
fun SetupScreen(model : MapViewModel, back : (ViewType) -> Unit) {
    val space = dimensionResource(id = R.dimen.item_padding)
    val (selectedMode, onModeSelected) = remember { mutableStateOf(model.currentViewMode) }
    val (selectedStyle, onStyleSelected) = remember { mutableStateOf(model.query.style) }

    Surface {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(1f)
        ) {
            TitleBar(stringResource(R.string.settings))

            Text(text = stringResource(id = R.string.map_mode), fontSize = 18.sp,
                modifier = Modifier
                    .padding(space)
                    .width(128.dp)
            )
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Select(stringResource(R.string.view_mode),
                    selectedMode == ViewMode.ModeView,
                    { onModeSelected(ViewMode.ModeView) },
                    Modifier.align(Alignment.CenterVertically)
                )
                Select(stringResource(R.string.edit_mode),
                    selectedMode == ViewMode.ModeEdit,
                    { onModeSelected(ViewMode.ModeEdit) },
                    Modifier.align(Alignment.CenterVertically),
                    false
                )
            }

            HorizontalDivider()

            Text(text = stringResource(id = R.string.map_style), fontSize = 18.sp,
                modifier = Modifier.padding(space)
            )
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Select(stringResource(R.string.map_main),
                    selectedStyle == MapStyle.Main,
                    { onStyleSelected(MapStyle.Main) },
                    Modifier.align(Alignment.CenterVertically)
                )
                Select(stringResource(R.string.map_dark),
                    selectedStyle == MapStyle.Dark,
                    { onStyleSelected(MapStyle.Dark) },
                    Modifier.align(Alignment.CenterVertically)
                )
                Select(stringResource(R.string.map_simple),
                    selectedStyle == MapStyle.Light,
                    { onStyleSelected(MapStyle.Light) },
                    Modifier.align(Alignment.CenterVertically)
                )
            }

            HorizontalDivider()

            Text(text = stringResource(id = R.string.query_settings), fontSize = 18.sp,
                modifier = Modifier.padding(space))

            val limit = InputNumber(stringResource(id = R.string.quantity), model.query.limit, Modifier.padding(space), "Limit")
            val radius = InputNumber(stringResource(id = R.string.radius), model.query.radius, Modifier.padding(space), "Radius")
            val filter = InputString(stringResource(id = R.string.filter), model.query.filter, Modifier.padding(space), "Filter")
            val clearData = ToggleFlag(stringResource(id = R.string.clear_changed), model.state.resetOnChange, Modifier.padding(space))

            HorizontalDivider()

            val useDb = ToggleFlag(stringResource(id = R.string.use_database), model.state.useSourceDb, Modifier.padding(space), false)
            val checkLoc = ToggleFlag(stringResource(id = R.string.check_location), model.state.checkLocation, Modifier.padding(space), model.state.locationEnabled)

            HorizontalDivider()

            Spacer(modifier = Modifier.weight(0.5f))

            ToolBar(
                tune = toolbar.Mode,
                back = { back(model.currentViewType) },
                save = {
                    model.state.useSourceDb = useDb
                    model.currentViewMode = selectedMode
                    model.query.style = selectedStyle
                    model.state.checkLocation = checkLoc
                    model.state.resetOnChange = clearData
                    if (model.query.limit != limit ||
                        model.query.radius != radius ||
                        model.query.filter.compareTo(filter) != 0)
                    {   // изменились параметры запроса
                        model.query.limit = limit
                        model.query.radius = radius
                        model.query.filter = filter
                        if (model.state.resetOnChange) {
                            model.clearData()
                        }
                    }
                }
            )
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun SetupScreenPreview() {
    SetupScreen(viewModel(), {})
}
