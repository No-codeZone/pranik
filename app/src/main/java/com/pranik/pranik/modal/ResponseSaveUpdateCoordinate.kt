package com.pranik.pranik.modal

import com.google.gson.annotations.SerializedName


data class ResponseSaveUpdateCoordinate (

    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String? = null

)