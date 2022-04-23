package com.example.myhome.feature.profile

import android.app.Activity
import android.app.AlertDialog
import android.app.AlertDialog.BUTTON_POSITIVE
import android.app.AlertDialog.Builder
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.myhome.R
import com.example.myhome.common.Constants.BASE_URL
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.common.Result
import com.example.myhome.common.showMessage
import com.example.myhome.services.ImageLoadingService
import com.example.myhome.services.UriToUploadable
import kotlinx.android.synthetic.main.dialog_user_edit.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class ProfileFragment : MyHomeFragment() {

    private val viewModel: UserViewModel by viewModel()
    private val imageLoadingService: ImageLoadingService by inject()
    private val pickImage = 100
    private var postImage: MultipartBody.Part? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get user info
        viewModel.user.observe(requireActivity()) { user ->
            prf_phone.text = user.phone
            prf_name.text = user.username

            if (user.image != "")
                imageLoadingService.load(prf_image, "${BASE_URL}${user.image}")
            else
                prf_image.setImageResource(R.drawable.ic_profile)
        }

        //edit user result
        viewModel.editUserResult.observe(requireActivity()) { result ->
            when (result) {
                is Result.Success -> {
                    setProgress(false)

                    if (result.data.state)
                        activity?.showMessage("پروفایل با موفقیت آپدیت شد")
                    else
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

    }

    private fun checkAuthState() {
        if (viewModel.isSignIn) {
            //visible items view
            edtBtn.visibility = View.VISIBLE
            myBannerBtn.visibility = View.VISIBLE
            prf_phone.visibility = View.VISIBLE

            //button sign out
            authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_out, 0)
            authBtn.text = getString(R.string.signOut)
            authBtn.setOnClickListener {
                viewModel.signOut()
                checkAuthState()
            }

            //edit user
            edtBtn.setOnClickListener {
                showDialogEditUser()
            }

            //go UserBanner
            myBannerBtn.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileToUserBannerFragment())
            }

        } else {
            //unVisible items view
            prf_image.setImageResource(R.drawable.ic_profile)
            prf_phone.visibility = View.GONE
            edtBtn.visibility = View.GONE
            myBannerBtn.visibility = View.GONE
            prf_name.text = getString(R.string.guest_user)

            //button sign in
            authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_in, 0)
            authBtn.text = getString(R.string.signIn)
            authBtn.setOnClickListener {
                viewModel.signOut()
                findNavController().navigate(ProfileFragmentDirections.actionProfileToLoginOrSignUp())
            }

        }
    }

    //edit user
    private fun showDialogEditUser() {
        val customLayout = layoutInflater.inflate(R.layout.dialog_user_edit, null)

        //create alert dialog
        val dialog: AlertDialog = Builder(requireContext())
            .setView(customLayout)
            .setPositiveButton("ثبت", null)
            .setNegativeButton("انصراف", null)
            .show()

        val positiveButton: Button = dialog.getButton(BUTTON_POSITIVE)

        //show old value in dialog box
        customLayout.username.setText(prf_name.text.toString())
        customLayout.phone.setText(prf_phone.text.toString())
        if (viewModel.user.value?.image != "")
            imageLoadingService.load(
                customLayout.edit_image,
                "${BASE_URL}${viewModel.user.value?.image}"
            )
        else
            customLayout.edit_image.setImageResource(R.drawable.ic_add_photo)

        //load image in gallery
        customLayout.edit_image.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        viewModel.selectedImageUri.observe(requireActivity()) { uri ->
            customLayout.edit_image.setImageURI(uri)
        }

        //edit user
        positiveButton.setOnClickListener {
            if (viewModel.user.value?.password == customLayout.old_password.text.toString().trim()
                && customLayout.phone.text.toString().trim().isNotEmpty()
                && customLayout.username.text.toString().trim().isNotEmpty()
            ) {
                viewModel.editUser(
                    RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        viewModel.user.value?.id.toString()
                    ),
                    RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        customLayout.phone.text.toString().trim()
                    ),
                    RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        customLayout.username.text.toString().trim()
                    ), RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        customLayout.new_password.text.toString().trim()
                    ),
                    postImage
                )
                dialog.dismiss()
            } else
                activity?.showMessage("اطلاعات ورودی اشتباه است")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val upload = UriToUploadable(requireActivity())
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            val imageUri = data?.data
            imageUri?.let { viewModel.setImageUri(it) }
            postImage = upload.getUploaderFile(imageUri, "image", "${UUID.randomUUID()}")
        }
    }

    override fun onResume() {
        super.onResume()
        checkAuthState()
    }
}