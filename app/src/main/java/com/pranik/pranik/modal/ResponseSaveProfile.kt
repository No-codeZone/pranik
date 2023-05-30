package com.pranik.pranik.modal

import com.google.gson.annotations.SerializedName

data class ResponseSaveProfile(
    @SerializedName("ReqStatus" ) var ReqStatus : ReqStatus? = ReqStatus(),
    @SerializedName("Result"    ) var Result    : String?    = null
)
