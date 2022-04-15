package com.example.myhome.feature.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import com.example.myhome.common.Constants.BASE_URL
import com.example.myhome.data.model.Banner
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val myHomeImage: com.example.myhome.view.MyHomeImageView =
            itemView.findViewById(R.id.image_banner)
        val price: TextView = itemView.findViewById(R.id.txt_price)
        val title: TextView = itemView.findViewById(R.id.txt_title)
        val location: TextView = itemView.findViewById(R.id.txt_location)
        val room: TextView = itemView.findViewById(R.id.txt_number_of_rooms)
        val homeSize: TextView = itemView.findViewById(R.id.txt_home_size)
        private val buttonFavorite: ImageView = itemView.findViewById(R.id.btn_fav)
        fun bindBanner(banner: Banner) {
            imageLoadingService.load(myHomeImage, "$BASE_URL${banner.bannerImage}")
            title.text = banner.title
            price.text = "${banner.price} تومان "
            location.text = banner.location
            room.text = banner.numberOfRooms.toString()
            homeSize.text = banner.homeSize.toString()

            if (banner.fav)
                buttonFavorite.setImageResource(R.drawable.ic_bookmarked)
            else
                buttonFavorite.setImageResource(R.drawable.ic_not_bookmarked)

            //click favorite Button
            buttonFavorite.setOnClickListener {
                banner.fav = !banner.fav
                notifyItemChanged(adapterPosition)
                bannerOnClickListener!!.onFavoriteBtnClick(banner)
            }

            itemView.setOnClickListener {
                bannerOnClickListener!!.onBannerClick(banner)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_banner, parent, false)
        )
    }

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