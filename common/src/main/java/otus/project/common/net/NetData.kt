package otus.project.common.net

import com.google.gson.annotations.SerializedName

data class PlaceData(
    @SerializedName("address") val address : String?,
    @SerializedName("name") val name : String?,
    @SerializedName("pin") val pin : List<Float>,
    @SerializedName("place_details") val place_details : String?,
    @SerializedName("type") val type : String?,
)

data class PlaceResponse(
    @SerializedName("request") val request : String?,
    @SerializedName("results") val results : List<PlaceData>
)
