package data.dp

import androidx.room.Database
import androidx.room.RoomDatabase
import data.Banner
import data.repository.source.BannerLocalDataSource

//اضافه کردن موجودیت های دیتابیس
//abstract به خاطر اینکه  روم قراره متد ها رو ایمپلمنت کنه
@Database(entities = [Banner::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun bannerDao(): BannerLocalDataSource
}