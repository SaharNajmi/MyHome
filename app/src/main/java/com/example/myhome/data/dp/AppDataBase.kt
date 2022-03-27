package com.example.myhome.data.dp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myhome.data.Banner
import com.example.myhome.data.repository.source.BannerLocalDataSource

@Database(entities = [Banner::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun bannerDao(): BannerLocalDataSource
}