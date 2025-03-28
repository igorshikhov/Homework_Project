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
import otus.project.mapapp.db.MarkerStore
import otus.project.mapapp.loc.CheckLocation
import otus.project.mapapp.net.NetClient
import otus.project.mapapp.net.PlaceData
import javax.inject.Inject

enum class ViewType { TypeAny, TypeList, TypeMap }
enum class ViewMode { ModeView, ModeEdit }
enum class MapStyle { Main, Dark, Light }

@HiltViewModel
class MapViewModel @Inject constructor (@ApplicationContext private val ctx : Context) : ViewModel() {

    @Inject lateinit var store : MarkerStore
    @Inject lateinit var check : CheckLocation

    companion object {
        val defaultCenter : Place = Place(55.75f,37.62f)
        val useSourceNet : Boolean = true
        var useSourceDb : Boolean = false
        var resetOnChange : Boolean = true
        var checkLocation : Boolean = false
        var locationEnabled : Boolean = false
        // текущее состояние
        var currentViewMode : ViewMode = ViewMode.ModeView
        var currentViewType : ViewType = ViewType.TypeAny
        // выбор из списка / на карте
        var currentSelected : Long = 0
        var currentPlace : Place = Place()
        // параметры запроса данных
        var currentCenter : Place = defaultCenter
        var currentRadius : Int = 5000
        var currentFilter : String = "monument"
        var currentLimit : Int = 20
        // параметры карты
        var currentStyle : MapStyle = MapStyle.Main
        var currentZoom : Int = 12

        private fun currentMapStyle() : String {
            return when (currentStyle) {
                MapStyle.Main -> "main"
                MapStyle.Dark -> "dark"
                MapStyle.Light -> "simple"
            }
        }

        private var lastId : Long = 0

        private fun newId() : Long = ++lastId
    }

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
            if (useSourceDb) {
                viewModelScope.launch(Dispatchers.IO) {
                    place = store.getPlace(objId)
                }
            }
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
        if (useSourceDb) {
            viewModelScope.launch(Dispatchers.IO) {
                store.addItem(item, place)
            }
        }
        return item.id
    }

    private fun initData() {
        if (mitems.isEmpty() || resetOnChange == false) {
            if (useSourceDb) {
                dataFromDb()
            }
            if (useSourceNet) {
                dataFromNet()
            }
        }
    }

    private fun dataFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            _items.addAll(store.getItems())
            for (item in mitems) {
                _place.put(item.id, store.getPlace(item.id))
            }
        }
        if (mitems.isNotEmpty()) {
            lastId = mitems.maxOf { it.id }
        }
    }

    private fun dataFromNet() {
        var err : String? = null
        runBlocking(Dispatchers.IO) {
            val data: Deferred<List<PlaceData>> = async {
                NetClient.getDataAsync(
                    currentFilter,
                    currentLimit,
                    currentCenter,
                    currentRadius,
                    { err = it }
                )
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
                if (useSourceDb) {
                    store.addItem(item, place)
                }
            }
        }
        err?.let { Toast.makeText(ctx, err, Toast.LENGTH_LONG).show() }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getMapImage(width : Int, height : Int) : Bitmap? {
        initData()
        if (checkLocation) {
            if (check.isLocationFound()) {
                currentCenter = check.getLocation() ?: defaultCenter
            }
        }
        val url = NetClient.getImageUrl(
            currentCenter,
            currentZoom,
            currentMapStyle(),
            width,
            height,
            mplace,
            currentSelected
        )
        var err : String? = null
        var bmp : Bitmap? = null
        runBlocking(Dispatchers.IO) {
            launch {
                bmp = NetClient.getBitmapAsync(url, { err = it })
            }
        }
        err?.let { Toast.makeText(ctx, err, Toast.LENGTH_LONG).show() }
        return bmp
    }
}
