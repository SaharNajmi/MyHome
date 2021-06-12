package view

import adapter.BannerListAdapter
import adapter.BannerOnClickListener
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import common.EXTRA_KEY_DATA
import common.MyHomeFragment
import data.Banner
import kotlinx.android.synthetic.main.fragment_sell_home.*
import main.BannerDetailActivity
import main.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SellHomeFragment : MyHomeFragment(), BannerOnClickListener {
    //از ویو مدل کوین استفاده میکنیم
    val mainViewModel: MainViewModel by viewModel()
    val bannerArrayList: BannerListAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sell_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setOnClickListener item recyclerView
        bannerArrayList.bannerOnClickListener = this
        //show banner in recyclerView
        recycler_view_sell.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view_sell.adapter = bannerArrayList

        mainViewModel.bannerLiveData.observe(viewLifecycleOwner) {
            bannerArrayList.banner = it as ArrayList<Banner>
            Timber.i(it.toString())
            bannerArrayList.notifyDataSetChanged()
        }
    }

    override fun onBannerClick(banner: Banner) {
        startActivity(Intent(requireContext(), BannerDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, banner)
        })
    }
}