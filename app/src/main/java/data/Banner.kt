package data

data class Banner(
    val id: Int,
    val userID: Int,
    val title: String,
    val category: String,
    val description: String,
    val location: String,
    val price: String,
    val image: String,
    val date: Int,
    val status: String
)