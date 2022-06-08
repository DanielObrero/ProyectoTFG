package com.proyecto.proyectotfg.Principal.Turista

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.proyecto.proyectotfg.Adaptadores.MensajesAdapter
import com.proyecto.proyectotfg.Clases.Mensajes
import com.proyecto.proyectotfg.R
import com.proyecto.proyectotfg.databinding.ActivityMainChatBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivityChat : AppCompatActivity() {
    private lateinit var mAdapter: MensajesAdapter
    private lateinit var mGridLayout: GridLayoutManager
    var listarutas=ArrayList<Mensajes>()
    private val db = Firebase.firestore
    private lateinit var mBinding: ActivityMainChatBinding
    var creado=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_chat)
        mBinding= ActivityMainChatBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.tvpersonadechat.text=intent.extras?.getString("guia")
        object : CountDownTimer(1000,1000){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                setupRecyclerViewT()
            }

        }.start()
        mBinding.swiplayout.setOnRefreshListener {
            setupRecyclerViewT()
            mBinding.swiplayout.isRefreshing=false
        }
        mBinding.btnactualizarchat.setOnClickListener {
            setupRecyclerViewT()
        }
        mBinding.btnenviar.setOnClickListener {
            enviarmensajeguia(mBinding.etmensaje.text.toString())
            enviarmensajemio(mBinding.etmensaje.text.toString())
            object : CountDownTimer(1000,1000){
                override fun onTick(p0: Long) {

                }

                override fun onFinish() {
                    mBinding.etmensaje.setText("")
                    setupRecyclerViewT()
                }

            }.start()
        }
    }

    private fun enviarmensajeguia(mensaje: String) {
        var guia=intent.extras?.getString("guia").toString()

        var ver=intent.extras?.getInt("ver")
        var num=intent.extras?.getString("numchat")
        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)

            if (ver==1){
                db.collection("users").document(guia).get().addOnSuccessListener {
                    var numchat=it.get("Num_chat") as Long
                    var cont=0
                    while (cont<numchat){
                        if (it.get("Chats.chat$cont.Receptor").toString().compareTo(email!!)==0){
                            var numeromensajes=it.get("Chats.chat$cont.Num_mensajes") as Long
                            var numeronuevo=numeromensajes
                            numeronuevo++
                            db.collection("users").document(guia).update(
                                mapOf(
                                    "Chats.chat$cont.Mensajes.mensaje$numeromensajes.mensaje" to mensaje,
                                    "Chats.chat$cont.Mensajes.mensaje$numeromensajes.persona" to email,
                                    "Chats.chat$cont.Num_mensajes" to numeronuevo
                                )
                            )
                        }
                        cont++
                    }
                }

            }else{
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



    }
    fun enviarmensajemio(mensaje: String){
        var guia=intent.extras?.getString("guia").toString()
        var ver=intent.extras?.getInt("ver")
        var num=intent.extras?.getString("numchat")

        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        db.collection("users").document(email!!).get().addOnSuccessListener {
            var numchat:Long=0
            if (ver==1){
                numchat=num!!.toLong()

            }else{
                numchat=it.get("Num_chat") as Long
                numchat--
            }

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



    private fun setupRecyclerViewT() {
        mAdapter= MensajesAdapter(ArrayList())
        mGridLayout= GridLayoutManager(this,1)

        obtenerDatos()


        mBinding.recyclerViewT.apply {
            setHasFixedSize(true)
            layoutManager=mGridLayout
            adapter=mAdapter
        }

    }

    private fun obtenerDatos() {

        listarutas.clear()
        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        var numjuego:Int=0


        val docRef = db.collection("users").document(email!!)
        docRef.get()
            .addOnSuccessListener { document ->
                var numchat=intent.extras?.getString("numchat")
                var nummensaje=document.get("Chats.chat$numchat.Num_mensajes") as Long
                var cont=0
                while (cont<nummensaje){
                    if (document.get("Chats.chat$cont.Receptor").toString().isNotEmpty()){
                        var chat: Mensajes =
                            Mensajes(document.get("Chats.chat$numchat.Mensajes.mensaje$cont.mensaje").toString(),
                                document.get("Chats.chat$numchat.Mensajes.mensaje$cont.persona").toString(),
                                email,
                                cont.toString())

                        listarutas.add(chat)
                    }


                    cont++
                }
                mAdapter.setStores(listarutas)


            }
            .addOnFailureListener { exception ->

            }
    }


}