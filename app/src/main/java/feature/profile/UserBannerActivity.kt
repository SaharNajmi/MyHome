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
import data.SELL_OR_RENT
import feature.home.BannerListAdapter
import feature.home.BannerOnClickListener
import feature.main.BannerDetailActivity
import kotlinx.android.synthetic.main.activity_user_banner.*
import kotlinx.android.synthetic.main.fragment_sell_home.*
import kotlinx.android.synthetic.main.fragment_sell_home.recycler_view_sell
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class UserBannerActivity : MyHomeActivity(), BannerOnClickListener {

    //از ویو مدل کوین استفاده میکنیم
    val mainViewModel: UserViewModel by viewModel()
    val bannerArrayList: BannerListAdapter by inject()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_user_banner)

        SELL_OR_RENT = 1

        //setOnClickListener item recyclerView
        bannerArrayList.bannerOnClickListener = this

        //show banner in recyclerView
            recycler_view_user_banner.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            recycler_view_user_banner.adapter = bannerArrayList

        mainViewModel.bannerLiveData.observe(this, object : Observer<List<Banner>> {
            override fun onChanged(t: List<Banner>?) {
                bannerArrayList.banner = t as ArrayList<Banner>
                Timber.i(t.toString())
            }
        })
    }

    override fun onBannerClick(banner: Banner) {
        startActivity(Intent(this, BannerDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, banner)
        })
    }
}