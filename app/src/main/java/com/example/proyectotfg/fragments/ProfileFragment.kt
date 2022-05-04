package com.example.proyectotfg.fragments

import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.provider.Settings.Global.getString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.proyectotfg.MainActivity
import com.example.proyectotfg.R
import com.example.proyectotfg.databinding.ActivityMainBinding
import com.example.proyectotfg.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import io.grpc.internal.JsonUtil.getString

class ProfileFragment : Fragment() {
    private lateinit var mBinding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        setup(email?:"")
    }
    private fun setup(email:String) {

        var textView: TextView =mBinding.Textv
        textView.text=email

        var logOutButton: Button =mBinding.cerrar
        logOutButton.setOnClickListener {
            val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)!!.edit()
            prefs.clear()
            prefs.apply()
            FirebaseAuth.getInstance().signOut()
            val intent= Intent(context, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    fun onBackPressed() {

    }

}