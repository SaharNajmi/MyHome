package view

import adapter.BannerListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myhome.R
import data.Banner
import data.SELL_OR_RENT
import kotlinx.android.synthetic.main.fragment_rent_home.*
import main.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class RentHomeFragment : Fragment() {

    val mainViewModel: MainViewModel by viewModel()
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

        SELL_OR_RENT = 2

        //show banner in recyclerView
        recycler_view_rent.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler_view_rent.adapter = bannerArrayList

        mainViewModel.bannerLiveData.observe(viewLifecycleOwner){
            bannerArrayList.banner= it as ArrayList<Banner>
            Timber.i(it.toString())
        }
    }

}