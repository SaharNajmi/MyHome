package com.example.myhome.feature.profile

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
import com.example.myhome.common.Constants
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.common.Result
import com.example.myhome.common.showMessage
import com.example.myhome.databinding.FragmentEditProfileBinding
import com.example.myhome.feature.main.MainActivity
import com.example.myhome.services.ImageLoadingService
import com.example.myhome.services.UriToUploadable
import okhttp3.MultipartBody
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class EditProfileFragment : MyHomeFragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val userViewModel: UserViewModel by viewModel()
    private val imageLoadingService: ImageLoadingService by inject()
    private val pickImage = 100
    private var postImage: MultipartBody.Part? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: EditProfileFragmentArgs by navArgs()
        val user = args.UserDetail

        //hideBottom Navigation
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigation()
        }

        //set value
        binding.username.setText(user.username)
        binding.phone.setText(user.phone)
        if (user.image != "")
            imageLoadingService.load(
                binding.userImage,
                "${Constants.BASE_URL}${user.image}"
            )
        else
            binding.userImage.setImageResource(R.drawable.ic_add_photo)


        //load image in gallery
        binding.userImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        //edit user result
        userViewModel.editUserResult.observe(requireActivity()) { result ->
            when (result) {
                is Result.Success -> {
                    setProgress(false)
                    if (result.data.state) {
                        activity?.showMessage("پروفایل با موفقیت آپدیت شد")
                        findNavController().popBackStack()
                    } else
                        activity?.showMessage("آپدیت پروفایل با شکست مواجه شد")
                }
                is Result.Loading -> {
                    setProgress(true)
                }

                is Result.Error -> {
                    setProgress(false)
                    context?.showMessage("مشکل در اتصال به اینترنت")
                }
            }
        }

        //click button edit profile
        binding.editProfileBtn.setOnClickListener {
            if (user.password == binding.oldPassword.text.toString().trim()
                && binding.phone.text.toString().trim().isNotEmpty()
                && binding.username.text.toString().trim().isNotEmpty()
            ) {
                userViewModel.editUser(
                    user.id.toString(),
                    binding.phone.text.toString().trim(),
                    binding.username.text.toString().trim(),
                    binding.newPassword.text.toString().trim(),
                    postImage
                )
            } else
                activity?.showMessage("اطلاعات ورودی اشتباه است")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val upload = UriToUploadable(requireActivity())
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            val imageUri = data?.data
            binding.userImage.setImageURI(imageUri)
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