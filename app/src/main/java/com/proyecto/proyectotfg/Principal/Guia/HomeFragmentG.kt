package com.proyecto.proyectotfg.Principal.Guia

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.proyecto.proyectotfg.Adaptadores.RutasAdapter
import com.proyecto.proyectotfg.OnClickListener
import com.proyecto.proyectotfg.R
import com.proyecto.proyectotfg.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.proyectotfg.Adaptadores.RutasAdapterT
import com.proyecto.proyectotfg.Clases.*

class HomeFragmentG : Fragment(),OnClickListener {
    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mAdapter: RutasAdapter
    private lateinit var mAdapterT: RutasAdapterT
    private lateinit var mGridLayout: GridLayoutManager
    var listarutas=ArrayList<Rutas>()
    var arrayprovincias=ArrayList<String>()
    var tipo=0
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.recyclerViewG.setOnScrollChangeListener(
            View.OnScrollChangeListener { v, scrollX, scrollY, _, _ ->
                if (!v.canScrollVertically(1)){
                    mBinding.floatingActionButton.visibility=View.GONE
                }else{
                    mBinding.floatingActionButton.visibility=View.VISIBLE
                }



            }
        )
        mBinding.swiplayoutt.setOnRefreshListener {
            setupRecyclerViewT()
            mBinding.swiplayoutt.isRefreshing=false
        }
        mBinding.swiplayoutg.setOnRefreshListener {
            setupRecyclerViewG()
            mBinding.swiplayoutg.isRefreshing=false
        }
    mBinding.btnturista.setOnClickListener {
        tipo=1
        mBinding.btnturista.isEnabled=false
        mBinding.btnguia.isEnabled=true
        mBinding.clGuia.visibility=View.GONE
        mBinding.clTurista.visibility=View.VISIBLE
        object : CountDownTimer(1000,1000){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                setupRecyclerViewT()


            }

        }.start()


    }

        mBinding.btnguia.setOnClickListener {
            tipo=0
            mBinding.btnguia.isEnabled=false
            mBinding.btnturista.isEnabled=true
            mBinding.clGuia.visibility=View.VISIBLE
            mBinding.clTurista.visibility=View.GONE
        }

        mBinding.floatingActionButton.setOnClickListener{
            val dialog=layoutInflater.inflate(R.layout.dialogo_nuevaruta,null)
            var dialogq = MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("Nueva ruta")
                setView(dialog)
                setCancelable(false)
                setPositiveButton("Aceptar") { _, i ->
                if (dialog.findViewById<EditText>(R.id.editTextTextPersonName).text.isNotEmpty()){
                    val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                    var email=prefs?.getString("email",null)
                    db.collection("users").document(email!!).get().addOnSuccessListener {
                        var numrutas=it.get("Num_rutas") as Long
                        var cont=0
                        while (cont<numrutas){
                            if (it.get("Rutas.ruta$cont.Nombre").toString().compareTo(dialog.findViewById<EditText>(R.id.editTextTextPersonName).text.toString())==0){
                                cont=-1
                                break
                            }
                            cont++
                        }
                        if (cont!=-1){
                            val intent= Intent(context, MainActivityadd::class.java).apply {
                                putExtra("email",email)
                                putExtra("rutanueva",1)
                                putExtra("nombreruta",dialog.findViewById<EditText>(R.id.editTextTextPersonName).text.toString())
                            }
                            startActivity(intent)
                        }else{
                            var dialogq = MaterialAlertDialogBuilder(context).apply {
                                setTitle("Info")
                                setCancelable(false)
                                setMessage("El nombre de la ruta no puede ser igual que una ya existente")
                                setPositiveButton("Aceptar") { _, i ->
                                }

                            }.show()
                        }
                    }



                } else{
                    var dialogq = MaterialAlertDialogBuilder(context).apply {
                        setTitle("Info")
                        setCancelable(false)
                        setMessage("El campo Nombre de ruta no puede estar vacio")
                        setPositiveButton("Aceptar") { _, i ->
                        }

                    }.show()
                }
                }
                setNegativeButton("Cancelar",null)

            }.show()



        }
    }

    override fun onResume() {
        super.onResume()
        object : CountDownTimer(1000,1000){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                setupRecyclerViewG()
            }

        }.start()
    }

    private fun setupRecyclerViewG() {
        mAdapter= RutasAdapter(ArrayList(),this)
        mGridLayout= GridLayoutManager(context,2)
        obtenerDatos()
        mBinding.recyclerViewG.apply {
            setHasFixedSize(true)
            layoutManager=mGridLayout
            adapter=mAdapter
        }
    }
    private fun setupRecyclerViewT() {
        mAdapterT= RutasAdapterT(ArrayList(),this)
        mGridLayout= GridLayoutManager(context,2)
        obtenerdatoscomoturista()
        mBinding.recyclerViewT.apply {
            setHasFixedSize(true)
            layoutManager=mGridLayout
            adapter=mAdapterT
        }

    }

    private fun obtenerDatos() {
        arrayprovincias.clear()
        listarutas.clear()
        val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        var numjuego:Int=0
        val docRef = db.collection("users").document(email!!)
        docRef.get()
            .addOnSuccessListener { document ->
                var numrutas=document.get("Num_rutas") as Long
                var cont=0
                while (cont<numrutas){
                    if (document.get("Rutas.ruta$cont.Nombre").toString().isNotEmpty()){
                        var rutas: Rutas = Rutas(document.get("Rutas.ruta$cont.Nombre").toString(),
                            document.get("Rutas.ruta$cont.Lugar de inicio").toString(),
                            document.get("Rutas.ruta$cont.Provincia").toString(),
                            document.get("Rutas.ruta$cont.Localidad").toString(),
                            document.get("Rutas.ruta$cont.Kms").toString(),cont.toString(),email)

                        listarutas.add(rutas)
                    }
                    cont++
                }
                mAdapter.setStores(listarutas)
            }
            .addOnFailureListener { exception ->

            }
    }

    fun obtenerdatoscomoturista(){
        listarutas.clear()
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
                            mAdapterT.setStores(listarutas)
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
        if (tipo==0){
            val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            var email=prefs?.getString("email",null)
            var intent=Intent(context,MainActivityverruta::class.java).apply {
                putExtra("nombreruta",rutas.titulo)
                putExtra("etprovincia",rutas.provincia)
                putExtra("etkms",rutas.kms)
                putExtra("etlugardeinicio",rutas.lugardeinicio)
                putExtra("localidad",rutas.localidad)
                putExtra("numruta",rutas.numruta)
                putExtra("emailguia",rutas.email)
                putExtra("email",email)
                putExtra("nuevaruta",0)
                putExtra("rutanueva",0)
            }

            startActivity(intent)
        }else{
            val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            var email=prefs?.getString("email",null)
            var intent=Intent(context,MainActivityDetallesderuta::class.java).apply {
                putExtra("nombreruta",rutas.titulo)
                putExtra("etprovincia",rutas.provincia)
                putExtra("etkms",rutas.kms)
                putExtra("emailguia",rutas.email)
                putExtra("tipo","guia")
                putExtra("etlugardeinicio",rutas.lugardeinicio)
                putExtra("localidad",rutas.localidad)
                putExtra("numruta",rutas.numruta)
                putExtra("email",email)
            }

            startActivity(intent)
        }

    }

    override fun editarrutafutura(rutas: RutasFuturas) {
        TODO("Not yet implemented")
    }

    override fun addruta(rutas: Rutas) {

        if (!mBinding.btnguia.isEnabled){
            var dialogo=AlertDialog.Builder(requireContext()).apply {
                setTitle("Informaci??n")
                setCancelable(false)
                setPositiveButton("Aceptar"){ _,i ->
                    val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                    var email=prefs?.getString("email",null)
                    db.collection("users").document(email!!).update(
                        mapOf(
                            "Rutas.ruta${rutas.numruta}.Nombre" to ""
                        )
                    ).addOnSuccessListener {
                        var dialogo=AlertDialog.Builder(requireContext()).apply {
                            setTitle("Informaci??n")
                            setCancelable(false)
                            setPositiveButton("Aceptar",null)
                            setMessage("La ruta se ha borrado correctamente")
                        }.show()
                        setupRecyclerViewG()
                    }
                }
                setNegativeButton("Cancelar",null)
                setMessage("??Desea borrar la ruta?")
            }.show()
        }


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
