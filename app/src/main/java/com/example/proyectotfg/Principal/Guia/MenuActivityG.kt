package com.example.proyectotfg.Principal.Guia

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.proyectotfg.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuActivityG : AppCompatActivity() {
    private lateinit var mActiveFragment: Fragment
    private var mFragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_guia)

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

        val homeFragment = HomeFragmentG()
        val addFragment = OpinionsFragment()
        val profileFragment = ProfileFragmentG()

        mActiveFragment = homeFragment

        fragmentManager.beginTransaction()
            .add(R.id.hostFramentG, profileFragment, ProfileFragmentG::class.java.name)
            .hide(profileFragment).commit()
        fragmentManager.beginTransaction()
            .add(R.id.hostFramentG, addFragment, OpinionsFragment::class.java.name)
            .hide(addFragment).commit()
        fragmentManager.beginTransaction()
            .add(R.id.hostFramentG, homeFragment, HomeFragmentG::class.java.name).commit()

       findViewById<BottomNavigationView>(R.id.bottomnavG).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_homeG -> {
                    fragmentManager.beginTransaction().hide(mActiveFragment).show(homeFragment).commit()
                    mActiveFragment = homeFragment
                    true
                }
                R.id.action_Opiniones -> {
                    fragmentManager.beginTransaction().hide(mActiveFragment).show(addFragment).commit()
                    mActiveFragment = addFragment
                    true
                }
                R.id.action_profileG -> {
                    fragmentManager.beginTransaction().hide(mActiveFragment).show(profileFragment).commit()
                    mActiveFragment = profileFragment
                    true
                }
                else -> false
            }
        }


    }
}