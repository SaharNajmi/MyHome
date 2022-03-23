package com.example.myhome.view

import android.content.Context
import android.util.AttributeSet
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.view.SimpleDraweeView


//از این کلاس استفده میکنیم تا هر موقع که خواستیم جای کتابخانه Fresco از یک کتابخانه دیگر برای لود تصاویر استفاده کنیم فقط لازم است SimpleDraweeView را تغیر بدیم
class MyHomeImageView : SimpleDraweeView {
    constructor(context: Context?, hierarchy: GenericDraweeHierarchy?) : super(context, hierarchy)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)
}