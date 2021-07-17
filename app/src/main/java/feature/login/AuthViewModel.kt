package feature.login

import common.MyHomeViewModel
import data.AuthState
import data.repository.UserRepository
import io.reactivex.Single
import services.ApiService

class AuthViewModel(val repository: UserRepository) : MyHomeViewModel() {
    fun login(phone: String, password: String): Single<AuthState> =
        repository.login(phone, password)
}