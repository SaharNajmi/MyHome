package data.repository

import data.AuthState
import data.repository.source.UserDataSource
import data.repository.source.UserLocalDataSource
import io.reactivex.Single

class UserRepositoryImplement(
    val userLocalDataSource: UserLocalDataSource,
    val userRemoteDataSource: UserDataSource
) : UserRepository {
    override fun login(phone: String, password: String): Single<AuthState> =
        userRemoteDataSource.login(phone, password).doOnSuccess {
            LoginUpdate.update(it.state)
            userLocalDataSource.saveLogin(it.state)
        }

    override fun checkLogin() = userLocalDataSource.checkLogin()

    override fun signOut() {
        userLocalDataSource.signOut()
        LoginUpdate.update(false)
    }
}