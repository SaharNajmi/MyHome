package data.repository

import data.repository.source.UserLocalDataSource
import data.repository.source.UserRemoteDataSource
import io.reactivex.Completable

class UserRepositoryImplement(
    val userLocalDataSource: UserLocalDataSource,
    val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override fun login(username: String, password: String): Completable {
        return userRemoteDataSource.login(username, password).doOnSuccess {
            saveLogin(true)
        }.ignoreElement()
        //نکته: ignoreElement: برای اینکه خروجی تابع لاگین که از نوع سینگله به Completable تبدیل بشه استفاده میکنیم
    }

    override fun signUp(username: String, password: String): Completable {
        //وقتی کاربر ثبتام میکنه هم ثبتنام انجام میشه هم لاگین
        //flatMap: برای اینکه چند تا رکوست با هم انجام بشه
        return userRemoteDataSource.signUp(username, password).flatMap {
            userRemoteDataSource.login(username, password)
        }.doOnSuccess {
            saveLogin(true)
        }.ignoreElement()
    }


    override fun saveLogin(login: Boolean) {
        userLocalDataSource.saveLogin(login)
    }

    override fun checkLogin() {
        userLocalDataSource.checkLogin()
    }
}