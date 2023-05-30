package com.pranik.pranik.modal

import com.google.gson.annotations.SerializedName


data class RequestSaveProfile (

    @SerializedName("FullName"             ) var FullName             : String?  = null,
    @SerializedName("IsActive"             ) var IsActive             : Boolean? = null,
    @SerializedName("CreatedDateUtc"       ) var CreatedDateUtc       : String?  = null,
    @SerializedName("IPAddress"            ) var IPAddress            : String?  = null,
    @SerializedName("ImageUrl"             ) var ImageUrl             : String?  = null,
    @SerializedName("Email"                ) var Email                : String?  = null,
    @SerializedName("EmailConfirmed"       ) var EmailConfirmed       : Boolean? = null,
    @SerializedName("PasswordHash"         ) var PasswordHash         : String?  = null,
    @SerializedName("SecurityStamp"        ) var SecurityStamp        : String?  = null,
    @SerializedName("PhoneNumber"          ) var PhoneNumber          : String?  = null,
    @SerializedName("PhoneNumberConfirmed" ) var PhoneNumberConfirmed : Boolean? = null,
    @SerializedName("TwoFactorEnabled"     ) var TwoFactorEnabled     : Boolean? = null,
    @SerializedName("LockoutEndDateUtc"    ) var LockoutEndDateUtc    : String?  = null,
    @SerializedName("LockoutEnabled"       ) var LockoutEnabled       : Boolean? = null,
    @SerializedName("AccessFailedCount"    ) var AccessFailedCount    : Int?     = null,
    @SerializedName("Id"                   ) var Id                   : String?  = null,
    @SerializedName("UserName"             ) var UserName             : String?  = null

)