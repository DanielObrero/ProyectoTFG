package com.example.proyectotfg.Principal.Turista

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.proyectotfg.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuActivityT : AppCompatActivity() {
    private lateinit var mActiveFragment: Fragment
    private var mFragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_turista)

        var bundle:Bundle?=intent.extras
        var email:String?=bundle?.getString("email")
        var tu:String?=bundle?.getString("tipo")
        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("tipo",tu)
        prefs.apply()
        mFragmentManager = supportFragmentManager
        setupBottomNav(mFragmentManager!!)
    }

    private fun setupBottomNav(fragmentManager: FragmentManager) {
        mFragmentManager?.let { // TODO: 14/06/21 clean before
            for (fragment in it.fragments) {
                it.beginTransaction().remove(fragment!!).commit()
            }
        }

        val homeFragment = HomeFragmentT()
        val routesFragment = RoutesFragment()
        val profileFragment = ProfileFragmentT()

        mActiveFragment = homeFragment

        fragmentManager.beginTransaction()
            .add(R.id.hostFramentT, profileFragment, ProfileFragmentT::class.java.name)
            .hide(profileFragment).commit()
        fragmentManager.beginTransaction()
            .add(R.id.hostFramentT, routesFragment, RoutesFragment::class.java.name)
            .hide(routesFragment).commit()
        fragmentManager.beginTransaction()
            .add(R.id.hostFramentT, homeFragment, HomeFragmentT::class.java.name).commit()

       findViewById<BottomNavigationView>(R.id.bottomnavT).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_homeT -> {
                    fragmentManager.beginTransaction().hide(mActiveFragment).show(homeFragment).commit()
                    mActiveFragment = homeFragment
                    true
                }
                R.id.action_routes -> {
                    fragmentManager.beginTransaction().hide(mActiveFragment).show(routesFragment).commit()
                    mActiveFragment = routesFragment
                    true
                }
                R.id.action_profileT -> {
                    fragmentManager.beginTransaction().hide(mActiveFragment).show(profileFragment).commit()
                    mActiveFragment = profileFragment
                    true
                }
                else -> false
            }
        }


    }
}