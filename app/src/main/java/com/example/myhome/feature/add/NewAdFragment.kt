package com.example.myhome.feature.add

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myhome.R
import com.example.myhome.common.MyHomeSingleObserver
import com.example.myhome.common.REQUEST_CODE
import com.example.myhome.data.*
import com.example.myhome.feature.login.LoginOrSignUpActivity
import com.example.myhome.feature.main.BannerViewModel
import com.example.myhome.feature.profile.UserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_new_ad.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import com.example.myhome.services.UriToUploadable
import java.util.*


class NewAdFragment : Fragment() {
    val bannerViewModel: BannerViewModel by viewModel {
        parametersOf(
            CATEGORY,
            SELL_OR_RENT,
            "all",
            0,
            0
        )
    }
    val userViewModel: UserViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()

    var userId: Int? = null
    var category = 0
    var sellOrRent = 0
    var homeSize: Int? = null
    var numberOfRoom: Int? = null
    var location = ""
    var title = ""
    var description = ""
    var price = ""
    var image: String = ""
    private var postImage: MultipartBody.Part? = null
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_ad, container, false)
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
                    MyMapActivity::class.java
                )
            )
        }
    }

    fun getUserId() {
        userViewModel.getUser(userViewModel.phoneNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MyHomeSingleObserver<UserInformation>(compositeDisposable) {
                override fun onSuccess(t: UserInformation) {
                    userId = t.id
                }
            })
    }

    fun addBanner() {
        //save new value in variable
        title = add_title.text.toString()
        description = add_description.text.toString()
        price = add_price.text.toString()
        location = add_location.text.toString()
        homeSize = add_home_size.text.toString().toInt()
        numberOfRoom = add_number_of_room.text.toString().toInt()


        bannerViewModel.addBanner(
            userId!!,
            RequestBody.create(
                okhttp3.MultipartBody.FORM,
                title
            ),
            RequestBody.create(
                okhttp3.MultipartBody.FORM,
                description
            ),
            RequestBody.create(
                okhttp3.MultipartBody.FORM,
                price
            ),
            RequestBody.create(
                okhttp3.MultipartBody.FORM,
                location
            ),
            category,
            sellOrRent,
            homeSize!!,
            numberOfRoom!!,
            postImage
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MyHomeSingleObserver<State>(compositeDisposable) {
                override fun onSuccess(t: State) {
                    if (t.state) {
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

                        Toast.makeText(
                            requireContext(),
                            "آگهی مورد نظر ثبت شد و در انتظار بررسی است!!!",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "ثبت آگهی با مشکل مواجه شد",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    fun clearAllField() {
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

    private fun spinnerSellOrRent() {
        val sp = add_sell_or_rent
        val adapterCate = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, arrayOf("فروش", "اجاره")
        )
        //com.example.myhome.view dropdown
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp.adapter = adapterCate

        //item default spinner
        sp.setSelection(0)

        sp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sellOrRent = sp.selectedItemPosition + 1
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {
                }
            }
    }

    private fun spinnerCategory() {
        var sp = add_category
        val adapterCate = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.array_category)
        )
        //com.example.myhome.view dropdown
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp.adapter = adapterCate

        //item default spinner
        sp.setSelection(0)

        //Show subcategories when change baseCategory
        sp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    category = sp.selectedItemPosition + 1
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }


    }

    private fun checkAuthStateForAddBanner() {
        //اگر کاربر وارد حساب کاربری نشه بود، قبل از ثبت آگهی باید وارد حساب خود شود
        if (userViewModel.isSignIn) {
            //show com.example.myhome.view
            showLayoutAdd.visibility = View.VISIBLE
            authBtn.visibility = View.GONE
            txtAlert.visibility = View.GONE

            //spinner select sell or rent
            spinnerSellOrRent()

            //spinner select category
            spinnerCategory()

            //user id
            getUserId()

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
                startActivity(
                    Intent(
                        requireContext(),
                        LoginOrSignUpActivity::class.java
                    )
                )
            }
        }

    }

    override fun onResume() {
        super.onResume()
        checkAuthStateForAddBanner()
        userViewModel.refresh()
        //موقعی که لوکیشن را از کاربر گرفتیم و به این فرگمنت برگشتیم مقدار را داخل کادر متنی موقعیت مکانی نشان دهد
        add_location.setText(LOCATION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val upload = UriToUploadable(requireActivity())
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            imageUri = data?.data
            add_banner_image.setImageURI(imageUri)
            postImage = upload.getUploaderFile(imageUri, "image", "${UUID.randomUUID()}")
        }
    }

}