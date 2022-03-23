package com.example.myhome.common

import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

//use class SingleObserver for not implement Duplicate method
//این کلاس باید abstract باشه چون نمیخایم همه ی متد هاشو ایمپلمنت کنیم onSuccess هر موقع که خواستیم امپلمنت میکنیم
abstract class MyHomeSingleObserver<T>(val compositeDisposable: CompositeDisposable) :
    SingleObserver<T> {

    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }

    override fun onError(e: Throwable) {
        Timber.e(e)
    }
}