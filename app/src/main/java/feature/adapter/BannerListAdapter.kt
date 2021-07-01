package feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myhome.R
import data.Banner
import services.ImageLoadingService
import view.MyHomeImageView
import java.lang.String.format


class BannerListAdapter(val imageLoadingService: ImageLoadingService) :
    RecyclerView.Adapter<BannerListAdapter.ViewHolder>() {

    var bannerOnClickListener: BannerOnClickListener? = null

    var banner = ArrayList<Banner>()
        //به خاطر اینکه مقدار عوض میشه وقتی صفحه لود شد
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: MyHomeImageView = itemView.findViewById(R.id.image_banner)
        val title: TextView = itemView.findViewById(R.id.txt_price)
        val price: TextView = itemView.findViewById(R.id.txt_title)
        val location: TextView = itemView.findViewById(R.id.txt_location)
        val room: TextView = itemView.findViewById(R.id.txt_number_of_rooms)
        val homeSize: TextView = itemView.findViewById(R.id.txt_home_size)
        fun bindBanner(banner: Banner) {
            imageLoadingService.load(image, banner.image)
            title.text = banner.title
            price.text = "${format("%,d", banner.price)} تومان "
            location.text = banner.location
            room.text = banner.numberOfRooms.toString()
            homeSize.text = banner.homeSize.toString()

            // نباید از خود آداپتر کاربر را به اکتیویتی بفرستیم اطلاعات را به فرگمنت پاس بده فرگمنت تصمیم میگیره که دیتا را به کجا بفرسته
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
}

interface BannerOnClickListener {
    fun onBannerClick(banner: Banner)
}