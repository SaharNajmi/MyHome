package data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Parcelize برای اینکه بتونیم مقادیر را ارسال کنیم
@Parcelize
data class Banner(
    val id: Int,
    val userID: Int,
    val title: String,
    val category: String,
    val sellOrRent: String,
    val description: String,
    val location: String,
    val homeSize: Int,
    val numberOfRooms: Int,
    val price: Int,
    val image: String,
    val date: Int,
    val status: Int
) : Parcelable

var CATEGORY = 1
var SELL_OR_RENT = 1

/*JSON
سه تا مقدارsellOrRent و category و phoneNumber میگیره که دو حالت داره یکیش همه آیدی ها رو نشون میده یکیشش شماره تلفنی که خودمون بهش میدیم یعنی آگهی های همون کاربری که لاگین کرده
گرفتن آگهی های کاربر:    ->    09100000000&sellOrRent=0&category=0
phoneNumber=all&sellOrRent=1&category=2   <-  گرفتن آگهی ها بر اساس دسته بندی و خرید یا فروش آن

[
    {
        "id": 4,
        "userID": 2,
        "title": "آپارتمانی 60 متر",
        "description": "سال ساخت 1400",
        "price": "8000000000",
        "location": "تهران، ولیعصر",
        "category": "2",
        "sellOrRent": "2",
        "homeSize": 150,
        "numberOfRooms": 1,
        "image": "images/1254.jpg",
        "status": 1,
        "date": 274098
    }
]
 */
