package com.example.myhome.feature.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.myhome.R
import com.example.myhome.common.Constants.CATEGORY
import com.example.myhome.common.Constants.LOCATION
import com.example.myhome.common.Constants.REQUEST_CODE
import com.example.myhome.common.Constants.SELL_OR_RENT
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.common.Result
import com.example.myhome.common.itemSelectedSpinner
import com.example.myhome.common.showMessage
import com.example.myhome.databinding.FragmentAddBannerBinding
import com.example.myhome.feature.main.BannerViewModel
import com.example.myhome.feature.profile.UserViewModel
import com.example.myhome.services.UriToUploadable
import okhttp3.MultipartBody
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class AddBannerFragment : MyHomeFragment() {
    private lateinit var binding: FragmentAddBannerBinding
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
        binding = FragmentAddBannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addBannerImage.setImageResource(R.drawable.ic_add_photo)
        binding.btnAddLocation.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    MapActivity::class.java
                )
            )
        }
        if (userViewModel.isSignIn) {

            binding.showLayoutAdd.visibility = View.VISIBLE
            binding.authBtn.visibility = View.GONE
            binding.txtAlert.visibility = View.GONE


            itemSelectedSpinner(binding.addSellOrRent, requireContext(), arrayOf("فروش", "اجاره"))

            itemSelectedSpinner(
                binding.addCategory,
                requireContext(),
                resources.getStringArray(R.array.array_category)
            )

            binding.addBannerImage.setOnClickListener {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, REQUEST_CODE)
            }

            binding.btnAdd.setOnClickListener {
                addBanner()
            }
            binding.btnClear.setOnClickListener {
                clearAllField()
            }
        } else {
            binding.showLayoutAdd.visibility = View.GONE
            binding.authBtn.visibility = View.VISIBLE
            binding.txtAlert.visibility = View.VISIBLE

            binding.authBtn.setOnClickListener {
                findNavController().navigate(AddBannerFragmentDirections.actionAddToLoginOrSignUp())
            }
        }

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
                binding.addTitle.text.toString().trim(),
                binding.addDescription.text.toString().trim(),
                binding.addPrice.text.toString().trim(),
                binding.addLocation.text.toString().trim(),
                binding.addCategory.selectedItemPosition + 1,
                binding.addSellOrRent.selectedItemPosition + 1,
                binding.addHomeSize.text.toString().toInt(),
                binding.addNumberOfRoom.text.toString().toInt(),
                postImage
            )
        }
    }

    private fun clearAllField() {
        binding.apply {
            addTitle.setText("")
            addDescription.setText("")
            addPrice.setText("")
            addLocation.setText("")
            addHomeSize.setText("")
            addNumberOfRoom.setText("")
            addCategory.setSelection(0)
            addSellOrRent.setSelection(0)
            addBannerImage.setImageResource(R.drawable.ic_add_photo)
        }
    }


    override fun onResume() {
        super.onResume()
        binding.addLocation.setText(LOCATION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val upload = UriToUploadable(requireActivity())
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val imageUri = data?.data
            binding.addBannerImage.setImageURI(imageUri)
            postImage = upload.getUploaderFile(imageUri, "image", "${UUID.randomUUID()}")
        }
    }

}