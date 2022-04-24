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
import com.example.myhome.services.ImageLoadingService
import com.example.myhome.services.UriToUploadable
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import okhttp3.MultipartBody
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class EditProfileFragment : MyHomeFragment() {
    private val userViewModel: UserViewModel by viewModel()
    private val imageLoadingService: ImageLoadingService by inject()
    private val pickImage = 100
    private var postImage: MultipartBody.Part? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: EditProfileFragmentArgs by navArgs()
        val user = args.UserDetail

        //set value
        username.setText(user.username)
        phone.setText(user.phone)
        if (user.image != "")
            imageLoadingService.load(
                user_image,
                "${Constants.BASE_URL}${user.image}"
            )
        else
            user_image.setImageResource(R.drawable.ic_add_photo)


        //load image in gallery
        user_image.setOnClickListener {
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
                        findNavController().navigate(EditProfileFragmentDirections.actionEditProfileToProfile())
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
        edit_profile_btn.setOnClickListener {
            if (user.password == old_password.text.toString().trim()
                && phone.text.toString().trim().isNotEmpty()
                && username.text.toString().trim().isNotEmpty()
            ) {
                userViewModel.editUser(
                    user.id.toString(),
                    phone.text.toString().trim(),
                    username.text.toString().trim(),
                    new_password.text.toString().trim(),
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
            user_image.setImageURI(imageUri)
            postImage = upload.getUploaderFile(imageUri, "image", "${UUID.randomUUID()}")
        }
    }

}