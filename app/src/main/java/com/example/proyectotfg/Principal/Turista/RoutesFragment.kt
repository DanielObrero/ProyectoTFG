package com.example.proyectotfg.Principal.Turista

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyectotfg.Adaptadores.RutasAdapter
import com.example.proyectotfg.Adaptadores.RutasfuturasAdapter
import com.example.proyectotfg.Clases.Fotos
import com.example.proyectotfg.Clases.Monumentos
import com.example.proyectotfg.Clases.Rutas
import com.example.proyectotfg.OnClickListener
import com.example.proyectotfg.Principal.Guia.MainActivityDetallesderuta
import com.example.proyectotfg.R
import com.example.proyectotfg.databinding.FragmentHomeTBinding
import com.example.proyectotfg.databinding.FragmentRoutesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RoutesFragment : Fragment() , OnClickListener{
    private lateinit var mBinding: FragmentRoutesBinding
    private lateinit var mAdapter: RutasfuturasAdapter
    private lateinit var mGridLayout: GridLayoutManager
    var listarutas=ArrayList<Rutas>()
    var tipo=0
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentRoutesBinding.inflate(inflater, container, false)
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

    }

    private fun setupRecyclerViewT() {
        mAdapter= RutasfuturasAdapter(ArrayList(),this)
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



                val docRef = db.collection("users").document(email!!)
                docRef.get()
                    .addOnSuccessListener { document ->
                        var tipo=document.get("Tipo de Usuario").toString()
                        Log.d("esto","tipo=$tipo")


                            var numrutasfuturas=document.get("Num_rutasfuturas") as Long

                            var cont2=0
                            if (numrutasfuturas!=0L) {
                                    while (cont2<numrutasfuturas) {
                                        var rutas: Rutas = Rutas(document.get("Rutas_futuras.ruta$cont2.Nombre").toString(),
                                            document.get("Rutas_futuras.ruta$cont2.Lugar de inicio").toString(),
                                            document.get("Rutas_futuras.ruta$cont2.Provincia").toString(),
                                            document.get("Rutas_futuras.ruta$cont2.Localidad").toString(),
                                            document.get("Rutas_futuras.ruta$cont2.Kms").toString(),
                                            document.get("Rutas_futuras.ruta$cont2.numruta").toString(),
                                            document.get("Rutas_futuras.ruta$cont2.Guia").toString())

                                        listarutas.add(rutas)
                                        cont2++
                                    }



                                mAdapter.setStores(listarutas)
                            }




                    }
                    .addOnFailureListener { exception ->
                        Log.d("rutas","fallo")
                    }







    }

    override fun borrarfoto(foto: Fotos) {
        TODO("Not yet implemented")
    }

    override fun editarmonumento(Monumento: Monumentos) {
        TODO("Not yet implemented")
    }

    override fun editarruta(rutas: Rutas) {
        val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        var intent= Intent(context, MainActivityDetallesderuta::class.java).apply {
            putExtra("nombreruta",rutas.titulo)
            putExtra("etprovincia",rutas.provincia)
            putExtra("etkms",rutas.kms)
            putExtra("etlugardeinicio",rutas.lugardeinicio)
            putExtra("localidad",rutas.localidad)
            putExtra("numruta",rutas.numruta)
            putExtra("email",rutas.email)
            putExtra("persona",email)
            putExtra("turista",1)
        }

        startActivity(intent)
    }

    override fun addruta(rutas: Rutas) {
        TODO("Not yet implemented")
    }

    override fun borrarmonumento(fotos: Monumentos) {
        TODO("Not yet implemented")
    }

}



