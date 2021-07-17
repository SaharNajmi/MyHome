package services

import com.google.gson.JsonObject
import data.Banner
import data.Login
import data.MSG
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiService {
    @GET("GetBanner.php")
    fun getAllBanner(
        @Query("sellOrRent") sellOrRent: Int,
        @Query("category") category: Int
    ): Single<List<Banner>>

    /*@POST("login.php")
    fun login(@Body jsonObject: JsonObject): Single<Login>*/
    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("phoneNumber") phoneNumber: String,
        @Field("password") password: String
    ): Single<Login>

    @POST("register.php")
    fun signUp(@Body jsonObject: JsonObject): Single<MSG>
}

fun createApiServiceInstance(): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.102/myhome/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    return retrofit.create(ApiService::class.java)
}