package com.pranik.pranik.modal
import com.google.gson.annotations.SerializedName
data class RequestBodySaveUpdateCoordinates (

    @SerializedName("Id"        ) var Id        : String? = null,
    @SerializedName("Latitude"  ) var Latitude  : String? = null,
    @SerializedName("Longitude" ) var Longitude : String? = null,
    @SerializedName("Range"     ) var Range     : String? = null,
    @SerializedName("IsActive"  ) var IsActive  : String? = null,
    @SerializedName("Title"     ) var Title     : String? = null

)