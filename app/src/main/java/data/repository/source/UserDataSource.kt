package data.repository.source

import data.Login
import data.MSG
import io.reactivex.Single

interface UserDataSource {
    fun login(username: String, password: String): Single<Login>

    fun signUp(username: String, password: String): Single<MSG>

    fun saveLogin(login: Boolean)

    fun checkLogin()
}