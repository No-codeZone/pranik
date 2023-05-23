package com.pranik.pranik

import com.pranik.pranik.modal.*
import com.pranik.pranik.profile.ProfileModel
import com.pranik.pranik.profile.UserProfileModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {
    @Headers("Content-Type:application/json")
//    @POST("auth-tokens")
//    fun login(@Body info: LoginPostModel):retrofit2.Call<UserClass>
    @POST("/api/Account/Login")
    // on below line we are creating a method to post our data.
    fun login(@Body signInInfo: LoginPostModel?): Call<UserClass?>?

    @GET("/api/Account/GetMyProfile")
    fun getProfile (@Header("Authorization") token: String): Call<ProfileModel>

    @GET("/api/Settings/GetGeoCoordinateList")
    fun getGeoLocation(@Header("Authorization") token: String) : Call<GetGeoLocationModal>

    @POST("/Api/Settings/SaveUpdateCoordinates")
    fun dialogMsg(@Header("Authorization") token: String,
                  @Body saveUpdateCoordinatesModel: RequestBodySaveUpdateCoordinates) :
            Call<ResponseSaveUpdateCoordinate>

    @POST("/api/Account/SaveProfile")
    fun updateProfile(@Header("Authorization") token: String,
                      @Body saveProfile: RequestSaveProfile) : Call<ResponseSaveProfile>

    @GET("/api/Account/GetMyProfile")
    fun getUserProfile(@Header("Authorization") token: String) : Call<UserProfileModel>
}
class RetrofitInstance{
    companion object{
        val BaseURL: String="http://pranik.techstern.com"
        val interceptor:HttpLoggingInterceptor=HttpLoggingInterceptor().apply {
            this.level=HttpLoggingInterceptor.Level.BODY
        }
        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BaseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}