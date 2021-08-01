package feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.myhome.R
import feature.adapter.ViewPagerAdapter
import feature.main.ShareViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_category.*
import kotlinx.android.synthetic.main.layout_search_view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment(), View.OnClickListener {

    private val shareViewModel by sharedViewModel<ShareViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*-----------------------------tab layout-------------------------------------*/
        viewPagerShowBanner.adapter = ViewPagerAdapter(childFragmentManager)
        mainTab.setupWithViewPager(viewPagerShowBanner)

        /*---------------------------selection category using radio button-------------------*/
        radio_button_cate_1.setOnClickListener(this)
        radio_button_cate_2.setOnClickListener(this)
        radio_button_cate_3.setOnClickListener(this)
        radio_button_cate_4.setOnClickListener(this)
        radio_button_cate_1.isChecked = true

        //search view use share view model
        sendValueSearch()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.radio_button_cate_1 -> {
                //send Data between Fragments
                shareViewModel.setData(1)
                radio_button_cate_2.isChecked = false
                radio_button_cate_3.isChecked = false
                radio_button_cate_4.isChecked = false
            }
            R.id.radio_button_cate_2 -> {
                shareViewModel.setData(2)
                radio_button_cate_1.isChecked = false
                radio_button_cate_3.isChecked = false
                radio_button_cate_4.isChecked = false
            }
            R.id.radio_button_cate_3 -> {
                shareViewModel.setData(3)
                radio_button_cate_1.isChecked = false
                radio_button_cate_2.isChecked = false
                radio_button_cate_4.isChecked = false
            }
            R.id.radio_button_cate_4 -> {
                shareViewModel.setData(4)
                radio_button_cate_1.isChecked = false
                radio_button_cate_2.isChecked = false
                radio_button_cate_3.isChecked = false
            }
        }
    }

    fun sendValueSearch() {
        search_view.queryHint = "جستجو"
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Send text search to another fragment
                shareViewModel.setDataSearch(newText!!)
                return true
            }
        })
    }
}