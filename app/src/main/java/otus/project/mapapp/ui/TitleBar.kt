package otus.project.mapapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import otus.project.mapapp.R

@Composable
fun TitleBar(title : String) {
    val space = dimensionResource(id = R.dimen.side_margin)
    Row(horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth(1f)
            .background(color = colorResource(id = R.color.medium_green))
    ) {
        Text(text = title,
            fontSize = 18.sp,
            color = colorResource(id = R.color.white),
            modifier = Modifier.padding(space)
        )
    }
}

@Preview(apiLevel = 34)
@Composable
fun TitleBarPreview() {
    TitleBar(title = "Title")
}
