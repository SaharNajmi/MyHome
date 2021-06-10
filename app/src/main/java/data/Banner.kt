package data

data class Banner(
    val id: Int,
    val userID: Int,
    val title: String,
    val category: Int,
    val sellOrRent: Int,
    val description: String,
    val location: String,
    val homeSize: Int,
    val numberOfRooms: Int,
    val price: Int,
    val image: String,
    val date: Int,
    val status: Int
)

var CATEGORY = 1
var SELL_OR_RENT = 1