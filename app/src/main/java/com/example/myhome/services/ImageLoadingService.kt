package com.example.myhome.services

import com.example.myhome.view.MyHomeImageView

interface ImageLoadingService {
    fun load(imageView: MyHomeImageView, imageUrl: String)
}