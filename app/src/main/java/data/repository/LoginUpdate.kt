package data.repository

import timber.log.Timber

object LoginUpdate {
    var login: Boolean? = false
        //فقط توسط این تایع این مقدار آپدیت میشه
        private set

    fun update(login: Boolean?) {
        Timber.i("LOGIN: ${login}")
        LoginUpdate.login = login
    }
}