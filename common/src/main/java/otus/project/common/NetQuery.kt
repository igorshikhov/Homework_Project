package otus.project.common

enum class MapStyle { Main, Dark, Light }

data class NetQuery (
    // параметры запроса данных
    var center : Place,
    var radius : Int,
    var filter : String,
    var limit : Int,
    // параметры карты
    var style : MapStyle,
    var zoom : Int
)

