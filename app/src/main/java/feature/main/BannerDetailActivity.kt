package feature.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import com.example.myhome.R
import common.BASE_URL
import common.MyHomeActivity
import common.MyHomeSingleObserver
import data.Banner
import data.CATEGORY
import data.State
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
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import services.ImageLoadingService
import view.scroll.ObservableScrollViewCallbacks
import view.scroll.ScrollState

class BannerDetailActivity : MyHomeActivity() {

    val viewModel: UserViewModel by viewModel()
    val bannerViewModel: BannerViewModel by viewModel { parametersOf(CATEGORY) }
    val bannerDetailViewModel: BannerDetailViewModel by viewModel { parametersOf(intent.extras) }
    val imageLoadingService: ImageLoadingService by inject()
    val compositeDisposable = CompositeDisposable()
    var bannerId: Int? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_detail)

        //send value when clicked item recyclerView and Show detail values in next activity
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
        bannerDetailViewModel.bannerLiveData.observe(this,
            Observer<Banner> {
                imageLoadingService.load(image_detail_banner, it.bannerImage)
                imageLoadingService.load(image_profile, it.bannerImage)

                //save value in variable
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
                    favoriteBtn.visibility = View.GONE

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
                    favoriteBtn.visibility = View.VISIBLE
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
        //spinner select sell or rent
        spinnerSellOrRent()
        //spinner select category
        spinnerCategory()

        //create alert dialog
        AlertDialog.Builder(this)
            .setView(customLayout)
            .setNegativeButton("خیر", null)
            .setPositiveButton("بله") { dialogInterface, which ->

                /*val category: Int? = null*/
                if (image != "")
                    imageLoadingService.load(customLayout.edit_banner_image, "${BASE_URL}${image}")
                else
                    customLayout.edit_banner_image.setImageResource(R.drawable.ic_add_photo)

                title = customLayout.edit_title.text.toString()
                description = customLayout.edit_description.text.toString()
                price = customLayout.edit_price.text.toString()
                homeSize = customLayout.edit_home_size.text.toString().toInt()
                numberOfRoom = customLayout.edit_number_of_room.text.toString().toInt()

                Toast.makeText(this, homeSize.toString(), Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    //spinner sell or rent
    private fun spinnerSellOrRent() {
        val sp = customLayout.edit_sell_or_rent
        //show cate base
        val adapterCate = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, arrayOf("فروش", "اجاره")
        )
        //view dropdown
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp.adapter = adapterCate

        //item default spinner cate base
        sp.setSelection(sellOrRent!! - 1)

        //Show subcategories when change baseCategory
        sp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    sellOrRent = sp.selectedItemPosition + 1
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {
                }
            }
    }

    //spinner category
    private fun spinnerCategory() {
        val sp = customLayout.edit_category
        //show cate base
        val adapterCate = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, resources.getStringArray(R.array.array_category)
        )

        //view dropdown
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp.adapter = adapterCate

        //item default spinner cate base
        sp.setSelection(category!! - 1)

        //Show subcategories when change baseCategory
        sp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    category = sp.selectedItemPosition + 1
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {
                }
            }
    }

}
