package otus.project.mapapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import otus.project.common.Item
import otus.project.mapapp.model.MapViewModel
import otus.project.common.ViewMode
import otus.project.common.ViewType

// Screen Labels
data object Preface  { val id = "Preface" }
data object MapMode  { val id = "MapMode" }
data object ItemMap  { val id = "MapView" }
data object ItemEdit { val id = "ItemEdit" }
data object ItemList { val id = "ItemList" }
data object ItemInfo { val id = "ItemInfo" }

@Composable
fun MapApp(viewModel: MapViewModel, finish : () -> Unit) {
    val navController : NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Preface.id) {
        composable(Preface.id) {
            StartScreen(
                model = viewModel,
                back = { finish() },
                turnView = { navController.navigate(if (it == ViewType.TypeList) ItemList.id else ItemMap.id) },
                turnMode = { navController.navigate(MapMode.id) }
            )	// done, view, mode
        }
        composable(MapMode.id) {
            SetupScreen(
                model = viewModel,
                back = { navController.navigate(if (it == ViewType.TypeList) ItemList.id else if (it == ViewType.TypeMap) ItemMap.id else Preface.id) }
            )  // back, clear
        }
        composable(ItemMap.id) {
            MapScreen(
                model = viewModel,
                back = { navController.navigate(Preface.id) },
                toList = { navController.navigate(ItemList.id) },
                toSetup = { navController.navigate(MapMode.id) },
                onClick = { viewModel.currentPlace = it
                  viewModel.currentSelected = viewModel.findIdByPlace(it)
                  navController.navigate(if (viewModel.currentViewMode == ViewMode.ModeView) ItemInfo.id else ItemEdit.id)
                } /*,
                { w, h -> viewModel.imageToView(activity.findViewById(R.id.imageView), w, h) }*/
            )	// back, view (list), refresh, info/edit
        }
        composable(ItemList.id) {
            ListScreen(
                model = viewModel,
                back = { navController.navigate(Preface.id) },
                toMap = { navController.navigate(ItemMap.id) },
                toSetup = { navController.navigate(MapMode.id) },
                onClick = { viewModel.currentSelected = it
                  viewModel.currentPlace = viewModel.getPlace(it)
                  navController.navigate(ItemInfo.id)
                }
            )	// back, view (map), info
        }
        composable(ItemInfo.id) {
            ItemInfoScreen(
                model = viewModel,
                back = { navController.navigate(if (it == ViewType.TypeList) ItemList.id else ItemMap.id) }
            )	// back, view (map)
        }
        composable(ItemEdit.id) {
            ItemEditScreen(
                back = { navController.navigate(ItemMap.id) },
                save = { item : Item -> viewModel.addItem(item, viewModel.currentPlace) }
            )	// back, save
        }
    }
}
