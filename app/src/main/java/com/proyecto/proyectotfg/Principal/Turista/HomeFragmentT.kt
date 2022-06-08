package com.proyecto.proyectotfg.Principal.Turista

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
import com.proyecto.proyectotfg.Adaptadores.RutasAdapter
import com.proyecto.proyectotfg.OnClickListener
import com.proyecto.proyectotfg.Principal.Guia.MainActivityDetallesderuta
import com.proyecto.proyectotfg.R
import com.proyecto.proyectotfg.databinding.FragmentHomeTBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.proyectotfg.Adaptadores.RutasAdapterT
import com.proyecto.proyectotfg.Clases.*

class HomeFragmentT : Fragment() , OnClickListener{
    private lateinit var mBinding: FragmentHomeTBinding
    private lateinit var mAdapter: RutasAdapterT
    private lateinit var mGridLayout: GridLayoutManager
    var listarutas=ArrayList<Rutas>()
    var tipo=0
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeTBinding.inflate(inflater, container, false)
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
        mBinding.swiplayout.setOnRefreshListener {
            setupRecyclerViewT()
            mBinding.swiplayout.isRefreshing=false
        }

    }

    private fun setupRecyclerViewT() {
        mAdapter= RutasAdapterT(ArrayList(),this)
        mGridLayout= GridLayoutManager(context,2)

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
        db.collection("users").get().addOnSuccessListener {
            for (i in it.documents){

                val docRef = db.collection("users").document(i.id)
                docRef.get()
                    .addOnSuccessListener { document ->
                        var tipo=document.get("Tipo de Usuario").toString()

                        if (tipo=="Guia"){
                            var numrutas=document.get("Num_rutas") as Long

                            var cont=0
                            while (cont<numrutas){
                                if (document.get("Rutas.ruta$cont.Nombre").toString().isNotEmpty()){
                                    var rutas: Rutas = Rutas(document.get("Rutas.ruta$cont.Nombre").toString(),
                                        document.get("Rutas.ruta$cont.Lugar de inicio").toString(),
                                        document.get("Rutas.ruta$cont.Provincia").toString(),
                                        document.get("Rutas.ruta$cont.Localidad").toString(),
                                        document.get("Rutas.ruta$cont.Kms").toString(),cont.toString(),i.id)

                                    listarutas.add(rutas)
                                }


                                cont++
                            }
                            mAdapter.setStores(listarutas)
                        }

                    }
                    .addOnFailureListener { exception ->

                    }
            }
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
            putExtra("emailguia",rutas.email)
            putExtra("persona",email)
            putExtra("turista",1)
        }

        startActivity(intent)
    }

    override fun editarrutafutura(rutas: RutasFuturas) {
        TODO("Not yet implemented")
    }

    override fun addruta(rutas: Rutas) {
        val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)



            var dialogq = MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("Añadir a rutas futuras")
                setCancelable(false)
                setMessage("Deseas añadir esta ruta, a tu apartado de futuras rutas")
                setPositiveButton("Aceptar"){ _, i->
                    db.collection("users").document(email!!).get().addOnSuccessListener {
                        var numrutasfuturas=it.get("Num_rutasfuturas") as Long
                        var cont=0
                        while (cont<numrutasfuturas){
                            if (it.get("Rutas_futuras.ruta$cont.Nombre").toString().compareTo(rutas.titulo)==0){
                                cont=-1
                                break
                            }
                            cont++
                        }

                        if (cont!=-1){
                            db.collection("users").document(email!!).get().addOnSuccessListener {
                                var numrutasfuturas=it.get("Num_rutasfuturas") as Long
                                var numerooriginal=numrutasfuturas
                                numrutasfuturas++
                                db.collection("users").document(email!!).update(
                                    mapOf(
                                        "Num_rutasfuturas" to numrutasfuturas,
                                        "Rutas_futuras.ruta${numerooriginal}.Nombre" to rutas.titulo,
                                        "Rutas_futuras.ruta${numerooriginal}.Provincia" to rutas.provincia,
                                        "Rutas_futuras.ruta${numerooriginal}.Localidad" to rutas.localidad,
                                        "Rutas_futuras.ruta${numerooriginal}.Lugardeinicio" to rutas.lugardeinicio,
                                        "Rutas_futuras.ruta${numerooriginal}.kms" to rutas.kms,
                                        "Rutas_futuras.ruta${numerooriginal}.Guia" to rutas.email,
                                        "Rutas_futuras.ruta${numerooriginal}.numruta" to rutas.numruta
                                    )
                                ).addOnSuccessListener {
                                    var dialogq = MaterialAlertDialogBuilder(requireContext()).apply {
                                        setTitle("Información")
                                        setMessage("Ruta registrada con éxisto en su apartado de rutas futuras")
                                        setPositiveButton("Aceptar",null)

                                    }.show()
                                }
                            }
                        }else{
                            var dialogq = MaterialAlertDialogBuilder(requireContext()).apply {
                                setTitle("Información")
                                setMessage("Esa ruta ya existe en tus rutas futuras")
                                setPositiveButton("Aceptar",null)

                            }.show()
                        }

                    }







                }
                setNegativeButton("Cancelar",null)

            }.show()


    }

    override fun addrutafutura(rutas: RutasFuturas) {
        TODO("Not yet implemented")
    }

    override fun borrarmonumento(fotos: Monumentos) {
        TODO("Not yet implemented")
    }

    override fun verchat(chat: Chats) {
        TODO("Not yet implemented")
    }
}
