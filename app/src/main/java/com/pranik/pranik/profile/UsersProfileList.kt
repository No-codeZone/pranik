package com.pranik.pranik.profile

import com.google.gson.annotations.SerializedName

data class UsersProfileList (

    @SerializedName("ReqStatus" ) var ReqStatus : ReqStatus?        = ReqStatus(),
    @SerializedName("Result"    ) var Result    : ArrayList<Result> = arrayListOf()
)
data class ReqStatusUsersList (

    @SerializedName("Status"  ) var Status  : Boolean? = null,
    @SerializedName("Message" ) var Message : String?  = null

)
data class ResultUsersList (

    @SerializedName("Id"        ) var Id        : Int?     = null,
    @SerializedName("Latitude"  ) var Latitude  : Double?  = null,
    @SerializedName("Longitude" ) var Longitude : Double?  = null,
    @SerializedName("Range"     ) var Range     : Int?     = null,
    @SerializedName("IsActive"  ) var IsActive  : Boolean? = null,
    @SerializedName("Title"     ) var Title     : String?  = null

)
