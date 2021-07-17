package data.repository.source

import data.AuthState
import io.reactivex.Single

interface UserDataSource {
    fun login(phone: String, password: String): Single<AuthState>

    fun saveLogin(login: Boolean)

    fun checkLogin()

    fun signOut()
}