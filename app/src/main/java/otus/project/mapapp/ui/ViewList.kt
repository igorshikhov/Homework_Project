package otus.project.mapapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
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
    ) {
        Text(text = item.id.toString(), fontSize = 18.sp, modifier = Modifier.width(48.dp))
        Text(text = item.name, fontSize = 18.sp)
    }
}

@Composable
fun itemList(getList : () -> List<Item>) : List<Item> {
    Box { CircularProgressIndicator(color = colorResource(id = R.color.medium_purple)) }
    return getList()
}

@Composable
fun ViewList(getList : () -> List<Item>, back : () -> Unit, toMap : () -> Unit, toSetup : () -> Unit, onClick : (Long) -> Unit) {
    MapViewModel.currentViewType = ViewType.TypeList
    val ilist = itemList(getList)
    Surface {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(1f)
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxHeight(0.93f)
                    .padding(dimensionResource(id = R.dimen.list_padding))
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
fun ViewListPreview() {
    ViewList({ listOf(Item(1, "объект 1", "данные 1", "адрес 1")) }, {}, {}, {}, {})
}
