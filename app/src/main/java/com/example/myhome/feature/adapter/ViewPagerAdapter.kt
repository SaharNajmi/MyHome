package com.example.myhome.feature.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.myhome.feature.home.BannerListAdapter
import com.example.myhome.feature.home.RentHomeFragment
import com.example.myhome.feature.home.SellHomeFragment

class ViewPagerAdapter(
    fm: FragmentManager,
    private val sellListAdapter: BannerListAdapter,
    private val rentListAdapter: BannerListAdapter,
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return SellHomeFragment(sellListAdapter)
            1 -> return RentHomeFragment(rentListAdapter)
        }
        return null!!
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "خرید و فروش"
            1 -> return "اجاره"
        }
        return null!!

    }
}