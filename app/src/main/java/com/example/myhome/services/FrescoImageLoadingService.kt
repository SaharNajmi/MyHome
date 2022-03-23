package com.example.myhome.services

import com.facebook.drawee.view.SimpleDraweeView
import com.example.myhome.view.MyHomeImageView

class FrescoImageLoadingService : ImageLoadingService {
    override fun load(imageView: MyHomeImageView, imageUrl: String) {
        if (imageView is SimpleDraweeView)
            imageView.setImageURI(imageUrl)
        else
            throw IllegalStateException("imageView must be instance of SimpleDraweeView")
    }
}