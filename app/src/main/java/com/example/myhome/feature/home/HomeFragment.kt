package com.example.myhome.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.myhome.R
import com.example.myhome.databinding.FragmentHomeBinding
import com.example.myhome.databinding.LayoutBottomSheetBinding
import com.example.myhome.feature.adapter.ViewPagerAdapter
import com.example.myhome.feature.main.ShareViewModel
import com.example.myhome.services.ImageLoadingService
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var layoutBottomSheetBinding: LayoutBottomSheetBinding

    val imageLoad: ImageLoadingService by inject()
    private val sellBannerArrayList = BannerListAdapter(imageLoad)
    private val rentBannerArrayList = BannerListAdapter(imageLoad)


    private val shareViewModel by sharedViewModel<ShareViewModel>()
    private var price = "all"
    private var homeSize = 0
    private var numberOfRooms = 0
    private var saveStatePrice = 0
    private var saveStateHomeSize = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //filter list
        binding.filterList.setOnClickListener {
            showBottomSheet()
        }

        shareViewModel.category.observe(requireActivity()) {
            when (it) {
                1 -> {
                    binding.layoutItemCategory.radioButtonCate1.isChecked = true
                    binding.layoutItemCategory.radioButtonCate2.isChecked = false
                    binding.layoutItemCategory.radioButtonCate3.isChecked = false
                    binding.layoutItemCategory.radioButtonCate4.isChecked = false
                }
                2 -> {
                    binding.layoutItemCategory.radioButtonCate2.isChecked = true
                    binding.layoutItemCategory.radioButtonCate1.isChecked = false
                    binding.layoutItemCategory.radioButtonCate3.isChecked = false
                    binding.layoutItemCategory.radioButtonCate4.isChecked = false
                }
                3 -> {
                    binding.layoutItemCategory.radioButtonCate3.isChecked = true
                    binding.layoutItemCategory.radioButtonCate1.isChecked = false
                    binding.layoutItemCategory.radioButtonCate2.isChecked = false
                    binding.layoutItemCategory.radioButtonCate4.isChecked = false
                }
                4 -> {
                    binding.layoutItemCategory.radioButtonCate4.isChecked = true
                    binding.layoutItemCategory.radioButtonCate1.isChecked = false
                    binding.layoutItemCategory.radioButtonCate2.isChecked = false
                    binding.layoutItemCategory.radioButtonCate3.isChecked = false
                }
            }
        }

        //tab layout
        binding.viewPagerShowBanner.adapter = ViewPagerAdapter(
            childFragmentManager,
            sellBannerArrayList,
            rentBannerArrayList
        )
        binding.mainTab.setupWithViewPager(binding.viewPagerShowBanner)

        //selection category using radio button
        binding.layoutItemCategory.radioButtonCate1.setOnClickListener(this)
        binding.layoutItemCategory.radioButtonCate2.setOnClickListener(this)
        binding.layoutItemCategory.radioButtonCate3.setOnClickListener(this)
        binding.layoutItemCategory.radioButtonCate4.setOnClickListener(this)

        //Send text search to another fragments
        sendTextSearch()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.radio_button_cate_1 -> {
                //send Data between Fragments
                shareViewModel.changeCategory(1)
                binding.layoutItemCategory.radioButtonCate2.isChecked = false
                binding.layoutItemCategory.radioButtonCate3.isChecked = false
                binding.layoutItemCategory.radioButtonCate4.isChecked = false
            }
            R.id.radio_button_cate_2 -> {
                shareViewModel.changeCategory(2)
                binding.layoutItemCategory.radioButtonCate1.isChecked = false
                binding.layoutItemCategory.radioButtonCate3.isChecked = false
                binding.layoutItemCategory.radioButtonCate4.isChecked = false
            }
            R.id.radio_button_cate_3 -> {
                shareViewModel.changeCategory(3)
                binding.layoutItemCategory.radioButtonCate1.isChecked = false
                binding.layoutItemCategory.radioButtonCate2.isChecked = false
                binding.layoutItemCategory.radioButtonCate4.isChecked = false
            }
            R.id.radio_button_cate_4 -> {
                shareViewModel.changeCategory(4)
                binding.layoutItemCategory.radioButtonCate1.isChecked = false
                binding.layoutItemCategory.radioButtonCate2.isChecked = false
                binding.layoutItemCategory.radioButtonCate3.isChecked = false
            }
        }
    }

    private fun sendTextSearch() {
        binding.searchView.queryHint = "جستجو"
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                shareViewModel.changeTextSearch(newText!!)
                return true
            }
        })
    }

    private fun showBottomSheet() {
        //create a new bottom sheet dialog
        val dialog = BottomSheetDialog(requireContext())
        //inflating layout
        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet, null)
        layoutBottomSheetBinding =
            LayoutBottomSheetBinding.inflate(layoutInflater, view as ViewGroup, false)
        //Show old values inside BottomSheet
        saveOldValue()

        //seekBar HomeSize and Price
        seekBar()

        //Apply changes the dialog button
        layoutBottomSheetBinding.btnApplyFilter.setOnClickListener {
            //RadioButton item selected numberOfRooms
            checkedRadioButton()

            //send Data when click btn apply filter list to sell and rent fragment
            shareViewModel.setDataFilter(price, numberOfRooms, homeSize)

            dialog.dismiss()
        }

        //dismissing the dialog button
        layoutBottomSheetBinding.btnDismissFilter.setOnClickListener {
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
        dialog.setContentView(layoutBottomSheetBinding.root)
        dialog.show()
    }

    private fun checkedRadioButton() {
        //RadioButton item selected numberOfRooms
        val selectedRadioButtonId = layoutBottomSheetBinding.radioGroupRoom.checkedRadioButtonId

        val radioButton: View =
            layoutBottomSheetBinding.radioGroupRoom.findViewById(selectedRadioButtonId)
        when (layoutBottomSheetBinding.radioGroupRoom.indexOfChild(radioButton)) {
            0 -> numberOfRooms = 0
            1 -> numberOfRooms = 1
            2 -> numberOfRooms = 2
            3 -> numberOfRooms = 3
        }
    }

    private fun seekBar() {
        //seekBar HomeSize
        layoutBottomSheetBinding.seekBarHomeSize.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
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
        layoutBottomSheetBinding.seekBarPrice.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
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

    private fun saveOldValue() {
        layoutBottomSheetBinding.seekBarHomeSize.progress = saveStateHomeSize
        layoutBottomSheetBinding.seekBarPrice.progress = saveStatePrice
        when (numberOfRooms) {
            0 -> layoutBottomSheetBinding.btnAnyRoom.isChecked = true

            1 -> layoutBottomSheetBinding.btnOneRoom.isChecked = true

            2 -> layoutBottomSheetBinding.btnTwoRoom.isChecked = true

            3 -> layoutBottomSheetBinding.btnMoreRoom.isChecked = true
        }
    }
}
