package otus.project.mapapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import otus.project.common.*
import otus.project.mapapp.R

@Composable
fun EditLine(name : String, data : String = "", modifier: Modifier = Modifier) : String {
    var input = remember { mutableStateOf(data) }
    Row(horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Text(text = name, fontSize = 18.sp, modifier = modifier
            .align(Alignment.CenterVertically)
            .width(96.dp)
        )
        TextField(value = input.value,
            onValueChange = { input.value = it },
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = modifier
        )
    }
    return input.value
}

@Composable
fun ItemEditScreen(back : () -> Unit, save : (Item) -> Unit) {
    val item = Item()
    val spaces = dimensionResource(id = R.dimen.item_padding)

    Surface {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(1f)
        ) {
            TitleBar(stringResource(R.string.edit_mode))

            item.name = EditLine(name = stringResource(id = R.string.item_name), modifier = Modifier.padding(spaces))
            item.address = EditLine(name = stringResource(id = R.string.item_address), modifier = Modifier.padding(spaces))
            item.details = EditLine(name = stringResource(id = R.string.item_detail), modifier = Modifier.padding(spaces))

            Spacer(modifier = Modifier.weight(1f))

            ToolBar(
                tune = toolbar.Item,
                back = back,
                save = { save(item) }
            )
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun ItemEditScreenPreview() {
    ItemEditScreen({}, {})
}
