package data.repository.source

import data.AuthState
import io.reactivex.Single
import services.ApiService

class UserRemoteDataSource(val apiService: ApiService) : UserDataSource {
    override fun login(phone: String, password: String): Single<AuthState> =
        apiService.login(phone, password)

    override fun saveLogin(login: Boolean) {
        TODO("Not yet implemented")
    }

    override fun checkLogin() {
        TODO("Not yet implemented")
    }
}