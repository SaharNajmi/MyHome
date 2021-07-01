package data.repository.source

import com.google.gson.JsonObject
import data.Login
import data.MSG
import io.reactivex.Completable
import io.reactivex.Single
import services.ApiService

class UserRemoteDataSource(val apiService: ApiService) :
    UserDataSource {
    override fun login(username: String, password: String): Single<Login> {
        return apiService.login(JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
        })
    }

    override fun signUp(username: String, password: String): Single<MSG> {
        return apiService.signUp(JsonObject().apply {
            //چیزی که قراره بفرستیم
            addProperty("phone", username)
            addProperty("password", password)
        })
    }

    override fun saveLogin(login: Boolean) {
        TODO("Not yet implemented")
    }

    override fun checkLogin() {
        TODO("Not yet implemented")
    }
}