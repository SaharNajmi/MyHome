package com.example.myhome.feature.main

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.lifecycle.observe
import com.example.myhome.R
import com.example.myhome.common.*
import com.example.myhome.common.Constants.BASE_URL
import com.example.myhome.common.Constants.CATEGORY
import com.example.myhome.common.Constants.REQUEST_CODE
import com.example.myhome.common.Constants.SELL_OR_RENT
import com.example.myhome.data.model.State
import com.example.myhome.feature.favorite.FavoriteViewModel
import com.example.myhome.feature.profile.UserViewModel
import com.example.myhome.services.ImageLoadingService
import com.example.myhome.services.UriToUploadable
import com.example.myhome.view.scroll.ObservableScrollViewCallbacks
import com.example.myhome.view.scroll.ScrollState
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_banner_detail.*
import kotlinx.android.synthetic.main.activity_banner_detail.view.*
import kotlinx.android.synthetic.main.dialog_banner_edit.*
import kotlinx.android.synthetic.main.dialog_banner_edit.view.*
import kotlinx.android.synthetic.main.dialog_user_edit.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.layout_edit_banner.*
import kotlinx.android.synthetic.main.layout_profile.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class BannerDetailActivity : MyHomeActivity() {

    private val viewModel: UserViewModel by viewModel()
    private val bannerViewModel: BannerViewModel by viewModel {
        parametersOf(
            CATEGORY,
            SELL_OR_RENT,
            "all",
            0,
            0
        )
    }
    private val favoriteViewModel: FavoriteViewModel by inject()
    private val bannerDetailViewModel: BannerDetailViewModel by viewModel { parametersOf(intent.extras) }
    private val imageLoadingService: ImageLoadingService by inject()
    private val compositeDisposable = CompositeDisposable()
    private var bannerId: Int? = null
    private var userId: Int? = null
    private var location = ""
    private var title = ""
    private var description = ""
    private var price = ""
    private var category: Int? = null
    private var sellOrRent: Int? = null
    private var homeSize: Int? = null
    private var numberOfRoom: Int? = null
    private var image: String = ""
    private var postImage: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_detail)

        //back button
        backBtn.setOnClickListener {
            finish()
        }

        //show detail banner
        detailBanner()

        //How to display image when scroll page
        image_detail_banner.post {
            val imageHeight = image_detail_banner.height

            val mainInfoShow = mainInfoShow
            val mainInfoHide = mainInfoHide

            val bannerImageView = image_detail_banner

            observableScrollView.addScrollViewCallbacks(object : ObservableScrollViewCallbacks {
                override fun onScrollChanged(
                    scrollY: Int,
                    firstScroll: Boolean,
                    dragging: Boolean
                ) {
                    if (scrollY > imageHeight - 80) {
                        mainInfoShow.alpha = 0f
                        mainInfoHide.alpha = 1f
                    } else {
                        mainInfoShow.alpha = 1f
                        mainInfoHide.alpha = 0f
                    }

                    //سرعت بالا رفتن عکس نصف سرعت اسکرول پیش فرض
                    bannerImageView.translationY = scrollY.toFloat() / 2

                }

                override fun onDownMotionEvent() {
                }

                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
                }

            })
        }

        bannerViewModel.deleteBannerResult.observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    setProgress(false)
                    showMessage("آگهی مورد نظر حذف شد ")
                    finish()
                }
                is Result.Loading -> {
                    setProgress(true)
                }
                is Result.Success -> {
                    setProgress(false)
                    showMessage("آگهی مورد نظر حذف شد ")
                    finish()
                }
            }
        }
    }

    private fun detailBanner() {
        bannerDetailViewModel.banner.observe(this) { banner ->
            imageLoadingService.load(image_detail_banner, "$BASE_URL${banner.bannerImage}")

            //check favorite
            checkFavorite(banner.fav)
            favoriteBtnDetailBanner.setOnClickListener {
                banner.fav = if (banner.fav) {
                    favoriteBtnDetailBanner.setImageResource(R.drawable.ic_not_bookmarked)
                    favoriteViewModel.deleteFavorites(banner)
                    false
                } else {
                    favoriteBtnDetailBanner.setImageResource(R.drawable.ic_bookmarked)
                    favoriteViewModel.addFavorites(banner)
                    true
                }
            }

            //save value in variable
            userId = banner.userID
            bannerId = banner.id
            location = banner.location
            title = banner.title
            description = banner.description
            price = banner.price
            category = banner.category
            sellOrRent = banner.sellOrRent
            homeSize = banner.homeSize
            numberOfRoom = banner.numberOfRooms
            image = banner.bannerImage

            //show value
            title_banner_show.text = banner.title
            desc_banner_show.text = banner.description
            txt_location_show.text = banner.location
            txt_number_of_rooms_show.text = banner.numberOfRooms.toString()
            txt_home_size_show.text = banner.homeSize.toString()
            price_banner_show.text = "قیمت: ${banner.price} تومان "
            title_banner_hide.text = banner.title
            txt_location_hide.text = banner.location
            txt_number_of_rooms_hide.text = banner.numberOfRooms.toString()
            txt_home_size_hide.text = banner.homeSize.toString()
            price_banner_hide.text = "قیمت: ${banner.price} تومان "
            val phoneUser = banner.phone
            name_profile.text = banner.username
            imageLoadingService.load(image_profile, "$BASE_URL${banner.userImage}")

            //call
            user_phone.setOnClickListener {
                Toast.makeText(this, phoneUser, Toast.LENGTH_SHORT).show()
            }

            if (viewModel.phoneNumber == phoneUser) {
                layout_edit_or_delete.visibility = View.VISIBLE
                //favoriteBtnDetailBanner.visibility = View.GONE

                //edit banner
                edit_banner.setOnClickListener {
                    editBanner()
                }

                //delete banner
                bannerId = banner.id

                delete_banner.setOnClickListener {
                    deleteBanner()
                }

            } else {
                layout_contact_us.visibility = View.VISIBLE
                //  favoriteBtnDetailBanner.visibility = View.VISIBLE
            }
        }
    }

    private fun deleteBanner() {
        AlertDialog.Builder(this)
            .setTitle("حذف آگهی")
            .setMessage("آیا میخواهید این آگهی را حذف کنید؟")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setNegativeButton("خیر", null)
            .setPositiveButton("بله") { _, _ -> bannerViewModel.deleteBanner(bannerId!!) }
            .show()
    }

    private fun editBanner() {
        val customLayout = layoutInflater.inflate(R.layout.dialog_banner_edit, null)

        //show old value in dialog box
        customLayout.edit_title.setText(title)
        customLayout.edit_location.setText(location)
        //delete ',' from price
        customLayout.edit_price.setText(price.filter(Char::isDigit))
        customLayout.edit_description.setText(description)
        customLayout.edit_home_size.setText(homeSize.toString())
        customLayout.edit_number_of_room.setText(numberOfRoom.toString())
        if (image != "")
            imageLoadingService.load(customLayout.edit_banner_image, "${BASE_URL}${image}")
        else
            customLayout.edit_banner_image.setImageResource(R.drawable.ic_add_photo)

        //load image in gallery
        customLayout.edit_banner_image.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, REQUEST_CODE)
        }

        //spinner select sell or rent
        addSpinnerSellOrRent(parentView = customLayout)
        //spinner select category
        addSpinnerCategory(parentView = customLayout)

        bannerViewModel.selectedImageUri.observe(this) { uri ->
            customLayout?.let {
                it.edit_banner_image.setImageURI(uri)
            }
        }

        //create alert dialog
        AlertDialog.Builder(this)
            .setView(customLayout)
            .setOnDismissListener {
                bannerViewModel.setImageUri(null)
            }
            .setNegativeButton("خیر", null)
            .setPositiveButton("بله") { dialogInterface, which ->

                title = customLayout.edit_title.text.toString()
                description = customLayout.edit_description.text.toString()
                price = customLayout.edit_price.text.toString()
                location = customLayout.edit_location.text.toString()
                homeSize = customLayout.edit_home_size.text.toString().toInt()
                numberOfRoom = customLayout.edit_number_of_room.text.toString().toInt()

                bannerViewModel.editBanner(
                    bannerId!!,
                    userId!!,
                    RequestBody.create(
                        MultipartBody.FORM,
                        title
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        description
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        price
                    ), RequestBody.create(
                        MultipartBody.FORM,
                        location
                    ),
                    category!!,
                    sellOrRent!!,
                    homeSize!!,
                    numberOfRoom!!,
                    postImage
                ).asyncNetworkRequest()
                    .subscribe(object : MyHomeSingleObserver<State>(compositeDisposable) {
                        override fun onSuccess(t: State) {
                            if (t.state) {

                                this@BannerDetailActivity.finish()

                                Toast.makeText(
                                    this@BannerDetailActivity,
                                    "آگهی مورد نظر با موفقیت آپدیت شد منتظر تایید آگهیتان باشید!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else
                                Toast.makeText(
                                    this@BannerDetailActivity,
                                    "آپدیت با شکست مواجه شد!!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                        }
                    })
            }
            .show()
    }

    private fun addSpinnerSellOrRent(parentView: View) {
        val sp = parentView.edit_sell_or_rent
        val adapterCate = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, arrayOf("فروش", "اجاره")
        )
        //set dropdown
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp.adapter = adapterCate

        //item default spinner
        sp.setSelection(sellOrRent!! - 1)

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

    private fun addSpinnerCategory(parentView: View) {
        val sp = parentView.edit_category
        val adapterCate = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, resources.getStringArray(R.array.array_category)
        )
        //set dropdown
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp.adapter = adapterCate

        //item default spinner
        sp.setSelection(category!! - 1)

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
                }
            }
    }

    private fun checkFavorite(favorite: Boolean) {
        if (favorite)
            favoriteBtnDetailBanner.setImageResource(R.drawable.ic_bookmarked)
        else
            favoriteBtnDetailBanner.setImageResource(R.drawable.ic_not_bookmarked)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val upload = UriToUploadable(this)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val imageUri = data?.data
            imageUri?.let { bannerViewModel.setImageUri(it) }
            postImage = upload.getUploaderFile(imageUri, "image", "${UUID.randomUUID()}")
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}
