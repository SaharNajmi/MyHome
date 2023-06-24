package com.example.myhome.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import com.example.myhome.common.Constants.BASE_URL
import com.example.myhome.data.model.Banner
import com.example.myhome.databinding.ItemBannerBinding
import com.example.myhome.services.ImageLoadingService

class BannerListAdapter(val imageLoadingService: ImageLoadingService) :
    RecyclerView.Adapter<BannerListAdapter.ViewHolder>(), Filterable {

    var bannerOnClickListener: BannerOnClickListener? = null

    var banner = ArrayList<Banner>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    var filterList = ArrayList<Banner>()

    fun setData(list: ArrayList<Banner>?) {
        this.filterList = list!!
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindBanner(banner: Banner) {
            imageLoadingService.load(binding.imageBanner, "$BASE_URL${banner.bannerImage}")
            binding.apply {
                txtTitle.text = banner.title
                txtPrice.text = "${banner.price} تومان "
                txtLocation.text = banner.location
                txtNumberOfRooms.text = banner.numberOfRooms.toString()
                txtHomeSize.text = banner.homeSize.toString()
            }


            if (banner.fav)
                binding.favoriteBtn.setImageResource(R.drawable.ic_bookmarked)
            else
                binding.favoriteBtn.setImageResource(R.drawable.ic_not_bookmarked)

            binding.favoriteBtn.setOnClickListener {
                banner.fav = !banner.fav
                notifyItemChanged(adapterPosition)
                bannerOnClickListener!!.onFavoriteBtnClick(banner)
            }

            binding.root.setOnClickListener {
                bannerOnClickListener!!.onBannerClick(banner)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemBannerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = banner.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindBanner(banner[position])

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults? {
                val filterResult = FilterResults()
                if (charSequence == null) {
                    filterList = banner
                } else {
                    val searChar = charSequence.toString().toLowerCase()
                    val itemModel = ArrayList<Banner>()
                    for (item in filterList) {
                        if (item.title.contains(searChar)) {
                            itemModel.add(item)
                        }
                    }
                    filterResult.count = itemModel.size
                    filterResult.values = itemModel
                }
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
                banner = filterResults!!.values as ArrayList<Banner>
                notifyDataSetChanged()
            }
        }
    }

    interface BannerOnClickListener {
        fun onBannerClick(banner: Banner)
        fun onFavoriteBtnClick(banner: Banner)
    }
}