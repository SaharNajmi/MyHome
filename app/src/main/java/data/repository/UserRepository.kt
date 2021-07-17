package data.repository

import data.AuthState
import io.reactivex.Single

interface UserRepository {
    fun login(phone: String, password: String): Single<AuthState>

    fun checkLogin()
}