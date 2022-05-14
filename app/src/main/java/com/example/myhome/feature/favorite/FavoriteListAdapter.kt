package com.example.myhome.feature.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.common.Constants.BASE_URL
import com.example.myhome.data.model.Banner
import com.example.myhome.databinding.ItemFavoriteBannerBinding
import com.example.myhome.services.ImageLoadingService

class FavoriteListAdapter(
    var banners: MutableList<Banner>,
    val imageLoadingService: ImageLoadingService,
    val favoriteBannerClickListener: FavoriteBannerClickListener
) :
    RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemFavoriteBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindBanner(banner: Banner) {
            imageLoadingService.load(binding.imageFavorite, "$BASE_URL${banner.bannerImage}")
            binding.titleFavorite.text = banner.title

            //item click recyclerView
            binding.root.setOnClickListener {
                favoriteBannerClickListener.onClick(banner)
            }

            //delete banner
            binding.deleteFavoriteBtn.setOnClickListener {
                favoriteBannerClickListener.deleteItemClick(banner)
                banners.remove(banner)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemFavoriteBannerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindBanner(banners[position])

    override fun getItemCount(): Int = banners.size

    interface FavoriteBannerClickListener {
        fun onClick(banner: Banner)
        fun deleteItemClick(banner: Banner)
    }
}
