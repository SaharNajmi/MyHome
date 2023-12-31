package com.example.myhome.feature.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.common.showMessage
import com.example.myhome.data.model.Banner
import com.example.myhome.databinding.FragmentFavoriteBinding
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteFragment : MyHomeFragment(), FavoriteListAdapter.FavoriteBannerClickListener {
    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBanners()
    }

    private fun getBanners() {
        favoriteViewModel.banners.observe(viewLifecycleOwner) { banners ->
            if (banners.isEmpty()) {
                binding.emptyLayout.isVisible = true
                binding.emptyLayout.setText(resources.getString(R.string.emptyFavorite))
            } else {
                binding.recyclerViewFavorite.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                binding.recyclerViewFavorite.adapter =
                    FavoriteListAdapter(banners as ArrayList<Banner>, get(), this)

                binding.emptyLayout.isVisible = false

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