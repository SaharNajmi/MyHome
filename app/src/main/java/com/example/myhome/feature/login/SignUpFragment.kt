package com.example.myhome.feature.login

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myhome.R
import com.example.myhome.common.MyHomeSingleObserver
import com.example.myhome.data.model.State
import com.example.myhome.services.UriToUploadable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_sign_up.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class SignUpFragment : Fragment() {

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

        //default image add user
        // img_add.setImageResource(R.drawable.ic_add_photo)

        // sign up
        signUpBtn.setOnClickListener {
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
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyHomeSingleObserver<State>(compositeDisposable) {
                    override fun onSuccess(t: State) {
                        if (t.state) {
                            Toast.makeText(
                                requireContext(),
                                "ثبت نام با موفقیت انجام شد",
                                Toast.LENGTH_SHORT
                            ).show()
                            requireActivity().finish()
                        } else
                            Toast.makeText(
                                requireContext(),
                                "ثبت نام با شکست مواجه شد!!",
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                })
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

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}