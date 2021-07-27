package feature.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import common.EXTRA_KEY_DATA
import data.Banner
import feature.main.BannerDetailActivity
import kotlinx.android.synthetic.main.fragment_favorite.*
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class FavoriteFragment : Fragment(), FavoriteListAdapter.FavoriteBannerClickListener {
    val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        favoriteViewModel.bannerLiveData.observe(viewLifecycleOwner) {
            rec_fav.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            rec_fav.adapter = FavoriteListAdapter(it as ArrayList<Banner>, get(), this)
            Timber.i(it.toString())
        }
    }

    override fun onClick(banner: Banner) {
        startActivity(Intent(requireContext(), BannerDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, banner)
        })
    }

    override fun deleteItemClick(banner: Banner) {
        favoriteViewModel.deleteFavorites(banner)
        //banner.isFavorite=false
        Toast.makeText(requireContext(), "آیتم از لیست علاقمندی حذف شد", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.refresh()
    }
}