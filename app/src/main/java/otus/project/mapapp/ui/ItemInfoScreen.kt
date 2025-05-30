package otus.project.mapapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import otus.project.common.*
import otus.project.mapapp.R
import otus.project.mapapp.model.MapViewModel

@Composable
fun InfoLine(name : String, data : String, modifier : Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Text(text = name, fontSize = 18.sp, modifier = modifier.width(112.dp))
        Text(text = data, fontSize = 18.sp, modifier = modifier)
    }
}

@Composable
fun ItemInfoScreen(model : MapViewModel, back : (ViewType) -> Unit) {
    val space = dimensionResource(id = R.dimen.item_padding)
    val item : Item = model.getItem(model.currentSelected)
    val place = "${model.currentPlace.latitude}, ${model.currentPlace.longitude}"
    Surface {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(1f)
        ) {
            TitleBar(stringResource(R.string.view_mode))

            InfoLine(name = stringResource(id = R.string.item_id) + " ${item.id}", data = "", Modifier.padding(space))
            HorizontalDivider()
            InfoLine(name = stringResource(id = R.string.item_name), data = item.name, Modifier.padding(space))
            InfoLine(name = stringResource(id = R.string.item_address), data = item.address, Modifier.padding(space))
            InfoLine(name = stringResource(id = R.string.item_detail), data = item.details, Modifier.padding(space))
            InfoLine(name = stringResource(id = R.string.item_group), data = item.category, Modifier.padding(space))
            InfoLine(name = stringResource(id = R.string.item_status), data = item.status, Modifier.padding(space))
            HorizontalDivider()
            InfoLine(name = stringResource(id = R.string.item_place), data = place, Modifier.padding(space))
            HorizontalDivider()

            Spacer(modifier = Modifier.weight(0.5f))

            ToolBar(
                tune = toolbar.Info,
                back = { back(model.currentViewType) },
                toMap = {
                    model.query.center = model.currentPlace
                    back(ViewType.TypeMap)
                }
            )
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun ItemInfoScreenPreview() {
    ItemInfoScreen(viewModel(), {})
}
