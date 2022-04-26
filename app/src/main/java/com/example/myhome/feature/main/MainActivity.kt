package com.example.myhome.feature.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myhome.R
import com.example.myhome.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //background fab is null
        binding.navigationBtn.bottomNavigationView.background = null
        val navController = findNavController(R.id.nav_host_fragment)
        binding.navigationBtn.bottomNavigationView.setupWithNavController(navController)
    }

    fun hideBottomNavigation() {
        binding.navigationBtn.layoutBottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.navigationBtn.layoutBottomNavigationView.visibility = View.VISIBLE
    }
}