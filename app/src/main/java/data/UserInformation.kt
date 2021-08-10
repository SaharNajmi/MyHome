package data

/*JSON
شماره موبایل را دریافت می کند و اطلاعات کاربر را برمی گرداند

{
    "id": 12,
    "username": "admin",
    "password": "1234",
    "phone": "09105552233",
    "image": "images/21-07-13-4-32-49-18424.jpeg"
}
 */
data class UserInformation(
    val id: Int,
    val image: String,
    val password: String,
    val username: String,
    val phone: String
)

