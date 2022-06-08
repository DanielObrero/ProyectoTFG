package com.proyecto.proyectotfg.Principal.Turista

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.proyecto.proyectotfg.Adaptadores.RutasfuturasAdapter
import com.proyecto.proyectotfg.OnClickListener
import com.proyecto.proyectotfg.Principal.Guia.MainActivityDetallesderuta
import com.proyecto.proyectotfg.R
import com.proyecto.proyectotfg.databinding.FragmentRoutesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.proyectotfg.Clases.*


class RoutesFragment : Fragment() , OnClickListener{
    private lateinit var mBinding: FragmentRoutesBinding
    private lateinit var mAdapter: RutasfuturasAdapter
    private lateinit var mGridLayout: GridLayoutManager
    var listarutas=ArrayList<RutasFuturas>()
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


        mBinding.button.setOnClickListener {
            setupRecyclerViewT()
        }
        mBinding.swiplayout.setOnRefreshListener {
            setupRecyclerViewT()
            mBinding.swiplayout.isRefreshing=false
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



                            var numrutasfuturas=document.get("Num_rutasfuturas") as Long

                            var cont2=0
                            if (numrutasfuturas!=0L) {
                                    while (cont2<numrutasfuturas) {
                                        if (document.get("Rutas_futuras.ruta$cont2.Nombre").toString().isNotEmpty()){
                                            var rutas: RutasFuturas = RutasFuturas(document.get("Rutas_futuras.ruta$cont2.Nombre").toString(),
                                                document.get("Rutas_futuras.ruta$cont2.Lugardeinicio").toString(),
                                                document.get("Rutas_futuras.ruta$cont2.Provincia").toString(),
                                                document.get("Rutas_futuras.ruta$cont2.Localidad").toString(),
                                                document.get("Rutas_futuras.ruta$cont2.kms").toString(),
                                                document.get("Rutas_futuras.ruta$cont2.numruta").toString(),
                                                cont2.toString(),
                                                document.get("Rutas_futuras.ruta$cont2.Guia").toString())

                                            listarutas.add(rutas)
                                        }

                                        cont2++
                                    }



                                mAdapter.setStores(listarutas)
                            }




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

    }

    override fun editarrutafutura(rutas: RutasFuturas) {
        val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        var intent= Intent(context, MainActivityDetallesderuta::class.java).apply {
            putExtra("nombreruta",rutas.titulo)
            putExtra("etprovincia",rutas.provincia)
            putExtra("etkms",rutas.kms)
            putExtra("etlugardeinicio",rutas.lugardeinicio)
            putExtra("localidad",rutas.localidad)
            putExtra("numruta",rutas.numruta)
            putExtra("emailguia",rutas.email)
            putExtra("persona",email)
            putExtra("turista",1)
        }

        startActivity(intent)
    }

    override fun addruta(rutas: Rutas) {

    }

    override fun addrutafutura(rutas: RutasFuturas) {
        var dialogo= AlertDialog.Builder(requireContext()).apply {
            setTitle("Información")
            setCancelable(false)
            setPositiveButton("Aceptar"){ _,i ->
                val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                var email=prefs?.getString("email",null)
                db.collection("users").document(email!!).update(
                    mapOf(
                        "Rutas_futuras.ruta${rutas.numrutafutura}.Nombre" to ""
                    )
                ).addOnSuccessListener {
                    var dialogo= AlertDialog.Builder(requireContext()).apply {
                        setTitle("Información")
                        setCancelable(false)
                        setPositiveButton("Aceptar",null)
                        setMessage("La ruta se ha borrado correctamente")
                    }.show()
                    setupRecyclerViewT()
                }
            }
            setNegativeButton("Cancelar",null)
            setMessage("¿Desea borrar la ruta?")
        }.show()
    }

    override fun borrarmonumento(fotos: Monumentos) {
        TODO("Not yet implemented")
    }

    override fun verchat(chat: Chats) {
        TODO("Not yet implemented")
    }

}



