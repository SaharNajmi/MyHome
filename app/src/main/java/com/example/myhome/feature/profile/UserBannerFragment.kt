package com.example.myhome.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.data.model.Banner
import com.example.myhome.databinding.FragmentUserBannerBinding
import com.example.myhome.feature.home.BannerListAdapter
import com.example.myhome.feature.main.BannerViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserBannerFragment : MyHomeFragment(), BannerListAdapter.BannerOnClickListener {
    private lateinit var binding: FragmentUserBannerBinding
    private val userViewModel: UserViewModel by viewModel()
    private val bannerViewModel: BannerViewModel by viewModel { parametersOf(1, 1) }
    private val bannerArrayList: BannerListAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //onClick item recyclerView
        bannerArrayList.bannerOnClickListener = this

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        //get banners
        getUserBanners()
    }

    private fun getUserBanners() {
        userViewModel.banners.observe(requireActivity()) { banners ->
            if (banners.isEmpty()) {
                //show empty layout
                binding.emptyLayout.isVisible = true
                binding.emptyLayout.setText(resources.getString(R.string.emptyListUserBanner))
            } else {
                bannerArrayList.banner = banners as ArrayList<Banner>
                binding.recyclerViewUserBanner.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                binding.recyclerViewUserBanner.adapter = bannerArrayList

                //hide empty layout
                binding.emptyLayout.isVisible = false
            }
        }
    }

    override fun onBannerClick(banner: Banner) {
        findNavController().navigate(
            UserBannerFragmentDirections.actionUserBannerFragmentToBannerDetailFragment(
                banner
            )
        )
    }

    override fun onFavoriteBtnClick(banner: Banner) {
        bannerViewModel.addBannerToFavorite(banner)
    }

    override fun onResume() {
        super.onResume()
        userViewModel.getBanner()
    }
}