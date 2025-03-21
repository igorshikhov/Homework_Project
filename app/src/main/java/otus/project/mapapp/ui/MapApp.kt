package otus.project.mapapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import otus.project.mapapp.R
import otus.project.mapapp.model.Item
import otus.project.mapapp.model.MapViewModel
import otus.project.mapapp.model.ViewMode
import otus.project.mapapp.model.ViewType

// Screen's Labels
data object Preface  { val id = "Preface" }
data object MapMode  { val id = "MapMode" }
data object ItemMap  { val id = "MapView" }
data object ItemEdit { val id = "ItemEdit" }
data object ItemList { val id = "ItemList" }
data object ItemInfo { val id = "ItemInfo" }

@Composable
fun MapApp(viewModel: MapViewModel, setTitle : (String) -> Unit, finish : () -> Unit) {
    val navController : NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Preface.id) {
        composable(Preface.id) {
            setTitle(stringResource(R.string.app_name))
            ViewStart(
                { finish() },
                { navController.navigate(if (it == ViewType.TypeList) ItemList.id else ItemMap.id) },
                { navController.navigate(MapMode.id) }
            )	// done, view, mode
        }
        composable(MapMode.id) {
            setTitle(stringResource(R.string.settings))
            ViewMode(
                { navController.navigate(if (it == ViewType.TypeList) ItemList.id else if (it == ViewType.TypeMap) ItemMap.id else Preface.id) },
                { viewModel.clearData() }
            )  // back, clear
        }
        composable(ItemMap.id) {
            setTitle(stringResource(R.string.map_view) + " (${MapViewModel.currentFilter})")
            ViewMap(
                { w, h -> viewModel.getMapImage(w, h) },
                { navController.navigate(Preface.id) },
                { navController.navigate(ItemList.id) },
                { navController.navigate(MapMode.id) },
                { MapViewModel.currentPlace = it
                  MapViewModel.currentSelected = viewModel.findIdByPlace(it)
                  navController.navigate(if (MapViewModel.currentViewMode == ViewMode.ModeView) ItemInfo.id else ItemEdit.id)
                } /*,
                { w, h -> viewModel.imageToView(activity.findViewById(R.id.imageView), w, h) }*/
            )	// back, view (list), refresh, info/edit
        }
        composable(ItemList.id) {
            setTitle(stringResource(R.string.item_list) + " (${MapViewModel.currentFilter})")
            ViewList(
                { viewModel.getItems() },
                { navController.navigate(Preface.id) },
                { navController.navigate(ItemMap.id) },
                { navController.navigate(MapMode.id) },
                { MapViewModel.currentSelected = it
                  MapViewModel.currentPlace = viewModel.getPlace(it)
                  navController.navigate(ItemInfo.id)
                }
            )	// back, view (map), info
        }
        composable(ItemInfo.id) {
            setTitle(stringResource(R.string.view_mode))
            ViewInfo(
                viewModel.getItem(MapViewModel.currentSelected),
                { navController.navigate(if (it == ViewType.TypeList) ItemList.id else ItemMap.id) }
            )	// back, view (map)
        }
        composable(ItemEdit.id) {
            setTitle(stringResource(R.string.edit_mode))
            ViewEdit(
                { navController.navigate(ItemMap.id) },
                { item : Item -> viewModel.addItem(item, MapViewModel.currentPlace) }
            )	// back, save
        }
    }
}
