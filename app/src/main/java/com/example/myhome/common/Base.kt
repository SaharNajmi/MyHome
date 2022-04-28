package com.example.myhome.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.myhome.R
import io.reactivex.disposables.CompositeDisposable


abstract class MyHomeFragment : Fragment(), MyHomeView {
    override val rootView: FrameLayout?
        get() = view as FrameLayout
    override val viewContext: Context?
        get() = context
}

abstract class MyHomeActivity : AppCompatActivity(), MyHomeView {
    override val rootView: FrameLayout?
        get() = window.decorView.rootView as FrameLayout?
    override val viewContext: Context?
        get() = this
}

interface MyHomeView {
    val rootView: FrameLayout?
    val viewContext: Context?
    fun setProgress(mustShow: Boolean) {
        rootView?.let {
            viewContext?.let { context ->
                var loadView = it.findViewById<View>(R.id.loadingView)
                if (loadView == null && mustShow) {
                    loadView =
                        LayoutInflater.from(context).inflate(R.layout.view_loading, it, false)
                    it.addView(loadView)
                }
                loadView?.visibility = if (mustShow) View.VISIBLE else View.GONE
            }
        }
    }
}

abstract class MyHomeViewModel : ViewModel() {
    val compositeDisposable = CompositeDisposable()
    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
