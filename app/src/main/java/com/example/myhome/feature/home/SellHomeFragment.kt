package com.example.myhome.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import com.example.myhome.common.Constants.CATEGORY
import com.example.myhome.common.Constants.EXTRA_KEY_DATA
import com.example.myhome.common.Constants.HOME_SIZE
import com.example.myhome.common.Constants.NUMBER_OF_ROOM
import com.example.myhome.common.Constants.PRICE
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.data.model.Banner
import com.example.myhome.feature.main.BannerDetailActivity
import com.example.myhome.feature.main.BannerViewModel
import com.example.myhome.feature.main.ShareViewModel
import kotlinx.android.synthetic.main.fragment_sell_home.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SellHomeFragment : MyHomeFragment(), BannerListAdapter.BannerOnClickListener {
    private var SELL_OR_RENT = 1
    private val bannerArrayList: BannerListAdapter by inject()

    private val bannerViewModel by viewModel<BannerViewModel>() {
        parametersOf(
            CATEGORY,
            SELL_OR_RENT,
            PRICE,
            HOME_SIZE,
            NUMBER_OF_ROOM
        )
    }

    //sharing data between Fragments
    private val shareViewModel by sharedViewModel<ShareViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sell_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //show/hide progressBar
        bannerViewModel.progress.observe((viewLifecycleOwner)) {
            setProgress(it)
        }

        //get text search in another fragment
        shareViewModel.search.observe(requireActivity()) {
            bannerArrayList.filter.filter(it)
        }

        //get category in another fragment -> get Data between Fragments
        shareViewModel.category.observe(requireActivity()) {
            bannerViewModel.chaneCategory(it)
        }

        //filter banner list
        shareViewModel.filter.observe(requireActivity()) {
            bannerViewModel.filter(it[0] as String, it[1] as Int, it[2] as Int)
        }

        //setOnClickListener item recyclerView
        bannerArrayList.bannerOnClickListener = this

        //show all banners
        getBanners()
    }

    private fun getBanners() {
        bannerViewModel.banners.observe(viewLifecycleOwner) { banners ->
            if (banners.isEmpty()) {
                //show empty layout
                showEmptyState(true)
            } else {
                bannerArrayList.banner = banners as ArrayList<Banner>
                recycler_view_sell.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                recycler_view_sell.adapter = bannerArrayList

                showEmptyState(false)
            }
        }
    }

    override fun onBannerClick(banner: Banner) {
        startActivity(Intent(requireContext(), BannerDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, banner)
        })
    }

    override fun onFavoriteBtnClick(banner: Banner) {
        bannerViewModel.addBannerToFavorite(banner)
    }

    override fun onResume() {
        super.onResume()
        bannerViewModel.getBanner()
    }
}