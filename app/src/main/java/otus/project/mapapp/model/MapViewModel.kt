package otus.project.mapapp.model

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import otus.project.common.*
import otus.project.common.net.NetClient
import otus.project.common.net.PlaceData
import otus.project.feature.db.MarkerStore
import otus.project.feature.loc.CheckLocation
import javax.inject.Inject

private fun MapStyle.toString() : String {
    return when (this) {
        MapStyle.Main -> "main"
        MapStyle.Dark -> "dark"
        MapStyle.Light -> "simple"
    }
}

@HiltViewModel
class MapViewModel @Inject constructor (
    @ApplicationContext private val ctx : Context,
    var client : NetClient,
    var store : MarkerStore,
    var check : CheckLocation
) : ViewModel() {

    private val defaultCenter : Place =
        Place(55.75f, 37.62f)

    // текущее состояние
    var currentViewMode : ViewMode = ViewMode.ModeView
    var currentViewType : ViewType = ViewType.TypeAny
    // выбор из списка / на карте
    var currentSelected : Long = 0
    var currentPlace : Place = Place()
    // параметры запроса
    val query : NetQuery = NetQuery(defaultCenter, 5000, "monument", 20, MapStyle.Main, 12)
    // настройки приложения
    val state : AppSetup = AppSetup(true, false, true, false, false)

    private var lastId : Long = 0
    private fun newId() : Long = ++lastId

    private var _place : MutableMap<Long, Place> = mutableMapOf()
    private val mplace : Map<Long, Place> = _place

    private var _items : MutableList<Item> = mutableListOf()
    private val mitems : List<Item> = _items

    fun clearData() {
        _place.clear()
        _items.clear()
        lastId = 0
        currentSelected = 0
    }

    fun getItem(objId : Long) : Item {
        //initData()
        return mitems.find { it.id == objId } ?: Item()
    }

    fun getItems() : List<Item> {
        initData()
        return mitems
    }

    fun getPlace(objId : Long) : Place {
        //initData()
        var place = mplace.get(objId)
        if (place == null) {
            var err : String? = null
            if (state.useSourceDb) {
                viewModelScope.launch(Dispatchers.IO) {
                    place = store.getPlace(objId) { err = it }
                }
            }
            err?.let { Toast.makeText(ctx, err, Toast.LENGTH_LONG).show() }
        }
        return place ?: Place()
    }

    fun findIdByPlace(place : Place) : Long {
        val near = 0.00001f
        var id : Long = 0
        for (p in mplace) {
            if (p.key > 0 &&
                Math.abs(p.value.latitude - place.latitude) < near &&
                Math.abs(p.value.longitude - place.longitude) < near) {
                id = p.key
                break
            }
        }
        return id
    }

    fun addItem(item : Item, place : Place) : Long {
        item.id = newId()
        _items.add(item)
        _place.put(item.id, place)
        var err : String? = null
        if (state.useSourceDb) {
            viewModelScope.launch(Dispatchers.IO) {
                store.addItem(item, place) { err = it }
            }
        }
        err?.let { Toast.makeText(ctx, err, Toast.LENGTH_LONG).show() }
        return item.id
    }

    private fun initData() {
        if (mitems.isEmpty() || !state.resetOnChange) {
            if (state.useSourceDb) {
                dataFromDb()
            }
            if (state.useSourceNet) {
                dataFromNet()
            }
        }
    }

    private fun dataFromDb() {
        var err : String? = null
        viewModelScope.launch(Dispatchers.IO) {
            _items.addAll(store.getItems { err = it })
            for (item in mitems) {
                _place.put(item.id, store.getPlace(item.id) { err = it })
            }
        }
        err?.let { Toast.makeText(ctx, err, Toast.LENGTH_LONG).show() }
        if (mitems.isNotEmpty()) {
            lastId = mitems.maxOf { it.id }
        }
    }

    private fun dataFromNet() {
        var err : String? = null
        runBlocking(Dispatchers.IO) {
            val data: Deferred<List<PlaceData>> = async {
                client.getDataAsync(
                        filter = query.filter,
                        count = query.limit,
                        center = query.center,
                        radius = query.radius,
                ) { err = it }
            }
            val results = data.await()
            for (r in results) {
                val item = Item(
                    id = newId(),
                    name = r.name ?: "",
                    details = r.place_details ?: "",
                    address = r.address ?: "",
                    category = r.type ?: ""
                )
                val place = Place(r.pin[1], r.pin[0])
                _items.add(item)
                _place.put(item.id, place)
                if (state.useSourceDb) {
                    store.addItem(item, place) { err = it }
                }
            }
        }
        err?.let { Toast.makeText(ctx, err, Toast.LENGTH_LONG).show() }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getMapImage(width : Int, height : Int) : Bitmap? {
        initData()
        if (state.checkLocation) {
            if (check.isLocationFound()) {
                query.center = check.getLocation() ?: defaultCenter
            }
        }
        val url = client.getImageUrl(
                center = query.center,
                zoom = query.zoom,
                style = query.style.toString(),
                width = width,
                height = height,
                pins = mplace,
                selected = currentSelected
        )
        var err : String? = null
        var bmp : Bitmap? = null
        runBlocking(Dispatchers.IO) {
            launch {
                bmp = client.getBitmapAsync(url) { err = it }
            }
        }
        err?.let { Toast.makeText(ctx, err, Toast.LENGTH_LONG).show() }
        return bmp
    }
}
