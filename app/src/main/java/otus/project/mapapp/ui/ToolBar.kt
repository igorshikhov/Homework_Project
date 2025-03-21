package otus.project.mapapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import otus.project.mapapp.R

// какие кнопки рисовать
data class ToolBarTune (
    val bBack : Boolean,
    val bMinus: Boolean,
    val bPlus : Boolean,
    val bSave : Boolean,
    val bMode : Boolean,
    val bList : Boolean,
    val bMap  : Boolean,
)

// заготовки для всех окон
object toolbar {
    val Start = ToolBarTune(false, false, false, false, true,  true,  true)
    val Map   = ToolBarTune(true,  true,  true,  false, true,  true,  false)
    val List  = ToolBarTune(true,  false, false, false, true,  false, true)
    val Info  = ToolBarTune(true,  false, false, false, false, false, true)
    val Item  = ToolBarTune(true,  false, false, true,  false, false, false)
    val Mode  = ToolBarTune(true,  false, false, true,  false, false, false)
}

@Composable
fun ToolBar(tune : ToolBarTune,
            back : () -> Unit = {},
            save : () -> Unit = {},
            toMap : () -> Unit = {},
            toList : () -> Unit = {},
            toSetup : () -> Unit = {},
            zoomIn : () -> Unit = {},
            zoomOut : () -> Unit = {},
            modifier : Modifier = Modifier
                //.background(color = colorResource(id = R.color.dark_green))
                .padding(4.dp, 0.dp)) {
    val bColors = ButtonColors(
        containerColor = colorResource(id = R.color.dark_green),
        contentColor = colorResource(id = R.color.white),
        disabledContainerColor = colorResource(id = R.color.dark_gray),
        disabledContentColor = colorResource(id = R.color.light_gray))

    Row(horizontalArrangement = Arrangement.Start,
        modifier = modifier.fillMaxWidth(1f)) {
        if (tune.bBack) {
            Button(onClick = { back() },
                modifier = modifier,
                colors = bColors,
                contentPadding = PaddingValues(8.dp)) {
                Text(" < ", fontSize = 18.sp)
            }
        }
        if (tune.bMinus) {
            Button(onClick = { zoomOut() },
                modifier = modifier,
                colors = bColors,
                contentPadding = PaddingValues(8.dp)) {
                Text(" - ", fontSize = 18.sp)
            }
        }
        if (tune.bPlus) {
            Button(onClick = { zoomIn() },
                modifier = modifier,
                colors = bColors,
                contentPadding = PaddingValues(8.dp)) {
                Text(" + ", fontSize = 18.sp)
            }
        }
        if (tune.bSave) {
            Button(onClick = { save(); back() },
                modifier = modifier,
                colors = bColors,
                contentPadding = PaddingValues(8.dp)) {
                Text(stringResource(id = R.string.save_data), fontSize = 18.sp)
            }
        }
        if (tune.bList) {
            Button(onClick = { toList() },
                modifier = modifier,
                colors = bColors,
                contentPadding = PaddingValues(8.dp)) {
                Text(stringResource(id = R.string.item_list), fontSize = 18.sp)
            }
        }
        if (tune.bMap) {
            Button(onClick = { toMap() },
                modifier = modifier,
                colors = bColors,
                contentPadding = PaddingValues(8.dp)) {
                Text(stringResource(id = R.string.map_view), fontSize = 18.sp)
            }
        }
        if (tune.bMode) {
            Button(onClick = { toSetup() },
                modifier = modifier,
                colors = bColors,
                contentPadding = PaddingValues(8.dp)) {
                Text(stringResource(id = R.string.settings), fontSize = 18.sp)
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun ToolBarPreview() {
    Surface {
        ToolBar(toolbar.Map)
    }
}

