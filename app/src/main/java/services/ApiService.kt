package services

import common.BASE_URL
import data.AuthState
import data.Banner
import data.UserInformation
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiService {
    @GET("GetBanner.php")
    fun getAllBanner(
        @Query("sellOrRent") sellOrRent: Int,
        @Query("category") category: Int,
        @Query("phoneNumber") phone: String
    ): Single<List<Banner>>

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("phoneNumber") phoneNumber: String,
        @Field("password") password: String
    ): Single<AuthState>

    //برای آپلود یا ویرایش عکس باید از MultipartBody.Part استفاده کنیم
    //RequestBody: برای اینکه مقدار سمت سرور داخل "" ذخیره نشوند
    @Multipart
    @POST("signUp.php")
    fun signUp(
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part?
    ): Single<AuthState>


    @GET("getUserUsingPhone.php")
    fun getUser(
        @Query("phoneNumber") phoneNumber: String
    ): Single<UserInformation>

    @Multipart
    @POST("editUser.php")
    fun editUser(
        @Part("id") id: RequestBody?,
        @Part("phoneNumber") phoneNumber: RequestBody?,
        @Part("username") username: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Single<AuthState>
}

fun createApiServiceInstance(): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    return retrofit.create(ApiService::class.java)
}