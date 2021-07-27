package feature.profile

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import common.EXTRA_KEY_DATA
import common.MyHomeActivity
import data.Banner
import feature.home.BannerListAdapter
import feature.home.BannerOnClickListener
import feature.main.BannerDetailActivity
import kotlinx.android.synthetic.main.activity_user_banner.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class UserBannerActivity : MyHomeActivity(), BannerOnClickListener {

    val userViewModel: UserViewModel by viewModel()
    val bannerArrayList: BannerListAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_banner)

        //setOnClickListener item recyclerView
        bannerArrayList.bannerOnClickListener = this

        //get banners
        getListUserBanner()
    }

    override fun onBannerClick(banner: Banner) {
        startActivity(Intent(this, BannerDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, banner)
        })
    }

    override fun onFavoriteBtnClick(banner: Banner) {
        TODO("Not yet implemented")
    }

    fun getListUserBanner() {
        //show banner in recyclerView
        recycler_view_user_banner.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view_user_banner.adapter = bannerArrayList

        userViewModel.bannerLiveData.observe(this, object : Observer<List<Banner>> {
            override fun onChanged(t: List<Banner>?) {
                bannerArrayList.banner = t as ArrayList<Banner>
            }
        })
    }

    //when delete item -> refresh recycler view(with Get banner list again)
    override fun onStart() {
        super.onStart()
        userViewModel.refresh()
    }
}