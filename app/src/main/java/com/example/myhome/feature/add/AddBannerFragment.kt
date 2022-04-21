package com.example.myhome.feature.add

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
import com.example.myhome.R
import com.example.myhome.common.Constants.CATEGORY
import com.example.myhome.common.Constants.LOCATION
import com.example.myhome.common.Constants.REQUEST_CODE
import com.example.myhome.common.Constants.SELL_OR_RENT
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.common.Result
import com.example.myhome.common.showMessage
import com.example.myhome.feature.main.BannerViewModel
import com.example.myhome.feature.profile.UserViewModel
import com.example.myhome.services.UriToUploadable
import kotlinx.android.synthetic.main.fragment_add_banner.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class AddBannerFragment : MyHomeFragment() {
    private val bannerViewModel: BannerViewModel by viewModel {
        parametersOf(
            CATEGORY,
            SELL_OR_RENT,
            "all",
            0,
            0
        )
    }
    private val userViewModel: UserViewModel by viewModel()
    private var postImage: MultipartBody.Part? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_banner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //default image
        add_banner_image.setImageResource(R.drawable.ic_add_photo)
        //add location
        btn_add_location.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    MapActivity::class.java
                )
            )
        }
        //check sign in before add Banner
        if (userViewModel.isSignIn) {
            showLayoutAdd.visibility = View.VISIBLE
            authBtn.visibility = View.GONE
            txtAlert.visibility = View.GONE

            //spinner select sell or rent
            itemSelectedSpinnerSellOrRent()

            //spinner select category
            itemSelectSpinnerCategory()

            //load image in gallery
            add_banner_image.setOnClickListener {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, REQUEST_CODE)
            }

            //btn add banner
            btn_add.setOnClickListener {
                addBanner()
            }
            ///btn clear field
            btn_clear.setOnClickListener {
                clearAllField()
            }
        } else {
            showLayoutAdd.visibility = View.GONE
            authBtn.visibility = View.VISIBLE
            txtAlert.visibility = View.VISIBLE

            //go login
            authBtn.setOnClickListener {
                findNavController().navigate(AddBannerFragmentDirections.actionAddToLoginOrSignUp())
            }
        }

        //add banner Result
        bannerViewModel.addBannerResult.observe(requireActivity()) { result ->
            when (result) {
                is Result.Error -> {
                    setProgress(false)
                    context?.showMessage("ثبت آگهی با مشکل مواجه شد")
                }
                is Result.Loading -> {
                    setProgress(true)
                }
                is Result.Success -> {
                    setProgress(false)
                    clearAllField()
                    context?.showMessage("آگهی مورد نظر ثبت شد و در انتظار بررسی است")
                }
            }
        }
    }

    private fun addBanner() {
        userViewModel.user.observe(requireActivity()) { user ->
            bannerViewModel.addBanner(
                user.id,
                RequestBody.create(
                    okhttp3.MultipartBody.FORM,
                    add_title.text.toString().trim()
                ),
                RequestBody.create(
                    okhttp3.MultipartBody.FORM,
                    add_description.text.toString().trim()
                ),
                RequestBody.create(
                    okhttp3.MultipartBody.FORM,
                    add_price.text.toString().trim()
                ),
                RequestBody.create(
                    okhttp3.MultipartBody.FORM,
                    add_location.text.toString().trim()
                ),
                add_category.selectedItemPosition + 1,
                add_sell_or_rent.selectedItemPosition + 1,
                add_home_size.text.toString().toInt(),
                add_number_of_room.text.toString().toInt(),
                postImage
            )
        }
    }

    private fun clearAllField() {
        //clear value
        add_title.setText("")
        add_description.setText("")
        add_price.setText("")
        add_location.setText("")
        add_home_size.setText("")
        add_number_of_room.setText("")
        add_category.setSelection(0)
        add_sell_or_rent.setSelection(0)
        add_banner_image.setImageResource(R.drawable.ic_add_photo)
    }

    private fun itemSelectSpinnerCategory() {
        val adapterCate = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, resources.getStringArray(R.array.array_category)
        )
        //set dropdown
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        add_category.adapter = adapterCate

        //item default spinner
        add_category.setSelection(0)
    }

    private fun itemSelectedSpinnerSellOrRent() {
        val adapterCate = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, arrayOf("فروش", "اجاره")
        )
        //set dropdown
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        add_sell_or_rent.adapter = adapterCate

        //item default spinner
        add_sell_or_rent.setSelection(0)
    }

    override fun onResume() {
        super.onResume()
        //show location in editText
        add_location.setText(LOCATION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val upload = UriToUploadable(requireActivity())
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val imageUri = data?.data
            add_banner_image.setImageURI(imageUri)
            postImage = upload.getUploaderFile(imageUri, "image", "${UUID.randomUUID()}")
        }
    }

}