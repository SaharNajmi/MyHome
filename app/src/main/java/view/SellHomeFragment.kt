package view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import com.example.myhome.R
import main.MainViewModel
import main.MyHomeFragment
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SellHomeFragment : MyHomeFragment() {
    //از ویو مدل کوین استفاده میکنیم
    val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sell_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      /*  mainViewModel.bannerLiveData.observe(viewLifecycleOwner){
            Timber.i(it.toString())
        }*/
    }
}