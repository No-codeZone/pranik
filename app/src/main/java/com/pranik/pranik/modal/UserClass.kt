package com.pranik.pranik

import com.google.gson.annotations.SerializedName

data class UserClass(
    @SerializedName("ReqStatus" ) var ReqStatus: ReqStatus = ReqStatus(),
    @SerializedName("Result"    ) var Result: Result?    = Result()
)
data class ReqStatus (

    @SerializedName("Status"  ) var Status  : Boolean? = null,
    @SerializedName("Message" ) var Message : String?  = null

)
data class Result (
    @SerializedName("access_token" ) var accessToken : String? = null,
    @SerializedName("token_type"   ) var tokenType   : String? = null,
    @SerializedName("expires_in"   ) var expiresIn   : Int?    = null,
    @SerializedName("userName"     ) var userName    : String? = null,
    @SerializedName("RoleName"     ) var roleName    : String? = null,
    @SerializedName(".issued"      ) var issued     : String? = null,
    @SerializedName(".expires"     ) var expires    : String? = null
)
