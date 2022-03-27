package com.example.myhome.feature.favorite

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
import com.example.myhome.common.Constants.EXTRA_KEY_DATA
import com.example.myhome.data.Banner
import com.example.myhome.feature.main.BannerDetailActivity
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.layout_empty_view.view.*
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment(), FavoriteListAdapter.FavoriteBannerClickListener {
    private val favoriteViewModel: FavoriteViewModel by viewModel()

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

            if (it!!.isNotEmpty()) {
                rec_fav.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                rec_fav.adapter = FavoriteListAdapter(it as ArrayList<Banner>, get(), this)

                rec_fav.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE

            } else {
                emptyLayout.visibility = View.VISIBLE
                emptyLayout.txtEmpty.text = getString(R.string.emptyFavorite)
            }
        }
    }

    override fun onClick(banner: Banner) {
        startActivity(Intent(requireContext(), BannerDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, banner)
        })
    }

    override fun deleteItemClick(banner: Banner) {
        favoriteViewModel.deleteFavorites(banner)
        Toast.makeText(requireContext(), "آیتم از لیست علاقمندی حذف شد", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.refresh()
    }
}