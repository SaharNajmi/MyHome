package feature.profile

import common.MyHomeViewModel
import data.repository.LoginUpdate
import data.repository.UserRepository

class ProfileViewModel(val userRepository: UserRepository) : MyHomeViewModel() {

    val isSignIn: Boolean
        get() = LoginUpdate.login != false

    fun signOut() = userRepository.signOut()

}