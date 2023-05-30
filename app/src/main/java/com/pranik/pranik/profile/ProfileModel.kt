package com.pranik.pranik.profile
import com.google.gson.annotations.SerializedName

class ProfileModel (
    @SerializedName("ReqStatus" ) var ReqStatus : ReqStatus? = ReqStatus(),
    @SerializedName("Result"    ) var Result    : Result?    = Result()
        )
data class ReqStatus (

    @SerializedName("Status"  ) var Status  : Boolean? = null,
    @SerializedName("Message" ) var Message : String?  = null
)
data class Roles (

    @SerializedName("UserId" ) var UserId : String? = null,
    @SerializedName("RoleId" ) var RoleId : String? = null

)
data class Result (

    @SerializedName("Claims"               ) var Claims               : ArrayList<String> = arrayListOf(),
    @SerializedName("Logins"               ) var Logins               : ArrayList<String> = arrayListOf(),
    @SerializedName("Roles"                ) var Roles                : ArrayList<Roles>  = arrayListOf(),
    @SerializedName("FullName"             ) var FullName             : String?           = null,
    @SerializedName("IsActive"             ) var IsActive             : Boolean?          = null,
    @SerializedName("CreatedDateUtc"       ) var CreatedDateUtc       : String?           = null,
    @SerializedName("IPAddress"            ) var IPAddress            : String?           = null,
    @SerializedName("ImageUrl"             ) var ImageUrl             : String?           = null,
    @SerializedName("Email"                ) var Email                : String?           = null,
    @SerializedName("EmailConfirmed"       ) var EmailConfirmed       : Boolean?          = null,
    @SerializedName("PasswordHash"         ) var PasswordHash         : String?           = null,
    @SerializedName("SecurityStamp"        ) var SecurityStamp        : String?           = null,
    @SerializedName("PhoneNumber"          ) var PhoneNumber          : String?           = null,
    @SerializedName("PhoneNumberConfirmed" ) var PhoneNumberConfirmed : Boolean?          = null,
    @SerializedName("TwoFactorEnabled"     ) var TwoFactorEnabled     : Boolean?          = null,
    @SerializedName("LockoutEndDateUtc"    ) var LockoutEndDateUtc    : String?           = null,
    @SerializedName("LockoutEnabled"       ) var LockoutEnabled       : Boolean?          = null,
    @SerializedName("Admin"                ) var RoleName             : String?          = null,
    @SerializedName("AccessFailedCount"    ) var AccessFailedCount    : Int?              = null,
    @SerializedName("Id"                   ) var Id                   : String?           = null,
    @SerializedName("UserName"             ) var UserName             : String?           = null
)