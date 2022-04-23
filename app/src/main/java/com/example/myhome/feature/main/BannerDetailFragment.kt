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
import com.example.myhome.feature.favorite.FavoriteViewModel
import com.example.myhome.feature.profile.UserViewModel
import com.example.myhome.services.ImageLoadingService
import kotlinx.android.synthetic.main.fragment_banner_detail.*
import kotlinx.android.synthetic.main.layout_edit_banner.*
import kotlinx.android.synthetic.main.layout_profile.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BannerDetailFragment : MyHomeFragment() {
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
        return inflater.inflate(R.layout.fragment_banner_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: BannerDetailFragmentArgs by navArgs()
        banner = args.bannerDetail

        //hideBottom Navigation
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigation()
        }

        imageLoadingService.load(image_detail_banner, "${Constants.BASE_URL}${banner.bannerImage}")
        imageLoadingService.load(user_image, "${Constants.BASE_URL}${banner.userImage}")
        title_banner_show.text = banner.title
        desc_banner_show.text = banner.description
        txt_location_show.text = banner.location
        txt_number_of_rooms_show.text = banner.numberOfRooms.toString()
        txt_home_size_show.text = banner.homeSize.toString()
        price_banner_show.text = "قیمت: ${banner.price} تومان "
        user_name.text = banner.username

        //favorite
        checkFavorite(banner.fav)
        favoriteBtn.setOnClickListener {
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
                    findNavController().navigate(BannerDetailFragmentDirections.actionBannerDetailFragmentToUserBannerFragment())
                }
            }
        }

    }

    private fun checkUserBanner() {
        if (viewModel.phoneNumber == banner.phone) {
            layout_edit_or_delete.visibility = View.VISIBLE

            //edit banner
            edit_banner.setOnClickListener {
                findNavController().navigate(
                    BannerDetailFragmentDirections.actionBannerDetailFragmentToEditBannerFragment(
                        banner
                    )
                )
            }

            //delete banner
            delete_banner.setOnClickListener {
                deleteBanner()
            }

        } else {
            layout_contact_us.visibility = View.VISIBLE

            //call
            user_phone.setOnClickListener {
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
            favoriteBtn.setImageResource(R.drawable.ic_bookmarked)
        else
            favoriteBtn.setImageResource(R.drawable.ic_not_bookmarked)
    }

    private fun changeFavorite(banner: Banner) {
        banner.fav = if (banner.fav) {
            favoriteBtn.setImageResource(R.drawable.ic_not_bookmarked)
            favoriteViewModel.deleteFavorites(banner)
            false
        } else {
            favoriteBtn.setImageResource(R.drawable.ic_bookmarked)
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
