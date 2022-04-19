package com.example.myhome.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import com.example.myhome.common.Constants.CATEGORY
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.data.model.Banner
import com.example.myhome.feature.main.BannerViewModel
import com.example.myhome.feature.main.ShareViewModel
import kotlinx.android.synthetic.main.fragment_rent_home.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RentHomeFragment : MyHomeFragment(), BannerListAdapter.BannerOnClickListener {
    private var SELL_OR_RENT = 2
    private val bannerViewModel: BannerViewModel by viewModel {
        parametersOf(
            CATEGORY,
            SELL_OR_RENT,
            "all",
            0,
            0
        )
    }
    private val shareViewModel by sharedViewModel<ShareViewModel>()
    private val bannerArrayList: BannerListAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rent_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get value search in another fragment
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
                recycler_view_rent.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                recycler_view_rent.adapter = bannerArrayList

                showEmptyState(false)
            }
        }
    }

    override fun onBannerClick(banner: Banner) {
        val direction = HomeFragmentDirections.actionHomeToBannerDetailFragment(banner)
        findNavController().navigate(direction)
    }

    override fun onFavoriteBtnClick(banner: Banner) {
        bannerViewModel.addBannerToFavorite(banner)
    }


    override fun onResume() {
        super.onResume()
        bannerViewModel.getBanner()
    }
}