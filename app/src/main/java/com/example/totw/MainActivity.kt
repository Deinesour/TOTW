package com.example.totw

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)


        val newsFrag = newsCategoriesFragment()
        val homeFrag = homeFragment()
        val eventsFrag = eventsFragment()

        setCurrentFragment(homeFrag)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(homeFrag)
                R.id.news->setCurrentFragment(newsFrag)
                R.id.events->setCurrentFragment(eventsFrag)

            }
            true
        }
    }
    fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.top_nav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val aboutFrag = AboutFragment()
        return when (item.itemId) {
            R.id.about -> {
                setCurrentFragment(aboutFrag)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}