package com.example.myhome.feature.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myhome.R
import com.example.myhome.common.Constants
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.common.Result
import com.example.myhome.common.showMessage
import com.example.myhome.data.model.Banner
import com.example.myhome.databinding.FragmentBannerDetailBinding
import com.example.myhome.feature.favorite.FavoriteViewModel
import com.example.myhome.feature.profile.UserViewModel
import com.example.myhome.services.ImageLoadingService
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BannerDetailFragment : MyHomeFragment() {
    private lateinit var binding: FragmentBannerDetailBinding
    lateinit var banner: Banner
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: UserViewModel by viewModel()
    private val favoriteViewModel: FavoriteViewModel by inject()
    private val bannerViewModel: BannerViewModel by viewModel {
        parametersOf(
            Constants.CATEGORY,
            Constants.SELL_OR_RENT,
            "all",
            0,
            0
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBannerDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: BannerDetailFragmentArgs by navArgs()
        banner = args.bannerDetail

        //hideBottom Navigation
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigation()
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.apply {
            imageLoadingService.load(
                layoutUserInfo.userImage,
                "${Constants.BASE_URL}${banner.userImage}"
            )
            imageLoadingService.load(
                imageDetailBanner,
                "${Constants.BASE_URL}${banner.bannerImage}"
            )
            titleBannerShow.text = banner.title
            descBannerShow.text = banner.description
            txtLocationShow.text = banner.location
            txtNumberOfRoomsShow.text = banner.numberOfRooms.toString()
            txtHomeSizeShow.text = banner.homeSize.toString()
            priceBannerShow.text = "قیمت: ${banner.price} تومان "
            layoutUserInfo.userName.text = banner.username
        }

        //favorite
        checkFavorite(banner.fav)
        binding.favoriteBtn.setOnClickListener {
            changeFavorite(banner)
        }

        //edit or delete banner
        checkUserBanner()

        //delete banner
        bannerViewModel.deleteBannerResult.observe(requireActivity()) { result ->
            when (result) {
                is Result.Error -> {
                    setProgress(false)
                    context?.showMessage("حذف آگهی با شکست مواجه شد!!!")
                }
                is Result.Loading -> {
                    setProgress(true)
                }
                is Result.Success -> {
                    setProgress(false)
                    context?.showMessage("آگهی مورد نظر حذف شد")
                    findNavController().popBackStack()
                }
            }
        }

    }

    private fun checkUserBanner() {
        if (viewModel.phoneNumber == banner.phone) {
            binding.layoutEditOrDelete.layoutEditBanner.visibility = View.VISIBLE

            //edit banner
            binding.layoutEditOrDelete.editBanner.setOnClickListener {
                findNavController().navigate(
                    BannerDetailFragmentDirections.actionBannerDetailFragmentToEditBannerFragment(
                        banner
                    )
                )
            }

            //delete banner
            binding.layoutEditOrDelete.deleteBanner.setOnClickListener {
                deleteBanner()
            }

        } else {
            binding.layoutUserInfo.layoutUserInfo.visibility = View.VISIBLE

            //call
            binding.layoutUserInfo.userPhone.setOnClickListener {
                //
            }
        }
    }

    private fun deleteBanner() {
        AlertDialog.Builder(requireContext())
            .setTitle("حذف آگهی")
            .setMessage("آیا میخواهید این آگهی را حذف کنید؟")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setNegativeButton("خیر", null)
            .setPositiveButton("بله") { _, _ -> bannerViewModel.deleteBanner(banner.id) }
            .show()
    }

    private fun checkFavorite(favorite: Boolean) {
        if (favorite)
            binding.favoriteBtn.setImageResource(R.drawable.ic_bookmarked)
        else
            binding.favoriteBtn.setImageResource(R.drawable.ic_not_bookmarked)
    }

    private fun changeFavorite(banner: Banner) {
        banner.fav = if (banner.fav) {
            binding.favoriteBtn.setImageResource(R.drawable.ic_not_bookmarked)
            favoriteViewModel.deleteFavorites(banner)
            false
        } else {
            binding.favoriteBtn.setImageResource(R.drawable.ic_bookmarked)
            favoriteViewModel.addFavorites(banner)
            true
        }
    }

    override fun onStop() {
        super.onStop()
        //show BottomNavigation
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showBottomNavigation()
        }
    }
}
