package com.example.myhome.feature.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myhome.R
import com.example.myhome.common.Constants
import com.example.myhome.common.Constants.CATEGORY
import com.example.myhome.common.Constants.SELL_OR_RENT
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.common.Result
import com.example.myhome.common.showMessage
import com.example.myhome.data.model.Banner
import com.example.myhome.services.ImageLoadingService
import com.example.myhome.services.UriToUploadable
import kotlinx.android.synthetic.main.fragment_edit_banner.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class EditBannerFragment : MyHomeFragment() {
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
        return inflater.inflate(R.layout.fragment_edit_banner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: BannerDetailFragmentArgs by navArgs()
        banner = args.bannerDetail

        //show old values
        edit_title.setText(banner.title)
        edit_location.setText(banner.location)
        //delete ',' from price
        edit_price.setText(banner.price.filter(Char::isDigit))
        edit_description.setText(banner.description)
        edit_home_size.setText(banner.homeSize.toString())
        edit_number_of_room.setText(banner.numberOfRooms.toString())
        edit_category.setSelection(banner.category - 1)
        edit_sell_or_rent.setSelection(banner.sellOrRent - 1)
        if (banner.bannerImage != "")
            imageLoadingService.load(
                edit_banner_image,
                "${Constants.BASE_URL}${banner.bannerImage}"
            )

        bannerViewModel.selectedImageUri.observe(requireActivity()) { uri ->
            edit_banner_image.setImageURI(uri)
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
        edit_banner_image.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, Constants.REQUEST_CODE)
        }

        itemSelectSpinnerCategory()
        itemSelectedSpinnerSellOrRent()

        //edit banner
        btn_edit_banner.setOnClickListener {
            bannerViewModel.editBanner(
                banner.id,
                banner.userID,
                RequestBody.create(
                    MultipartBody.FORM,
                    edit_title.text.toString().trim()
                ),
                RequestBody.create(
                    MultipartBody.FORM,
                    edit_description.text.toString()
                ),
                RequestBody.create(
                    MultipartBody.FORM,
                    edit_price.text.toString()
                ), RequestBody.create(
                    MultipartBody.FORM,
                    edit_location.text.toString()
                ),
                edit_category.selectedItemPosition + 1,
                edit_sell_or_rent.selectedItemPosition + 1,
                edit_home_size.text.toString().toInt(),
                edit_number_of_room.text.toString().toInt(),
                postImage
            )
        }

    }

    private fun itemSelectSpinnerCategory() {
        val adapterCate = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, resources.getStringArray(R.array.array_category)
        )
        //set dropdown
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edit_category.adapter = adapterCate

        //item default spinner
        edit_category.setSelection(banner.category - 1)
    }

    private fun itemSelectedSpinnerSellOrRent() {
        val adapterCate = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, arrayOf("فروش", "اجاره")
        )
        //set dropdown
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edit_sell_or_rent.adapter = adapterCate

        //item default spinner
        edit_sell_or_rent.setSelection(banner.sellOrRent - 1)
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
}
