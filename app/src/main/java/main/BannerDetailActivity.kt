package main

import android.os.Bundle
import androidx.lifecycle.observe
import com.example.myhome.R
import common.MyHomeActivity
import kotlinx.android.synthetic.main.activity_banner_detail.*
import kotlinx.android.synthetic.main.layout_profile.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import services.ImageLoadingService
import timber.log.Timber
import view.scroll.ObservableScrollViewCallbacks
import view.scroll.ScrollState
import java.lang.String

class BannerDetailActivity : MyHomeActivity() {

    val bannerDetailViewModel: BannerDetailViewModel by viewModel { parametersOf(intent.extras) }
    val imageLoadingService: ImageLoadingService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_detail)


        //send value when clicked item recyclerView and Show detail values in next activity
        bannerDetailViewModel.bannerLiveData.observe(this) {
            Timber.i(it.toString())
            imageLoadingService.load(image_detail_banner, it.image)
            imageLoadingService.load(image_profile, it.image)


            title_banner_show.text = it.title
            txt_location_show.text = it.location
            txt_number_of_rooms_show.text = it.numberOfRooms.toString()
            txt_home_size_show.text = it.homeSize.toString()
            price_banner_show.text = "قیمت: ${String.format("%,d", it.price)} تومان "

            title_banner_hide.text = it.title
            txt_location_hide.text = it.location
            txt_number_of_rooms_hide.text = it.numberOfRooms.toString()
            txt_home_size_hide.text = it.homeSize.toString()
            price_banner_hide.text = "قیمت: ${String.format("%,d", it.price)} تومان "

        }


        //یعنی تمام کارهای یو انجام و محاسبه شده
        image_detail_banner.post {
            val imageHeight = image_detail_banner.height

            //ای دی را داخل یه متغیر میزاریم که هر بار نره findviewbyid را کال کنه
            val mainInfoShow = mainInfoShow
            val mainInfoHide = mainInfoHide

            val bannerImageView = image_detail_banner

            observableScrollView.addScrollViewCallbacks(object : ObservableScrollViewCallbacks {
                override fun onScrollChanged(
                    scrollY: Int,
                    firstScroll: Boolean,
                    dragging: Boolean
                ) {
                    if (scrollY > imageHeight - 80) {
                        mainInfoShow.alpha = 0f
                        mainInfoHide.alpha = 1f
                    } else {
                        mainInfoShow.alpha = 1f
                        mainInfoHide.alpha = 0f
                    }

                    //سرعت بالا رفتن عکس نصف سرعت اسکرول خودمون باشه
                    bannerImageView.translationY = scrollY.toFloat() / 2

                }

                override fun onDownMotionEvent() {
                }

                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
                }

            })
        }
    }
}