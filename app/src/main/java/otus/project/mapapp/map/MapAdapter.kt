package otus.project.mapapp.map

import otus.project.mapapp.model.Place
import ru.mail.maps.data.LatLon
import ru.mail.maps.data.MapLocation
import ru.mail.maps.data.ScreenLocation
import ru.mail.maps.sdk.views.MapView

class MapAdapter(val mapView: MapView) {
    private var clickMarker : (id : Long, place : Place) -> Unit = { _, _ -> }
    private var clickEmpty : (place : Place) -> Unit = { _ -> }

    private fun onClickMarker(ids : String, loc : MapLocation) {
        // convert parameters and call clickMarker()
        val id : Long = ids.toLong()
        val place = Place(loc.latitude?.toFloat() ?: 0f, loc.longitude?.toFloat() ?: 0f)
        clickMarker(id, place)
    }

    private fun onClickEmpty(loc : MapLocation) {
        // convert parameters and call clickEmpty()
        val place  = Place(loc.latitude?.toFloat() ?: 0f, loc.longitude?.toFloat() ?: 0f)
        clickEmpty(place)
    }

    fun setListeners(onMarker : (id : Long, place : Place) -> Unit, onEmpty : (place : Place) -> Unit) {
        clickMarker = onMarker
        clickEmpty = onEmpty
        mapView.getMapAsync { map ->
            map.setOnMarkerClickListener({ ids : String, loc : MapLocation -> onClickMarker(ids, loc) })
            map.setOnMapClickListener({ loc : MapLocation, _ : ScreenLocation -> onClickEmpty(loc) })
        }
    }

    fun locate(place : Place) {
        val loc = LatLon(place.latitude.toDouble(), place.longitude.toDouble())
        mapView.getMapAsync { map -> map.flyTo(loc, true) }
    }

    fun addMark(id : Long, place : Place) {
        val ids = id.toString()
        val loc = MapLocation(place.latitude.toDouble(), place.longitude.toDouble())
        //val mark = MarkerEntity(ids, loc, ImageSrcValue())
        //mapView.getMapAsync { map -> map.addMarker(mark) }
    }
}

/*
data class MapStartOptions(
    val center: LatLon, // точка начального расположения карты (учитываются только lat, lon)
    val zoomLevel: Float, // начальное значение уровня масштабирования (zoomLevel)
    val style: MapStyle, // стиль карты, может быть выбран из соответствующего enum или использован свой
    val compassLocationMode: CompassLocationMode, // настройка компаса, может быть выбран из соответствующего enum
    val logoConfig: LogoConfig // конфигурация логотипа
)

data class LogoConfig(
    val logoAlignment: Alignment, // выравнивание логотипа, может быть выбран из четырех возможных вариантов Alignment: BottomRight, BottomLeft, TopRight, TopLeft
    val logoAdditionalPaddings: AdditionalPaddings // дополнительные паддинги, с помощью которого можно задать горизонтальные и вертикальные отступы
)

// Изменить координаты центра карты
// За анимацию изменений отвечает аргумент animated, за длительность - duration, а за кривую движения камеры - cameraCurve.
// Указание длительности анимации и кривой движения камеры опционально
fun flyTo(mapLocation: MapLocation, animated: Boolean, durationMs: Int?, cameraCurve: CameraCurve = CameraCurve.Standard())

// Увеличить карту, step — значение шага, на который будет увеличена карта
fun zoomIn(step: Float = .5f, animated: Boolean = true)

// Уменьшить карту, step — значение шага, на который будет уменьшена карта
fun zoomOut(step: Float = .5f, animated: Boolean = true)

// Выставляет направление карты, bearing лежит в полуинтервале [0, 360)
fun setBearing(bearing: Float, animated: Boolean = true)

// Выставляет уровень масштаба карты, zoom лежит в полуинтервале (0, 20]
fun setZoom(zoom: Float, animated: Boolean = true)

// Добавляет маркер на карту, где
// MarkerEntity — модель маркера,
// id — уникальный для каждого маркера идентификатор,
// coordinates — координаты точки, к которой будет привязан маркер (учитывается только latitude и longitude, остальные null)
// image — одно из значений enum MarkerImage
fun addMarker(marker: MarkerEntity)

// Добавить список маркеров на карту
fun addMarker(markers: List<MarkerEntity>)

// Удалить маркер с указанным идентификатором
fun removeMarker(id: String)

// Удалить все маркеры с карты
fun removeAllMarkers()

// Показать PopUp окно над маркером,
// markerId — идентификатор маркера, к которому будет привязано окно;
// content — HTML-строка, текст которой будет отображен в окне
fun showPopUp(markerId: String, content: String)

// Скрыть PopUp окно для соответствующего идентификатору маркера
fun hidePopUp(markerId: String)

// Добавить метод-callback, который будет вызван, когда пользователь сделает клик на один из маркеров
// id — идентификатор маркера, на который кликнул пользователь,
// location — координаты, которые соответствуют маркеру (учитывается только latitude и longitude, остальные null)
fun setOnMarkerClickListener(onCLickListener: (id: String, location: MapLocation) -> Unit)

// Удалить метод-callback, отвечающий за клик по маркеру
fun removeMarkerClickListener()

// Задать перемещение маркеру с соответствующим id в позицию location.
// animated — необходима ли анимация перемещения маркера duration — длительность анимации перемещения
fun moveMarker(id: String, location: MapLocation, animated: Boolean, duration: Double)

// Добавить коллбэк, который будет вызываться в случае возникновения тех или иных ошибок в сдк.
// Все ошибки являются наследниками класса MapError
fun addOnErrorListener(onErrorListener: (error: MapError) -> Unit)

// Переместить карту, чтобы центр экрана совпадал с параметром center (учитывается только latitude и longitude, остальные null)
fun setCenter(center: MapLocation, animated: Boolean)

// Изменить стиль карты без необходимости переинициализации
fun changeStyle(style: MapStyle)

// Разрешить или запретить управление картой жестами
fun enableDragPan(enable: Boolean)

// Разрешить или запретить изменение масштаба карты и вращение пользователем
fun enableZoomRotate(enable: Boolean)

// Установить метод-коллбэк, который будет вызван при изменении масштаба карты
fun setOnZoomChangedListener(listener: (zoom: Double) -> Unit)

// Удалить метод-коллбэк, отвечающий за изменение масштаба карты
fun removeZoomChangedListener()

// Установить метод-коллбэк, отвечающий за событие клика по карте
fun setOnMapClickListener(onClickListener: (location: MapLocation, screenLocation: ScreenLocation) -> Unit)

// Установить метод-коллбэк, отвечающий за событие длительного клика по карте
fun setOnMapLongClickListener(onClickListener: (location: MapLocation, screenLocation: ScreenLocation) -> Unit)

// Добавить новый слой на карту
fun addLayer(layer: Layer)

// Добавить новый источник данных для карты
fun addMapDataSource(source: MapDataSource)

// Удалить источник данных по id
fun removeSource(sourceId: String)

// Удалить слой с карты по id
fun removeLayer(layerId: String)

// Добавить кластер на карту, где в объекте Cluster содержатся:
// id — идентификатор кластера,
// markers — список объектов маркеров,
// radius — радиус кластера в метрах,
// textColor — цвет текста в hex ("#ff0000"),
// backgroundColor — цвет фона в hex ("#ffffff")
fun addCluster(cluster: Cluster)

// Удалить кластер по идентификатору
fun removeCluster(id: String)
*/

