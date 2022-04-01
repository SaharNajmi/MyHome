package com.example.myhome.feature.profile

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import com.example.myhome.common.Constants.EXTRA_KEY_DATA
import com.example.myhome.common.MyHomeActivity
import com.example.myhome.data.model.Banner
import com.example.myhome.feature.home.BannerListAdapter
import com.example.myhome.feature.main.BannerDetailActivity
import com.example.myhome.feature.main.BannerViewModel
import kotlinx.android.synthetic.main.activity_user_banner.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserBannerActivity : MyHomeActivity(), BannerListAdapter.BannerOnClickListener {

    private val userViewModel: UserViewModel by viewModel()
    private val bannerViewModel: BannerViewModel by viewModel { parametersOf(1, 1) }
    private val bannerArrayList: BannerListAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_banner)

        //back button
        backBtn.setOnClickListener {
            finish()
        }

        //setOnClickListener item recyclerView
        bannerArrayList.bannerOnClickListener = this

        //get banners
        getUserBanners()
    }

    override fun onBannerClick(banner: Banner) {
        startActivity(Intent(this, BannerDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, banner)
        })
    }

    override fun onFavoriteBtnClick(banner: Banner) {
        bannerViewModel.addBannerToFavorite(banner)
    }

    private fun getUserBanners() {
        userViewModel.banners.observe(this) { banners ->
            if (banners.isEmpty()) {
                //show empty layout
                showEmptyState(true)
                txtEmpty.text = getString(R.string.emptyListUserBanner)

            } else {
                bannerArrayList.banner = banners as ArrayList<Banner>
                recycler_view_user_banner.layoutManager =
                    LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                recycler_view_user_banner.adapter = bannerArrayList

                showEmptyState(false)
            }
        }
    }

    //when delete item -> refresh recycler view
    override fun onStart() {
        super.onStart()
        userViewModel.getBanner()
    }
}