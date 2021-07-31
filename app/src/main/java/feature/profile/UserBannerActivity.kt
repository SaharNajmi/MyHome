package feature.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import feature.main.BannerViewModel
import kotlinx.android.synthetic.main.activity_user_banner.*
import kotlinx.android.synthetic.main.layout_empty_view.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserBannerActivity : MyHomeActivity(), BannerOnClickListener {

    val userViewModel: UserViewModel by viewModel()
    val bannerArrayList: BannerListAdapter by inject()
    val bannerViewModel: BannerViewModel by viewModel { parametersOf(1, 1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_banner)

        //back button
        backBtn.setOnClickListener {
            finish()
        }

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
        bannerViewModel.addBannerToFavorite(banner)
    }

    fun getListUserBanner() {
        //show banner in recyclerView
        recycler_view_user_banner.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view_user_banner.adapter = bannerArrayList

        userViewModel.bannerLiveData.observe(this, object : Observer<List<Banner>> {
            override fun onChanged(t: List<Banner>?) {
                if (t!!.isNotEmpty()) {
                    bannerArrayList.banner = t as ArrayList<Banner>
                    recycler_view_user_banner.visibility = View.VISIBLE
                    emptyLayout.visibility = View.GONE
                } else {
                    emptyLayout.visibility = View.VISIBLE
                    emptyLayout.txtEmpty.text = getString(R.string.emptyListUserBanner)
                }
            }
        })
    }

    //when delete item -> refresh recycler view(with Get banner list again)
    override fun onStart() {
        super.onStart()
        userViewModel.refresh()
    }
}