package com.example.myhome.common

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.example.myhome.R

class EmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.layout_empty_view, this)
    }

    fun setText(string: String) {
        findViewById<TextView>(R.id.txtEmpty).text = string
    }
}