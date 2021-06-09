package services

import view.MyHomeImageView

interface ImageLoadingService {
    fun load(imageView: MyHomeImageView, imageUrl: String)
}