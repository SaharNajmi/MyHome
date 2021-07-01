package feature.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import feature.home.RentHomeFragment
import feature.home.SellHomeFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return SellHomeFragment()
            1 -> return RentHomeFragment()
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