package com.proyecto.proyectotfg.Principal.Turista

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.proyecto.proyectotfg.Login.MainActivity
import com.proyecto.proyectotfg.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

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
        val chatfragment = ChatFragmentT()
        val profileFragment = ProfileFragmentT()

        mActiveFragment = homeFragment

        fragmentManager.beginTransaction()
            .add(R.id.hostFramentT, profileFragment, ProfileFragmentT::class.java.name)
            .hide(profileFragment).commit()
        fragmentManager.beginTransaction()
            .add(R.id.hostFramentT, chatfragment, ChatFragmentT::class.java.name)
            .hide(chatfragment).commit()
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
                R.id.action_chatT -> {
                    fragmentManager.beginTransaction().hide(mActiveFragment).show(chatfragment).commit()
                    mActiveFragment = chatfragment
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

    override fun onBackPressed() {
        var dialogq = MaterialAlertDialogBuilder(this).apply {
            setTitle("Advertencia")
            setCancelable(false)
            setMessage("¿Deseas cerrar sesión?")
            setPositiveButton("Aceptar") { _, i ->
                val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)!!.edit()
                prefs.clear()
                prefs.apply()
                FirebaseAuth.getInstance().signOut()
                val intent= Intent(context, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            setNegativeButton("Cancelar",null)

        }.show()
    }
}