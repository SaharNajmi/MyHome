package com.example.myhome.common

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.asyncNetworkRequest(): Single<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun Context.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun itemSelectedSpinner(
    spinner: Spinner,
    context: Context,
    arrayItems: Array<String>
) {
    val adapterCate = ArrayAdapter<String>(
        context,
        android.R.layout.simple_spinner_item, arrayItems
    )
    //set dropdown
    adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = adapterCate

    //item default spinner
    spinner.setSelection(0)
}
