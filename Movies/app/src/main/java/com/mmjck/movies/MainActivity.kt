package com.mmjck.movies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mmjck.movies.ui.screen.favorites.FavoritesFragment
import com.mmjck.movies.ui.screen.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.main_toolbar)
        bottomNav = findViewById(R.id.bottom_nav)

        setSupportActionBar(toolbar)
        loadFragment(HomeFragment(), R.id.nav_home)

        bottomNav.setOnItemSelectedListener {
             when (it.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment(), it.itemId)
                    true
                }
                R.id.nav_favorites -> {
                    loadFragment(FavoritesFragment(), it.itemId)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, id: Int ) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()

        setToolbarTitle(id)
    }

    private fun setToolbarTitle(itemId: Int) {
        val title = when (itemId) {
            R.id.nav_home -> getString(R.string.home_tab_label)
            R.id.nav_favorites -> getString(R.string.favorites_tab_label)
            else -> "Movies App"
        }
        findViewById<Toolbar>(R.id.main_toolbar).title = title
    }
}

