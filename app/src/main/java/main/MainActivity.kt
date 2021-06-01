package main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myhome.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sevenlearn.nikestore.common.setupWithNavController
import kotlinx.android.synthetic.main.bottom_nav_with_fab.*
import view.FavoriteFragment
import view.HomeFragment
import view.NewAdFragment
import view.ProfileFragment

class MainActivity : AppCompatActivity() {
    /*   override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
           setContentView(R.layout.activity_main)

           *//*=====================Switch between Fragments in BottomNavigationView==================*//*
        //بک گراند fab خالی میشه ->  fab روی باتن نویگیشن به حالت نیم دایره درمیاد
        bottomNavigationView.background = null
        supportFragmentManager.beginTransaction()
            .replace(R.id.switch_fragment, HomeFragment())
            .commit()
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.switch_fragment, HomeFragment())
                        .commit()
                }
                R.id.fav -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.switch_fragment, FavoriteFragment())
                        .commit()
                }
                R.id.profile -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.switch_fragment, ProfileFragment())
                        .commit()
                }
                R.id.add -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.switch_fragment, NewAdFragment())
                        .commit()
                }
            }
            true
        }

    }*/
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val navGraphIds = listOf(R.navigation.home, R.navigation.profile, R.navigation.favorite)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}