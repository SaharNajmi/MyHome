package com.example.myhome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.bottom_nav_with_fab.*
import view.FavoriteFragment
import view.HomeFragment
import view.NewAdFragment
import view.ProfileFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*=====================Switch between Fragments in BottomNavigationView==================*/
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

        /*

                }
                R.id.chat -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.switch_fragment, ChatFragment())
                        .commit()
                }
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.switch_fragment, AdListFragment())
                        .commit()
                }
                R.id.favorite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.switch_fragment, FavoriteFragment())
                        .commit()
                }
            }
            true
        }

        floatingAdd.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.switch_fragment, NewAdFragment())
                .commit()
        }
*/
    }
}