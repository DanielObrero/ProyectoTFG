package com.proyecto.proyectotfg.Principal.Guia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.proyecto.proyectotfg.Adaptadores.ChatsAdapter
import com.proyecto.proyectotfg.OnClickListener
import com.proyecto.proyectotfg.Principal.Turista.MainActivityChat
import com.proyecto.proyectotfg.R
import com.proyecto.proyectotfg.databinding.FragmentChatBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.proyectotfg.Clases.*

class ChatFragmentG : Fragment() ,OnClickListener{
    private lateinit var mBinding: FragmentChatBinding
    private lateinit var mAdapter: ChatsAdapter
    private lateinit var mGridLayout: GridLayoutManager
    var listarutas=ArrayList<Chats>()
    var tipo=0
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentChatBinding.inflate(inflater, container, false)
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.btnactualizar.setOnClickListener {
            setupRecyclerViewT()
        }
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

    }
    private fun setupRecyclerViewT() {
        mAdapter= ChatsAdapter(ArrayList(),this)
        mGridLayout= GridLayoutManager(context,1)

        obtenerDatos()


        mBinding.recyclerViewT.apply {
            setHasFixedSize(true)
            layoutManager=mGridLayout
            adapter=mAdapter
        }

    }

    private fun obtenerDatos() {

        listarutas.clear()
        val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        var numjuego:Int=0


        val docRef = db.collection("users").document(email!!)
        docRef.get()
            .addOnSuccessListener { document ->


                var numchat=document.get("Num_chat") as Long
                var cont=0
                while (cont<numchat){
                    if (document.get("Chats.chat$cont.Receptor").toString().isNotEmpty()){
                        var chat:Chats=Chats(document.get("Chats.chat$cont.Receptor").toString(),cont.toString())

                        listarutas.add(chat)
                    }


                    cont++
                }
                mAdapter.setStores(listarutas)


            }
            .addOnFailureListener { exception ->

            }
    }

    override fun borrarfoto(foto: Fotos) {
        TODO("Not yet implemented")
    }

    override fun editarmonumento(Monumento: Monumentos) {
        TODO("Not yet implemented")
    }

    override fun editarruta(rutas: Rutas) {
        TODO("Not yet implemented")
    }

    override fun editarrutafutura(rutas: RutasFuturas) {
        TODO("Not yet implemented")
    }

    override fun addruta(rutas: Rutas) {
        TODO("Not yet implemented")
    }

    override fun addrutafutura(rutas: RutasFuturas) {
        TODO("Not yet implemented")
    }

    override fun borrarmonumento(fotos: Monumentos) {
        TODO("Not yet implemented")
    }

    override fun verchat(chat: Chats) {
        val intent= Intent(context, MainActivityChat::class.java).apply {
            putExtra("guia",chat.persona)
            putExtra("numchat",chat.numchat)
            putExtra("ver",1)
        }
        startActivity(intent)
    }


}
