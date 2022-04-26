package com.example.myhome.feature.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myhome.R
import com.example.myhome.common.*
import com.example.myhome.common.Constants.CATEGORY
import com.example.myhome.common.Constants.SELL_OR_RENT
import com.example.myhome.data.model.Banner
import com.example.myhome.databinding.FragmentEditBannerBinding
import com.example.myhome.services.ImageLoadingService
import com.example.myhome.services.UriToUploadable
import okhttp3.MultipartBody
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class EditBannerFragment : MyHomeFragment() {
    private lateinit var binding: FragmentEditBannerBinding
    lateinit var banner: Banner
    private val imageLoadingService: ImageLoadingService by inject()
    private var postImage: MultipartBody.Part? = null
    private val bannerViewModel: BannerViewModel by viewModel {
        parametersOf(
            CATEGORY,
            SELL_OR_RENT,
            "all",
            0,
            0
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: EditBannerFragmentArgs by navArgs()
        banner = args.bannerDetail

        //hideBottom Navigation
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigation()
        }

        binding.apply {
            //show old values
            editTitle.setText(banner.title)
            editLocation.setText(banner.location)
            //delete ',' from price
            editPrice.setText(banner.price.filter(Char::isDigit))
            editDescription.setText(banner.description)
            editHomeSize.setText(banner.homeSize.toString())
            editNumberOfRoom.setText(banner.numberOfRooms.toString())
            editCategory.setSelection(banner.category - 1)
            editSellOrRent.setSelection(banner.sellOrRent - 1)
        }

        if (banner.bannerImage != "")
            imageLoadingService.load(
                binding.editBannerImage,
                "${Constants.BASE_URL}${banner.bannerImage}"
            )

        bannerViewModel.selectedImageUri.observe(requireActivity()) { uri ->
            binding.editBannerImage.setImageURI(uri)
        }

        bannerViewModel.editBannerResult.observe(requireActivity()) { result ->
            when (result) {
                is Result.Error -> {
                    setProgress(false)
                    context?.showMessage("آپدیت آگهی با شکست مواجه شد!!!")
                }
                is Result.Loading -> {
                    setProgress(true)
                }
                is Result.Success -> {
                    setProgress(false)
                    context?.showMessage("آگهی مورد نظر با موفقیت آپدیت شد منتظر تایید آگهیتان باشید!")
                    findNavController().navigate(EditBannerFragmentDirections.actionEditBannerFragmentToUserBannerFragment())
                }
            }
        }

        //load image in gallery
        binding.editBannerImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, Constants.REQUEST_CODE)
        }

        //spinner select sell or rent
        itemSelectedSpinner(binding.editSellOrRent, requireContext(), arrayOf("فروش", "اجاره"))

        //spinner select category
        itemSelectedSpinner(
            binding.editCategory,
            requireContext(),
            resources.getStringArray(R.array.array_category)
        )

        //edit banner
        binding.btnEditBanner.setOnClickListener {
            bannerViewModel.editBanner(
                banner.id,
                banner.userID,
                binding.editTitle.text.toString().trim(),
                binding.editDescription.text.toString(),
                binding.editPrice.text.toString(),
                binding.editLocation.text.toString(),
                binding.editCategory.selectedItemPosition + 1,
                binding.editSellOrRent.selectedItemPosition + 1,
                binding.editHomeSize.text.toString().toInt(),
                binding.editNumberOfRoom.text.toString().toInt(),
                postImage
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val upload = UriToUploadable(requireActivity())
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE) {
            val imageUri = data?.data
            imageUri?.let { bannerViewModel.setImageUri(it) }
            postImage = upload.getUploaderFile(imageUri, "image", "${UUID.randomUUID()}")
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
