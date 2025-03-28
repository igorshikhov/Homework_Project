package otus.project.mapapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import otus.project.mapapp.R
import otus.project.mapapp.model.MapViewModel
import otus.project.mapapp.model.ViewType

@Composable
fun StartScreen(back : () -> Unit, turnView : (type : ViewType) -> Unit, turnMode : () -> Unit) {
    MapViewModel.currentViewType = ViewType.TypeAny
    Surface {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(1f)
        ) {
            TitleBar(stringResource(R.string.app_name))

            Spacer(modifier = Modifier.weight(0.5f))

            AnimatedVisibility(visible = true,
                enter = scaleIn(animationSpec = repeatable(iterations = 3,
                    animation = tween(5000, easing = LinearOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse))) {
                Box(contentAlignment  = Alignment.Center,
                    modifier = Modifier.fillMaxWidth(1f)) {
                    Image(painter = painterResource(R.drawable.globe) , contentDescription = "")
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            ToolBar(
                tune = toolbar.Start,
                back = back,
                toMap = { turnView(ViewType.TypeMap) },
                toList = { turnView(ViewType.TypeList) },
                toSetup = turnMode
            )
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun StartScreenPreview() {
    StartScreen({}, {}, {})
}
