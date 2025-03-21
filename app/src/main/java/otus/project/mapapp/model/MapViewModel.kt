package otus.project.mapapp.model

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import otus.project.mapapp.db.MarkerStore
import otus.project.mapapp.map.CheckLocation
import otus.project.mapapp.net.NetClient
import otus.project.mapapp.net.PlaceData

enum class ViewType { TypeAny, TypeList, TypeMap }
enum class ViewMode { ModeView, ModeEdit }
enum class MapStyle { Main, Dark, Light }

class MapViewModel(private val ctx : Context, private val store : MarkerStore) : ViewModel() {

    companion object {
        var currentViewType : ViewType = ViewType.TypeAny
        var currentViewMode : ViewMode = ViewMode.ModeView
        var currentSelected : Long = 0
        var currentPlace : Place = Place()
        var currentCenter : Place = Place(55.75f,37.62f)
        var currentRadius : Int = 5000
        var currentFilter : String = "памятник"
        var currentLimit : Int = 25
        var currentStyle : MapStyle = MapStyle.Main
        var currentZoom : Int = 12
        var resetOnChange : Boolean = true
        var checkLocation : Boolean = false

        val useSourceNet : Boolean = true
        val useSourceDb : Boolean = false

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
        initData()
        var place = mplace.get(objId)
        if (place == null) {
            if (useSourceDb) {
                place = store.getPlace(objId)
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
            store.addItem(item, place)
        }
        return item.id
    }

    private fun dataFromDb() {
        _items.addAll(store.getItems())
        for (item in mitems) {
             _place.put(item.id, store.getPlace(item.id))
        }
        if (mitems.isNotEmpty()) {
            lastId = mitems.maxOf { it.id }
        }
    }

    private fun initData() {
        if (mitems.isEmpty() || resetOnChange == false) {
            if (useSourceDb) {
                dataFromDb()
            }
            if (useSourceNet) {
                val results : List<PlaceData> = NetClient.getDataAsync(currentFilter, currentLimit, currentCenter, currentRadius, ctx)
                for (r in results) {
                    addItem(
                        Item(name = r.name ?: "", details = r.place_details ?: "", address = r.address ?: "", category = r.type ?: ""),
                        Place(r.pin[1], r.pin[0])
                    )
                }
            }
        }
    }

    fun getMapImage(width : Int, height : Int) : Bitmap? {
        initData()
        if (checkLocation) {
            if (CheckLocation.isLocationFound()) {
                currentCenter = CheckLocation.getLocation()
            }
        }
        val url = NetClient.getImageUrl(currentCenter, currentZoom, currentMapStyle(), width, height, mplace, currentSelected)
        return NetClient.getBitmapAsync(url, ctx)
    }
}
