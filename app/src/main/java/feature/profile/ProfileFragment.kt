package feature.profile

import android.app.Activity
import android.app.AlertDialog
import android.app.AlertDialog.BUTTON_POSITIVE
import android.app.AlertDialog.Builder
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.myhome.R
import common.BASE_URL
import common.MyHomeFragment
import common.MyHomeSingleObserver
import data.State
import data.UserInformation
import feature.login.LoginOrSignUpActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_edit.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import services.ImageLoadingService
import services.UriToUploadable
import java.util.*

class ProfileFragment : MyHomeFragment() {

    val viewModel: UserViewModel by viewModel()

    val imageLoadingService: ImageLoadingService by inject()
    val compositeDisposable = CompositeDisposable()
    lateinit var customLayout: View
    var userId: Int? = null
    lateinit var phone: String
    lateinit var username: String
    var image: String = ""
    var password: String = ""
    private val pickImage = 100
    private var imageUri: Uri? = null
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
    }

    private fun checkAuthState() {
        if (viewModel.isSignIn) {

            //visible view
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

            //get user banner
            myBannerBtn.setOnClickListener {
                startActivity(Intent(requireContext(), UserBannerActivity::class.java))
            }

            //get user
            viewModel.getUser(viewModel.phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyHomeSingleObserver<UserInformation>(compositeDisposable) {
                    override fun onSuccess(t: UserInformation) {
                        userId = t.id
                        image = t.image
                        password = t.password

                        //show value in view
                        prf_phone.text = t.phone
                        prf_name.text = t.username
                        if (image != "")
                            imageLoadingService.load(prf_image, "${BASE_URL}${image}")
                        else
                            prf_image.setImageResource(R.drawable.ic_profile)
                    }
                })

        } else {
            //un visible view
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
                startActivity(
                    Intent(
                        requireContext(),
                        LoginOrSignUpActivity::class.java
                    )
                )
            }

        }
    }

    //edit user
    private fun showDialogEditUser() {
        customLayout = layoutInflater.inflate(R.layout.dialog_edit, null)

        //create alert dialog
        val dialog: AlertDialog = Builder(requireContext())
            .setView(customLayout)
            .setPositiveButton("ثبت", null)
            .setNegativeButton("انصراف", null)
            .show()

        // prevent a dialog from closing when a button is clicked
        val positiveButton: Button = dialog.getButton(BUTTON_POSITIVE)

        //show old value in dialog box
        customLayout.username.setText(prf_name.text.toString())
        customLayout.phone.setText(prf_phone.text.toString())
        if (image != "")
            imageLoadingService.load(customLayout.edit_image, "${BASE_URL}${image}")
        else
            customLayout.edit_image.setImageResource(R.drawable.ic_add_photo)

        //load image in gallery
        customLayout.edit_image.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        //when click ok AlertDialog
        positiveButton.setOnClickListener {
            phone = customLayout.phone.text.toString()
            username = customLayout.username.text.toString()

            if (password == customLayout.old_password.text.toString()
                && phone != ""
                && username != ""
            ) {
                //Edit user when the input information is correct
                viewModel.editUser(
                    RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        userId.toString()
                    ),
                    RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        phone
                    ),
                    RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        username
                    ), RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        customLayout.new_password.text.toString()
                    ),
                    postImage
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : MyHomeSingleObserver<State>(compositeDisposable) {
                        override fun onSuccess(t: State) {
                            if (t.state) {
                                Toast.makeText(
                                    requireContext(),
                                    "پروفایل با موفقیت آپدیت شد",
                                    Toast.LENGTH_SHORT
                                ).show()
                                prf_name.text = username
                                prf_phone.text = phone
                                dialog.dismiss()
                            } else
                                Toast.makeText(
                                    requireContext(),
                                    "آپدیت با شکست مواجه شد!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                    })

            } else
                Toast.makeText(requireContext(), "اطاعات ورودی اشتباه است!!!", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val upload = UriToUploadable(requireActivity())
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            customLayout.edit_image.setImageURI(imageUri)
            postImage = upload.getUploaderFile(imageUri, "image", "${UUID.randomUUID()}")
        }
    }

    override fun onResume() {
        super.onResume()
        //در onResume موقع برگشت به اکتیویتی دوباره این تابع فراخوان میشه اما اگه در OnCreate باشه فقط اول برنامه ک صفحه لود بشه فراخوانی میشه
        checkAuthState()
    }
}