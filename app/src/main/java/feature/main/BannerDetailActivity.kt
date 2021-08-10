package feature.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.example.myhome.R
import common.BASE_URL
import common.MyHomeActivity
import common.MyHomeSingleObserver
import common.REQUEST_CODE
import data.Banner
import data.CATEGORY
import data.SELL_OR_RENT
import data.State
import feature.favorite.FavoriteViewModel
import feature.profile.UserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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
import services.ImageLoadingService
import services.UriToUploadable
import view.scroll.ObservableScrollViewCallbacks
import view.scroll.ScrollState
import java.util.*

class BannerDetailActivity : MyHomeActivity() {

    val viewModel: UserViewModel by viewModel()
    val bannerViewModel: BannerViewModel by viewModel {
        parametersOf(
            CATEGORY,
            SELL_OR_RENT,
            "all",
            0,
            0
        )
    }
    val favoriteViewModel: FavoriteViewModel by inject()
    val bannerDetailViewModel: BannerDetailViewModel by viewModel { parametersOf(intent.extras) }
    val imageLoadingService: ImageLoadingService by inject()
    val compositeDisposable = CompositeDisposable()
    var bannerId: Int? = null
    var userId: Int? = null
    lateinit var customLayout: View
    var location = ""
    var title = ""
    var description = ""
    var price = ""
    var category: Int? = null
    var sellOrRent: Int? = null
    var homeSize: Int? = null
    var numberOfRoom: Int? = null
    var image: String = ""
    private var postImage: MultipartBody.Part? = null
    private var imageUri: Uri? = null
    var favorite: Boolean = false

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
        //یعنی تمام کارهای یو انجام و محاسبه شده
        image_detail_banner.post {
            val imageHeight = image_detail_banner.height

            //ای دی را داخل یه متغیر میزاریم که هر بار نره findviewbyid را کال کنه
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

                    //سرعت بالا رفتن عکس نصف سرعت اسکرول خودمون باشه
                    bannerImageView.translationY = scrollY.toFloat() / 2

                }

                override fun onDownMotionEvent() {
                }

                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
                }

            })
        }
    }

    fun detailBanner() {
        bannerDetailViewModel.bannerLiveData.observe(this) {

        }
        bannerDetailViewModel.bannerLiveData.observe(this,
            Observer<Banner> {
                imageLoadingService.load(image_detail_banner, "$BASE_URL${it.bannerImage}")

                //favorite
                favorite = it.fav
                checkFavorite(favorite)

                val favbanner = it
                favoriteBtnDetailBanner.setOnClickListener {
                    if (favorite) {
                        favoriteBtnDetailBanner.setImageResource(R.drawable.ic_not_bookmarked)
                        favoriteViewModel.deleteFavorites(favbanner)
                        favorite = false

                    } else {
                        favoriteBtnDetailBanner.setImageResource(R.drawable.ic_bookmarked)
                        favoriteViewModel.addFavorites(favbanner)
                        favorite = true
                    }
                }

                //save value in variable
                userId = it.userID
                bannerId = it.id
                location = it.location
                title = it.title
                description = it.description
                price = it.price
                category = it.category
                sellOrRent = it.sellOrRent
                homeSize = it.homeSize
                numberOfRoom = it.numberOfRooms
                image = it.bannerImage

                //show value in view
                title_banner_show.text = it.title
                txt_location_show.text = it.location
                txt_number_of_rooms_show.text = it.numberOfRooms.toString()
                txt_home_size_show.text = it.homeSize.toString()
                price_banner_show.text = "قیمت: ${it.price} تومان "
                title_banner_hide.text = it.title
                txt_location_hide.text = it.location
                txt_number_of_rooms_hide.text = it.numberOfRooms.toString()
                txt_home_size_hide.text = it.homeSize.toString()
                price_banner_hide.text = "قیمت: ${it.price} تومان "
                val phoneUser = it.phone
                name_profile.text = it.username
                imageLoadingService.load(image_profile, "$BASE_URL${it.userImage}")

                //call with user
                user_phone.setOnClickListener {
                    Toast.makeText(this, phoneUser, Toast.LENGTH_SHORT).show()
                }

                /* اگه شماره موبایل این آگهی با شماره موبایل کاربری که لاگین کرده یکی  باشه یعنی
                 این آگهی مال اونه پس باید بتونه ویرایش و حذف را انجام دهد*/
                if (viewModel.phoneNumber == phoneUser) {
                    layout_edit_or_delete.visibility = View.VISIBLE
                    //favoriteBtnDetailBanner.visibility = View.GONE

                    //edit banner user
                    edit_banner.setOnClickListener {
                        editBanner()
                    }
                    //delete banner user
                    bannerId = it.id

                    delete_banner.setOnClickListener {
                        deleteBanner()
                    }

                } else {
                    layout_contact_us.visibility = View.VISIBLE
                    //  favoriteBtnDetailBanner.visibility = View.VISIBLE
                }
            })
    }

    fun deleteBanner() {
        //create alert dialog
        AlertDialog.Builder(this)
            .setTitle("حذف آگهی")
            .setMessage("آیا میخواهید این آگهی را حذف کنید؟")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setNegativeButton("خیر", null)
            .setPositiveButton("بله") { dialogInterface, which ->
                bannerViewModel.deleteBanner(bannerId!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object :
                        MyHomeSingleObserver<State>(compositeDisposable) {
                        override fun onSuccess(t: State) {
                            if (t.state) {
                                this@BannerDetailActivity.finish()
                                Toast.makeText(
                                    this@BannerDetailActivity,
                                    "آگهی مورد نظر حذف شد ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else
                                Toast.makeText(
                                    this@BannerDetailActivity,
                                    "حذف با شکست مواجه شد!!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                    })
            }
            .show()
    }

    fun editBanner() {
        customLayout = layoutInflater.inflate(R.layout.dialog_banner_edit, null)

        //show old value in dialog box
        customLayout.edit_title.setText(title)
        customLayout.edit_location.setText(location)
        customLayout.edit_price.setText(price)
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
        spinnerSellOrRent()
        //spinner select category
        spinnerCategory()

        //create alert dialog
        AlertDialog.Builder(this)
            .setView(customLayout)
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
                    ), RequestBody.create(
                        okhttp3.MultipartBody.FORM,
                        location
                    ),
                    category!!,
                    sellOrRent!!,
                    homeSize!!,
                    numberOfRoom!!,
                    postImage
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
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

    private fun spinnerSellOrRent() {
        val sp = customLayout.edit_sell_or_rent
        val adapterCate = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, arrayOf("فروش", "اجاره")
        )
        //view dropdown
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

    private fun spinnerCategory() {
        val sp = customLayout.edit_category
        val adapterCate = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, resources.getStringArray(R.array.array_category)
        )
        //view dropdown
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

    fun checkFavorite(favorite: Boolean) {
        if (favorite)
            favoriteBtnDetailBanner.setImageResource(R.drawable.ic_bookmarked)
        else
            favoriteBtnDetailBanner.setImageResource(R.drawable.ic_not_bookmarked)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val upload = UriToUploadable(this)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            imageUri = data?.data
            customLayout.edit_banner_image.setImageURI(imageUri)
            postImage = upload.getUploaderFile(imageUri, "image", "${UUID.randomUUID()}")
        }
    }
}
