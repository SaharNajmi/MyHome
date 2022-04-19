package com.example.myhome.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.data.model.Banner
import com.example.myhome.feature.home.BannerListAdapter
import com.example.myhome.feature.main.BannerViewModel
import kotlinx.android.synthetic.main.fragment_user_banner.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserBannerFragment : MyHomeFragment(), BannerListAdapter.BannerOnClickListener {
    private val userViewModel: UserViewModel by viewModel()
    private val bannerViewModel: BannerViewModel by viewModel { parametersOf(1, 1) }
    private val bannerArrayList: BannerListAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_banner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //onClick item recyclerView
        bannerArrayList.bannerOnClickListener = this

        //get banners
        getUserBanners()
    }

    private fun getUserBanners() {
        userViewModel.banners.observe(requireActivity()) { banners ->
            if (banners.isEmpty()) {
                //show empty layout
                showEmptyState(true)

                txtEmpty.text = getString(R.string.emptyListUserBanner)
            } else {
                bannerArrayList.banner = banners as ArrayList<Banner>
                recycler_view_user_banner.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                recycler_view_user_banner.adapter = bannerArrayList

                //hide empty layout
                showEmptyState(false)
            }
        }
    }

    override fun onBannerClick(banner: Banner) {
        findNavController().navigate( UserBannerFragmentDirections.actionUserBannerFragmentToBannerDetailFragment(banner))
    }

    override fun onFavoriteBtnClick(banner: Banner) {
        bannerViewModel.addBannerToFavorite(banner)
    }

    override fun onResume() {
        super.onResume()
        userViewModel.getBanner()
    }
}