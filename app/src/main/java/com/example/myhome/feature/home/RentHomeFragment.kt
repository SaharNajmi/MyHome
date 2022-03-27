package com.example.myhome.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import com.example.myhome.common.Constants.CATEGORY
import com.example.myhome.common.Constants.EXTRA_KEY_DATA
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.data.Banner
import com.example.myhome.feature.main.BannerDetailActivity
import com.example.myhome.feature.main.BannerViewModel
import com.example.myhome.feature.main.ShareViewModel
import kotlinx.android.synthetic.main.fragment_rent_home.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class RentHomeFragment : MyHomeFragment(), BannerListAdapter.BannerOnClickListener {
    private var SELL_OR_RENT = 2
    private val bannerViewModel: BannerViewModel by viewModel {
        parametersOf(
            CATEGORY,
            SELL_OR_RENT,
            "all",
            0,
            0
        )
    }
    private val shareViewModel by sharedViewModel<ShareViewModel>()


    val bannerArrayList: BannerListAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rent_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get value text search in another fragment
        searchView()

        //get category in another fragment -> get Data between Fragments
        chaneCategory()

        //filter banner list
        filterList()

        //setOnClickListener item recyclerView
        bannerArrayList.bannerOnClickListener = this

        //show banner in recyclerView
        recycler_view_rent.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view_rent.adapter = bannerArrayList

        bannerViewModel.bannerLiveData.observe(viewLifecycleOwner, object : Observer<List<Banner>> {
            override fun onChanged(t: List<Banner>?) {
                if (t!!.isNotEmpty()) {
                    bannerArrayList.banner = t as ArrayList<Banner>
                    Timber.i(t.toString())
                    recycler_view_rent.visibility = View.VISIBLE
                    emptyLayout.visibility = View.GONE

                    //update list
                    bannerArrayList.setData(t)

                } else {
                    emptyLayout.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun chaneCategory() {
        shareViewModel.getDataCategory().observe(requireActivity(),
            Observer<Int> {
                CATEGORY = it
                bannerViewModel.chaneCategory(CATEGORY)
            })
    }

    private fun searchView() {
        shareViewModel.getDataSearch().observe(requireActivity(),
            Observer<String> {
                bannerArrayList.filter.filter(it!!)
            })
    }

    private fun filterList() {
        shareViewModel.getDataFilter().observe(requireActivity(), Observer<ArrayList<Any>> {
            bannerViewModel.filter(it[0] as String, it[1] as Int, it[2] as Int)
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