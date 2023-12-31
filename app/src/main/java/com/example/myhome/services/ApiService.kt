package com.example.myhome.services

import com.example.myhome.common.Constants.BASE_URL
import com.example.myhome.data.model.Banner
import com.example.myhome.data.model.State
import com.example.myhome.data.model.User
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiService {
    @GET("getBanner.php")
    fun getBanners(
        @Query("sellOrRent") sellOrRent: Int,
        @Query("category") category: Int,
        @Query("phoneNumber") phone: String,
        @Query("price") price: String,
        @Query("homeSize") homeSize: Int,
        @Query("numberOfRooms") numberOfRooms: Int
    ): Single<List<Banner>>

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("phoneNumber") phoneNumber: String,
        @Field("password") password: String
    ): Single<State>

    @Multipart
    @POST("signUp.php")
    fun signUp(
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part?
    ): Single<State>


    @GET("getUserUsingPhone.php")
    fun getUser(
        @Query("phoneNumber") phoneNumber: String
    ): Single<User>

    @Multipart
    @POST("editUser.php")
    fun editUser(
        @Part("id") id: RequestBody?,
        @Part("phoneNumber") phoneNumber: RequestBody?,
        @Part("username") username: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Single<State>

    @GET("deleteBanner.php")
    fun deleteBanner(
        @Query("id") id: Int
    ): Single<State>

    @Multipart
    @POST("editBanner.php")
    fun editBanner(
        @Part("id") id: Int,
        @Part("userID") userID: Int,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("location") location: RequestBody,
        @Part("category") category: Int,
        @Part("sellOrRent") sellOrRent: Int,
        @Part("homeSize") homeSize: Int,
        @Part("numberOfRooms") numberOfRooms: Int,
        @Part image: MultipartBody.Part?
    ): Single<State>

    @Multipart
    @POST("addBanner.php")
    fun addBanner(
        @Part("userID") userID: Int,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("location") location: RequestBody,
        @Part("category") category: Int,
        @Part("sellOrRent") sellOrRent: Int,
        @Part("homeSize") homeSize: Int,
        @Part("numberOfRooms") numberOfRooms: Int,
        @Part image: MultipartBody.Part?
    ): Single<State>
}

fun createApiServiceInstance(): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    return retrofit.create(ApiService::class.java)
}