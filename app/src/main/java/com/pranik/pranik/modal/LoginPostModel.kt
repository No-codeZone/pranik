package com.pranik.pranik.modal

import com.google.gson.annotations.SerializedName

data class LoginPostModel(
    @SerializedName("UserName") var UserName : String? = null,
    @SerializedName("Password") var Password : String? = null
)