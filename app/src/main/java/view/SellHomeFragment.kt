package view

import adapter.BannerListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import common.MyHomeFragment
import common.cate
import data.Banner
import data.CATEGORY
import data.SELL_OR_RENT
import kotlinx.android.synthetic.main.fragment_sell_home.*
import main.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SellHomeFragment : MyHomeFragment() {
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
       // Toast.makeText(requireContext(), cate(CATEGORY).toString(),Toast.LENGTH_LONG).show()

        recycler_view_sell.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view_sell.adapter = bannerArrayList

        mainViewModel.bannerLiveData.observe(viewLifecycleOwner) {
         bannerArrayList.banner = it as ArrayList<Banner>
           Timber.i(it.toString())
        }
    }
}