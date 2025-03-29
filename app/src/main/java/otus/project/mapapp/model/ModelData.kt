package otus.project.mapapp.model

data class Item (
    var id : Long = 0,
    var name : String = "",
    var details : String = "",
    var address : String = "",
    var category : String = "",
    var status : String = "",
)

data class Place (
    var latitude : Float = 0f,
    var longitude : Float = 0f,
)

enum class ViewType { TypeAny, TypeList, TypeMap }
enum class ViewMode { ModeView, ModeEdit }
enum class MapStyle { Main, Dark, Light }

data class CurrentQuery (
    // параметры запроса данных
    var center : Place,
    var radius : Int,
    var filter : String,
    var limit : Int,
    // параметры карты
    var style : MapStyle,
    var zoom : Int
)

data class AppSettings (
    var useSourceNet : Boolean,
    var useSourceDb : Boolean,
    var resetOnChange : Boolean,
    var checkLocation : Boolean,
    var locationEnabled : Boolean
)