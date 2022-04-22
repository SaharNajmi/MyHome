package com.example.myhome.feature.login

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.myhome.R
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.common.Result
import com.example.myhome.common.showMessage
import com.example.myhome.feature.main.MainActivity
import com.example.myhome.services.UriToUploadable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_sign_up.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class SignUpFragment : MyHomeFragment() {

    private val viewModel: AuthViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()
    private val pickImage = 100
    private var postImage: MultipartBody.Part? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //go to fragment login
        loginLinkBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, LoginFragment())
            }.commit()
        }

        //load image in gallery
        img_add.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        // sign up
        signUpBtn.setOnClickListener {
            if (phoneEt.text.toString().trim().isNotEmpty() &&
                usernameEt.text.toString().trim().isNotEmpty() &&
                passwordEt.text.toString().trim().isNotEmpty()
            )
                viewModel.signUp(
                    RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        phoneEt.text.toString()
                    ),
                    RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        usernameEt.text.toString()
                    ),
                    RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        passwordEt.text.toString()
                    ),
                    postImage
                )
            else
                context?.showMessage("لطفا تمامی فیلدها را پر کنید")
        }

        viewModel.signUpResult.observe(requireActivity()) { result ->
            when (result) {
                is Result.Success -> {
                    setProgress(false)

                    if (result.data.state) {
                        activity?.showMessage("ثبت نام با موفقیت انجام شد")
                        findNavController().navigate(LoginOrSignUpFragmentDirections.actionLoginOrSignUpToProfile())
                    } else
                        activity?.showMessage("ثبت نام با شکست مواجه شد!!")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val upload = UriToUploadable(requireActivity())
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            val imageUri = data?.data
            img_add.setImageURI(imageUri)
            postImage = upload.getUploaderFile(imageUri, "image", "${UUID.randomUUID()}")
        }
    }

    override fun onResume() {
        super.onResume()
        //hideBottom Navigation
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigation()
        }
    }
}