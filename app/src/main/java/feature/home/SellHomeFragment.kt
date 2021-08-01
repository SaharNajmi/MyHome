package feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import common.EXTRA_KEY_DATA
import common.MyHomeFragment
import data.Banner
import data.CATEGORY
import feature.main.BannerDetailActivity
import feature.main.BannerViewModel
import feature.main.ShareViewModel
import kotlinx.android.synthetic.main.fragment_sell_home.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class SellHomeFragment : MyHomeFragment(), BannerOnClickListener {
    var SELL_OR_RENT = 1
    val bannerArrayList: BannerListAdapter by inject()

    private val bannerViewModel by viewModel<BannerViewModel>() {
        parametersOf(
            CATEGORY,
            SELL_OR_RENT
        )
    }

    //sharing data between Fragments with sharedViewModel
    private val shareViewModel by sharedViewModel<ShareViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sell_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //(search view) -> get value text search in another fragment use shareViewModel
        searchView()

        //get category in another fragment -> get Data between Fragments Using sharedViewModel
        chaneCategory()

        //setOnClickListener item recyclerView
        bannerArrayList.bannerOnClickListener = this

        //show banner in recyclerView
        recycler_view_sell.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view_sell.adapter = bannerArrayList

        bannerViewModel.bannerLiveData.observe(viewLifecycleOwner, object : Observer<List<Banner>> {
            override fun onChanged(t: List<Banner>?) {
                if (t!!.isNotEmpty()) {
                    bannerArrayList.banner = t as ArrayList<Banner>
                    Timber.i(t.toString())
                    recycler_view_sell.visibility=View.VISIBLE
                    emptyLayout.visibility = View.GONE

                    //ست کردن آرایه جدید بعد از هر بار جستجو
                    bannerArrayList.setData(t)


                } else {
                    emptyLayout.visibility = View.VISIBLE
                }
            }
        })

    }

    fun chaneCategory() {
        shareViewModel.getData().observe(requireActivity(),
            Observer<Int> {
                CATEGORY = it
                bannerViewModel.chaneCategory(CATEGORY)
            })
    }

    fun searchView() {
        shareViewModel.getDataSearch().observe(requireActivity(),
            Observer<String> {
                bannerArrayList.filter.filter(it!!)
            })
    }

    override fun onBannerClick(banner: Banner) {
        startActivity(Intent(requireContext(), BannerDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, banner)
        })
    }

    override fun onFavoriteBtnClick(banner: Banner) {
        bannerViewModel.addBannerToFavorite(banner)
    }

    override fun onResume() {
        super.onResume()
        bannerViewModel.refresh()
    }
}