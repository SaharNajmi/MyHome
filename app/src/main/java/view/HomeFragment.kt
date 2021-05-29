package view

import adapter.ViewPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myhome.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_category.*

class HomeFragment : Fragment(), View.OnClickListener {

    var cateOne = false
    var cateTwo = false
    var cateThree = false
    var cateFour = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Toast.makeText(requireContext(),cate_1.parent.toString(), Toast.LENGTH_LONG).show()

        /*-----------------------------tab layout-------------------------------------*/
        viewPagerShowBanner.adapter = ViewPagerAdapter(childFragmentManager)
        mainTab.setupWithViewPager(viewPagerShowBanner)

        /*---------------------------selection category using radio button-------------------*/
        radio_button_cate_1.setOnClickListener(this)
        radio_button_cate_2.setOnClickListener(this)
        radio_button_cate_3.setOnClickListener(this)
        radio_button_cate_4.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.radio_button_cate_1 -> {
                radio_button_cate_2.isChecked = false
                radio_button_cate_3.isChecked = false
                radio_button_cate_4.isChecked = false
            }
            R.id.radio_button_cate_2 -> {
                radio_button_cate_1.isChecked = false
                radio_button_cate_3.isChecked = false
                radio_button_cate_4.isChecked = false
            }
            R.id.radio_button_cate_3 -> {
                radio_button_cate_1.isChecked = false
                radio_button_cate_2.isChecked = false
                radio_button_cate_4.isChecked = false
            }
            R.id.radio_button_cate_4 -> {
                radio_button_cate_1.isChecked = false
                radio_button_cate_2.isChecked = false
                radio_button_cate_3.isChecked = false
            }
        }
    }
}