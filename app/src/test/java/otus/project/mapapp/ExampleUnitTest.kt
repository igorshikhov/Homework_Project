package otus.project.mapapp

import android.content.Context
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

import otus.project.common.*
import otus.project.common.net.NetClient
import otus.project.common.net.PlaceData
import otus.project.feature.db.DatabaseModule
import otus.project.feature.db.MarkerStore
import otus.project.mapapp.model.MapViewModel

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

    @Mock
    private lateinit var ctx : Context

    @Before
    fun init() { ctx = mock<Context>() }

    @Test
    fun testViewModel() {
        val client = NetClient()
        val store = MarkerStore(DatabaseModule.provideMarkerDao(DatabaseModule.provideMarkerDatabase(ctx)))
        val check = otus.project.feature.loc.CheckLocation(ctx)
        val viewModel = MapViewModel(ctx, client, store, check)
        val item = Item(1, "объект 1", "данные 1", "адрес 1")
        val place = Place(55.75f, 37.62f)
        viewModel.addItem(item, place)
        val ilist : List<Item> = viewModel.getItems()
        assertEquals(1, ilist.size)
    }

    @Test
    fun testViewModelItem() {
        val client = NetClient()
        val store = MarkerStore(DatabaseModule.provideMarkerDao(DatabaseModule.provideMarkerDatabase(ctx)))
        val check = otus.project.feature.loc.CheckLocation(ctx)
        val viewModel = MapViewModel(ctx, client, store, check)
        val item = Item(1, "объект 1", "данные 1", "адрес 1")
        val place = Place(55.75f, 37.62f)
        viewModel.addItem(item, place)
        val found : Item = viewModel.getItem(1)
        assertEquals(1, found.id)
    }

    @Test
    fun testViewModelPlace() {
        val client = NetClient()
        val store = MarkerStore(DatabaseModule.provideMarkerDao(DatabaseModule.provideMarkerDatabase(ctx)))
        val check = otus.project.feature.loc.CheckLocation(ctx)
        val viewModel = MapViewModel(ctx, client, store, check)
        val item = Item(1, "объект 1", "данные 1", "адрес 1")
        val place = Place(55.75f, 37.62f)
        viewModel.addItem(item, place)
        val found : Place = viewModel.getPlace(1)
        assertEquals(place.latitude, found.latitude)
        assertEquals(place.longitude, found.longitude)
    }

    @Test
    fun testViewModelFind() {
        val client = NetClient()
        val store = MarkerStore(DatabaseModule.provideMarkerDao(DatabaseModule.provideMarkerDatabase(ctx)))
        val check = otus.project.feature.loc.CheckLocation(ctx)
        val viewModel = MapViewModel(ctx, client, store, check)
        val item = Item(1, "объект 1", "данные 1", "адрес 1")
        val place = Place(55.75f, 37.62f)
        viewModel.addItem(item, place)
        val id = viewModel.findIdByPlace(place)
        assertEquals(1, id)
    }
}