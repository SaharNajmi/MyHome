package data.repository.source

import android.content.SharedPreferences
import data.Login
import data.MSG
import io.reactivex.Single

class UserLocalDataSource(val sharedPreferences: SharedPreferences) : UserDataSource {
    override fun login(username: String, password: String): Single<Login> {
        TODO("Not yet implemented")
    }

    override fun signUp(username: String, password: String): Single<MSG> {
        TODO("Not yet implemented")
    }

    override fun saveLogin(login: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean("login", login)
        }.apply()
    }

    override fun checkLogin() {
      sharedPreferences.getBoolean("login", false)
    }
}