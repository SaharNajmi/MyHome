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
import com.example.myhome.databinding.FragmentSignUpBinding
import com.example.myhome.feature.main.MainActivity
import com.example.myhome.services.UriToUploadable
import okhttp3.MultipartBody
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class SignUpFragment : MyHomeFragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: AuthViewModel by viewModel()
    private val pickImage = 100
    private var postImage: MultipartBody.Part? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginLinkBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, LoginFragment())
            }.commit()
        }

        binding.imgAdd.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        binding.signUpBtn.setOnClickListener {
            if (binding.phoneEt.text.toString().trim().isNotEmpty() &&
                binding.usernameEt.text.toString().trim().isNotEmpty() &&
                binding.passwordEt.text.toString().trim().isNotEmpty()
            )
                viewModel.signUp(
                    binding.phoneEt.text.toString(),
                    binding.usernameEt.text.toString(),
                    binding.passwordEt.text.toString(),
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
            binding.imgAdd.setImageURI(imageUri)
            postImage = upload.getUploaderFile(imageUri, "image", "${UUID.randomUUID()}")
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigation()
        }
    }
}