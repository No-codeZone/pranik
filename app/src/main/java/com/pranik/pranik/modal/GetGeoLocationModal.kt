package com.pranik.pranik.modal

import com.google.gson.annotations.SerializedName


data class GetGeoLocationModal (

    @SerializedName("ReqStatus" ) var ReqStatus : ReqStatus?        = ReqStatus(),
    @SerializedName("Result"    ) var Result    : ArrayList<Result> = arrayListOf()

)

data class ReqStatus (

    @SerializedName("Status"  ) var Status  : Boolean? = null,
    @SerializedName("Message" ) var Message : String?  = null

)


data class Result (

    @SerializedName("Id"        ) var Id        : Int?     = null,
    @SerializedName("Latitude"  ) var Latitude  : Double?  = null,
    @SerializedName("Longitude" ) var Longitude : Double?  = null,
    @SerializedName("Range"     ) var Range     : Double?     = null,
    @SerializedName("IsActive"  ) var IsActive  : Boolean? = null,
    @SerializedName("Title"     ) var Title     : String?  = null

)