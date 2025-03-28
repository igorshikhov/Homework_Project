package otus.project.mapapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import otus.project.mapapp.R
import otus.project.mapapp.model.Item
import otus.project.mapapp.model.MapViewModel
import otus.project.mapapp.model.ViewType

@Composable
fun ItemLine(item : Item, onClick: () -> Unit, modifier : Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth(1f)
            .padding(dimensionResource(id = R.dimen.list_padding))
            .clickable(onClick = onClick)
            .testTag("ItemLine-${item.id}")
    ) {
        Text(text = item.id.toString(), fontSize = 18.sp, modifier = Modifier.width(48.dp))
        Text(text = item.name, fontSize = 18.sp)
    }
}

@Composable
fun ListScreen(getList : () -> List<Item>, back : () -> Unit, toMap : () -> Unit, toSetup : () -> Unit, onClick : (Long) -> Unit) {
    MapViewModel.currentViewType = ViewType.TypeList
    val ilist = getList()
    Surface {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(1f)
        ) {
            TitleBar(stringResource(R.string.item_list) + " (${MapViewModel.currentFilter})")

            LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxHeight(0.93f)
                    .padding(dimensionResource(id = R.dimen.list_padding))
                    .testTag("ViewList-LazyColumn")
            ) {
                items(ilist.size, { ilist[it].id } ) { index ->
                    ItemLine(ilist[index], { onClick(ilist[index].id) })
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            ToolBar(
                tune = toolbar.List,
                back = back,
                toMap = toMap,
                toSetup = toSetup
            )
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun ListScreenPreview() {
    ListScreen({ listOf(Item(1, "объект 1", "данные 1", "адрес 1")) }, {}, {}, {}, {})
}
