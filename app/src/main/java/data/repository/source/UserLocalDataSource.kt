package data.repository.source

import data.AuthState
import io.reactivex.Single

class UserLocalDataSource : UserDataSource {
    override fun login(phone: String, password: String): Single<AuthState> {
        TODO("Not yet implemented")
    }
}