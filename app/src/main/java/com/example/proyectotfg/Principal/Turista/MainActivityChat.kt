package com.example.proyectotfg.Principal.Turista

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.proyectotfg.R
import com.example.proyectotfg.databinding.ActivityMainChatBinding
import com.example.proyectotfg.databinding.ActivityMainDetallesderutaBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivityChat : AppCompatActivity() {
    private val db = Firebase.firestore
    private lateinit var mBinding: ActivityMainChatBinding
    var creado=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_chat)
        mBinding= ActivityMainChatBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnenviar.setOnClickListener {
            enviarmensajeguia(mBinding.etmensaje.text.toString())
            enviarmensajemio(mBinding.etmensaje.text.toString())
        }
    }

    private fun enviarmensajeguia(mensaje: String) {
        var guia=intent.extras?.getString("guia").toString()
        Log.d("guia",guia)
        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)



        db.collection("users").document(guia).get().addOnSuccessListener {

            var numchat=it.get("Num_chat") as Long
            numchat--
            var numeromensajes=it.get("Chats.chat$numchat.Num_mensajes") as Long
            var numeronuevo=numeromensajes
            numeronuevo++
            db.collection("users").document(guia).update(
                mapOf(
                    "Chats.chat$numchat.Mensajes.mensaje$numeromensajes.mensaje" to mensaje,
                    "Chats.chat$numchat.Mensajes.mensaje$numeromensajes.persona" to email,
                    "Chats.chat$numchat.Num_mensajes" to numeronuevo
                )
            )
        }

    }

    fun crearchatmio(){
        var guia=intent.extras?.getString("guia").toString()
        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        db.collection("users").document(email!!).get().addOnSuccessListener {

            var numchat = it.get("Num_chat") as Long
            var nuevo=numchat
            nuevo++
            db.collection("users").document(email).update(
                mapOf(
                    "Chats.chat$numchat.Num_mensajes" to 0,
                    "Chats.chat$numchat.Receptor" to guia,
                    "Num_chat" to nuevo
                )
            )
        }
    }
    fun enviarmensajemio(mensaje: String){
        var guia=intent.extras?.getString("guia").toString()
        Log.d("guia",guia)
        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        db.collection("users").document(email!!).get().addOnSuccessListener {

            var numchat=it.get("Num_chat") as Long
            numchat--
            var numeromensajes=it.get("Chats.chat$numchat.Num_mensajes") as Long
            var numeronuevo=numeromensajes
            numeronuevo++
            db.collection("users").document(email).update(
                mapOf(
                    "Chats.chat$numchat.Mensajes.mensaje$numeromensajes.mensaje" to mensaje,
                    "Chats.chat$numchat.Mensajes.mensaje$numeromensajes.persona" to email,
                    "Chats.chat$numchat.Num_mensajes" to numeronuevo
                )
            )
        }
    }



    fun aumentar(){
        var guia=intent.extras?.getString("guia").toString()
        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        db.collection("users").document(email!!).get().addOnSuccessListener {

            var numchat = it.get("Num_chat") as Long
            numchat++
            db.collection("users").document(email).update(
                mapOf(

                )
            )
        }
    }


}