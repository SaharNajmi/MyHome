package com.example.myhome.feature.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.common.showMessage
import com.example.myhome.data.model.Banner
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteFragment : MyHomeFragment(), FavoriteListAdapter.FavoriteBannerClickListener {
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

        ////show all banners
        getBanners()
    }

    private fun getBanners() {
        favoriteViewModel.banners.observe(viewLifecycleOwner) { banners ->
            if (banners.isEmpty()) {
                //show empty layout
                showEmptyState(true)
                txtEmpty.text = getString(R.string.emptyFavorite)
            } else {
                rec_fav.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                rec_fav.adapter = FavoriteListAdapter(banners as ArrayList<Banner>, get(), this)
                showEmptyState(false)
            }
        }
    }

    override fun onClick(banner: Banner) {
        findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteToBannerDetailFragment(
                banner
            )
        )
    }

    override fun deleteItemClick(banner: Banner) {
        favoriteViewModel.deleteFavorites(banner)
        activity?.showMessage("آیتم از لیست علاقمندی حذف شد")
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.getFavorite()
    }
}