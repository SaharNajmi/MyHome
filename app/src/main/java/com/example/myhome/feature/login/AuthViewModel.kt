package com.example.myhome.feature.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myhome.common.MyHomeViewModel
import com.example.myhome.common.Result
import com.example.myhome.common.asyncNetworkRequest
import com.example.myhome.data.model.State
import com.example.myhome.data.repository.UserRepository
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthViewModel(val repository: UserRepository) : MyHomeViewModel() {

    private val _loginResult = MutableLiveData<Result<State>>()
    val loginResult: LiveData<Result<State>> = _loginResult

    private val _signUpResult = MutableLiveData<Result<State>>()
    val signUpResult: LiveData<Result<State>> = _signUpResult

    fun login(phone: String, password: String) = repository.login(phone, password)
        .asyncNetworkRequest()
        .subscribe(object : SingleObserver<State> {
            override fun onSuccess(t: State) {
                _loginResult.value = Result.Success(t)
            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
                _loginResult.value = Result.Loading
            }

            override fun onError(e: Throwable) {
                _loginResult.value = Result.Error(e)
            }
        })

    fun signUp(
        phoneNumber: RequestBody,
        username: RequestBody,
        password: RequestBody,
        imageProfile: MultipartBody.Part?
    ) =
        repository.signUp(phoneNumber, username, password, imageProfile).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<State> {
                override fun onSuccess(t: State) {
                    _signUpResult.value = Result.Success(t)
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                    _signUpResult.value = Result.Loading
                }

                override fun onError(e: Throwable) {
                    _signUpResult.value = Result.Error(e)
                }
            })

}