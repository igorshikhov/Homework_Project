package otus.project.common

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
