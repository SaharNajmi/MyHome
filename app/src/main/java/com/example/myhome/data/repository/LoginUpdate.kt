package com.example.myhome.data.repository

import timber.log.Timber

object LoginUpdate {
    var login: Boolean? = false
        private set

    fun update(login: Boolean?) {
        Timber.i("LOGIN: $login")
        LoginUpdate.login = login
    }
}