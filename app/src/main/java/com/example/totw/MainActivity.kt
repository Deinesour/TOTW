package com.example.totw

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)


        val newsFrag = newsCategoriesFragment()
        val homeFrag = homeFragment()
        val eventsFrag = eventsFragment()
        val events2Frag = events2Fragment()

        setCurrentFragment(homeFrag)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(homeFrag)
                R.id.news->setCurrentFragment(newsFrag)
                R.id.events->setCurrentFragment(events2Frag)
            }
            true
        }
    }
    private fun setCurrentFragment(fragment: Fragment)=
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
        val socialFrag = SocialMediaFragment()
        val notifFrag = NotificationsFragment()
        val newsletterFrag = newsletterFragment()
        val photoGalleryFrag = PhotoGalleryFragment()

        return when (item.itemId) {
            R.id.about -> {
                setCurrentFragment(aboutFrag)
                true
            }R.id.socialMedia -> {
                setCurrentFragment(socialFrag)
                true
            }R.id.notifications -> {
                setCurrentFragment(notifFrag)
                true
            }R.id.newsletter -> {
                setCurrentFragment(newsletterFrag)
                true
            }R.id.photoGallery -> {
                setCurrentFragment(photoGalleryFrag)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}