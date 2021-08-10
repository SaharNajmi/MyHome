package feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.myhome.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import feature.adapter.ViewPagerAdapter
import feature.main.ShareViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_category.*
import kotlinx.android.synthetic.main.layout_search_view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class HomeFragment : Fragment(), View.OnClickListener {

    private val shareViewModel by sharedViewModel<ShareViewModel>()
    var price = "all"
    var homeSize = 0
    var numberOfRooms = 0
    var saveStatePrice = 0
    var saveStateHomeSize = 0

    lateinit var seekBarPrice: SeekBar
    lateinit var seekBarHomeSize: SeekBar
    lateinit var radioGroup: RadioGroup
    lateinit var radioButton1: RadioButton
    lateinit var radioButton2: RadioButton
    lateinit var radioButton3: RadioButton
    lateinit var radioButton4: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //filter list
        filter_list.setOnClickListener {
            showBottomSheet()
        }

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
                shareViewModel.setDataCategory(1)
                radio_button_cate_2.isChecked = false
                radio_button_cate_3.isChecked = false
                radio_button_cate_4.isChecked = false
            }
            R.id.radio_button_cate_2 -> {
                shareViewModel.setDataCategory(2)
                radio_button_cate_1.isChecked = false
                radio_button_cate_3.isChecked = false
                radio_button_cate_4.isChecked = false
            }
            R.id.radio_button_cate_3 -> {
                shareViewModel.setDataCategory(3)
                radio_button_cate_1.isChecked = false
                radio_button_cate_2.isChecked = false
                radio_button_cate_4.isChecked = false
            }
            R.id.radio_button_cate_4 -> {
                shareViewModel.setDataCategory(4)
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

    fun showBottomSheet() {
        //create a new bottom sheet dialog
        val dialog = BottomSheetDialog(requireContext())

        //inflating layout
        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet, null)
        val btnClose = view.findViewById<Button>(R.id.btnDismissFilter)
        val btnApply = view.findViewById<Button>(R.id.btnApplyFilter)
        seekBarHomeSize = view!!.findViewById<SeekBar>(R.id.seekBarHomeSize)
        seekBarPrice = view.findViewById<SeekBar>(R.id.seekBarPrice)
        radioGroup = view.findViewById(R.id.radio_group_room) as RadioGroup
        radioButton1 = view.findViewById(R.id.btn_any) as RadioButton
        radioButton2 = view.findViewById(R.id.btn_one) as RadioButton
        radioButton3 = view.findViewById(R.id.btn_two) as RadioButton
        radioButton4 = view.findViewById(R.id.btn_more) as RadioButton

        //Show old values inside BottomSheet
        saveOldValue()

        // seekBar HomeSize and Price
        seekBar()

        // Apply changes the dialog button
        btnApply.setOnClickListener {

            //RadioButton item selected numberOfRooms
            checkedRadioButton()

            //send Data when click btn apply filter list to sell and rent fragment
            shareViewModel.setDataFilter(price, numberOfRooms, homeSize)

            dialog.dismiss()
        }

        //dismissing the dialog button
        btnClose.setOnClickListener {
            //delete value filter list
            saveStatePrice = 0
            saveStateHomeSize = 0
            price = "all"
            homeSize = 0
            numberOfRooms = 0

            shareViewModel.setDataFilter(price, numberOfRooms, homeSize)

            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun checkedRadioButton() {
        //RadioButton item selected numberOfRooms
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId

        val radioButton: View = radioGroup.findViewById(selectedRadioButtonId)
        val indexRadioButtonItemSelected: Int = radioGroup.indexOfChild(radioButton)
        when (indexRadioButtonItemSelected) {
            0 -> numberOfRooms = 0
            1 -> numberOfRooms = 1
            2 -> numberOfRooms = 2
            3 -> numberOfRooms = 3
        }
    }

    private fun seekBar() {
        // seekBar HomeSize
        seekBarHomeSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                when (seekBar?.progress) {
                    0 -> {
                        homeSize = 0
                        saveStateHomeSize = 0
                    }
                    1 -> {
                        homeSize = 100
                        saveStateHomeSize = 1
                    }
                    2 -> {
                        homeSize = 250
                        saveStateHomeSize = 2
                    }
                    3 -> {
                        homeSize = 251
                        saveStateHomeSize = 3
                    }
                }
            }
        })

        //seekBar Price
        seekBarPrice.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                when (seekBar?.progress) {
                    0 -> {
                        price = "all"
                        saveStatePrice = 0
                    }
                    1 -> {
                        price = "500000000"
                        saveStatePrice = 1
                    }
                    2 -> {
                        price = "1000000000"
                        saveStatePrice = 2
                    }
                    3 -> {
                        price = "1000000001"
                        saveStatePrice = 3
                    }
                }
            }
        })
    }

    fun saveOldValue() {
        seekBarHomeSize.progress = saveStateHomeSize
        seekBarPrice.progress = saveStatePrice
        when (numberOfRooms) {
            0 -> radioButton1.isChecked = true

            1 -> radioButton2.isChecked = true

            2 -> radioButton3.isChecked = true

            3 -> radioButton4.isChecked = true
        }
    }
}
