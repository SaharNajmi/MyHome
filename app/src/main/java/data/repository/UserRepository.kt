package data.repository

import io.reactivex.Completable

interface UserRepository {

    fun login(username: String, password: String): Completable

    fun signUp(username: String, password: String): Completable

    fun saveLogin(login: Boolean)

    fun checkLogin()
}