package com.example.myhome.feature.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import com.example.myhome.common.BASE_URL
import com.example.myhome.data.Banner
import kotlinx.android.synthetic.main.item_favorite_banner.view.*
import com.example.myhome.services.ImageLoadingService

class FavoriteListAdapter(
    var banners: MutableList<Banner>,
    val imageLoadingService: ImageLoadingService,
    val favoriteBannerClickListener: FavoriteBannerClickListener
) :
    RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: com.example.myhome.view.MyHomeImageView = itemView.findViewById(R.id.img_fav)
        val title: TextView = itemView.findViewById(R.id.title_fav)
        fun bindBanner(banner: Banner) {
            imageLoadingService.load(image, "$BASE_URL${banner.bannerImage}")
            title.text = banner.title

            //item click recyclerView
            itemView.setOnClickListener {
                favoriteBannerClickListener.onClick(banner)
            }

            //delete banner
            itemView.deleteFav.setOnClickListener {
                favoriteBannerClickListener.deleteItemClick(banner)
                banners.remove(banner)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite_banner, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindBanner(banners[position])

    override fun getItemCount(): Int = banners.size

    interface FavoriteBannerClickListener {
        fun onClick(banner: Banner)
        fun deleteItemClick(banner: Banner)
    }
}
