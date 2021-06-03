package main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.myhome.R

//قراره همه ی فرگمنت ها موارد داخل این کلاس را اکستند کنند و دیگر نیازی نباشد داخل همه فرگمنت ها هر سری اضافه کنیم- یکبار مینویسیم همیشه ازش استفاده میکنیم
abstract class MyHomeFragment : Fragment(), MyHomeView {
    override val rootView: CoordinatorLayout?
        get() = view as CoordinatorLayout
    override val viewContext: Context?
        get() = context
}

abstract class MyHomeActivity : AppCompatActivity(), MyHomeView {
    override val rootView: CoordinatorLayout?
        get() = window.decorView.rootView as CoordinatorLayout?
    override val viewContext: Context?
        get() = this
}

interface MyHomeView {
    val rootView: CoordinatorLayout?
    val viewContext: Context?

    fun setProgress(mustShow: Boolean) {
        //اگر rootView خالی نبود
        rootView?.let {
            viewContext?.let { context ->
                //ست شده CoordinatorLayout چک کن که روت ویو قبلا داخل
                var loadView = it.findViewById<View>(R.id.loadingView)
                //اگر loadView بهCoordinatorLayout اضافه نشده بود آن را اضافه میکند
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
