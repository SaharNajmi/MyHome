package data.repository

import data.AuthState
import data.repository.source.UserDataSource
import data.repository.source.UserLocalDataSource
import io.reactivex.Single
import timber.log.Timber

class UserRepositoryImplement(
    val userLocalDataSource: UserLocalDataSource,
    val userRemoteDataSource: UserDataSource
) : UserRepository {
    override fun login(phone: String, password: String): Single<AuthState> =
        userRemoteDataSource.login(phone, password).doOnSuccess {
            Timber.i("LOGIN" + it.state.toString())
        }
}