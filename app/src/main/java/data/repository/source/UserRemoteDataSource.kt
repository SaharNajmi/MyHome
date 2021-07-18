package data.repository.source

import data.AuthState
import data.UserInformation
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import services.ApiService

class UserRemoteDataSource(val apiService: ApiService) : UserDataSource {
    override fun login(phone: String, password: String): Single<AuthState> =
        apiService.login(phone, password)

    override fun signUp(
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        imageProfile: MultipartBody.Part?
    ): Single<AuthState> = apiService.signUp(phoneNumber, username, password, imageProfile)

    override fun saveLogin(login: Boolean) {
        TODO("Not yet implemented")
    }

    override fun checkLogin() {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }

    override fun savePhoneNumber(phoneNumber: String) {
        TODO("Not yet implemented")
    }

    override fun getPhoneNumber(): String {
        TODO("Not yet implemented")
    }

    override fun getUser(phone: String): Single<UserInformation> = apiService.getUser(phone)

    override fun editUser(
        id: RequestBody,
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        image: MultipartBody.Part?
    ): Single<AuthState> = apiService.editUser(id, phoneNumber, username, password, image)
}