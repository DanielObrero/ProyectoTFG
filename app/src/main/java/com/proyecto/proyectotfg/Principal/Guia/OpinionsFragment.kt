package com.proyecto.proyectotfg.Principal.Guia

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.proyecto.proyectotfg.Adaptadores.OpinionesAdapter
import com.proyecto.proyectotfg.Clases.Opiniones
import com.proyecto.proyectotfg.R
import com.proyecto.proyectotfg.databinding.FragmentOpinionsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class OpinionsFragment : Fragment(){

    private lateinit var mBinding: FragmentOpinionsBinding
    private lateinit var mAdapter: OpinionesAdapter
    private lateinit var mGridLayout: GridLayoutManager
    var listarutas=ArrayList<Opiniones>()
    var tipo=0
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentOpinionsBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        object : CountDownTimer(1000,1000){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                setupRecyclerViewT()
            }

        }.start()

        mBinding.button.setOnClickListener {
            setupRecyclerViewT()
        }
        mBinding.swiplayout.setOnRefreshListener {
            setupRecyclerViewT()
            mBinding.swiplayout.isRefreshing=false
        }
    }

    private fun setupRecyclerViewT() {
        mAdapter= OpinionesAdapter(ArrayList())
        mGridLayout= GridLayoutManager(context,1)

        obtenerDatos()


        mBinding.recyclerView.apply {
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
        db.collection("users").get().addOnSuccessListener {


                val docRef = db.collection("users").document(email!!)
                docRef.get()
                    .addOnSuccessListener { document ->

                            var numrutas=document.get("Num_rutas") as Long

                            var cont=0
                            while (cont<numrutas){
                                var numopiniones=document.get("Rutas.ruta$cont.Num_opiniones") as Long
                                var cont2=0
                                while (cont2<numopiniones){
                                    var opinion:Opiniones= Opiniones(
                                        document.get("Rutas.ruta$cont.Opiniones.opinion$cont2.nombreruta").toString(),
                                        document.get("Rutas.ruta$cont.Opiniones.opinion$cont2.persona").toString(),
                                        document.get("Rutas.ruta$cont.Opiniones.opinion$cont2.valor").toString(),
                                        document.get("Rutas.ruta$cont.Opiniones.opinion$cont2.numruta").toString(),
                                    )
                                    listarutas.add(opinion)
                                    cont2++
                                }

                                cont++
                            }
                            mAdapter.setStores(listarutas)


                    }
                    .addOnFailureListener { exception ->

                    }

        }







    }


}