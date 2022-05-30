package com.example.proyectotfg.Principal.Guia

import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.data.LocalUriFetcher
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proyectotfg.Adaptadores.MonumentosAdapter
import com.example.proyectotfg.Adaptadores.MonumentosAdapterVista
import com.example.proyectotfg.Clases.Fotos
import com.example.proyectotfg.Clases.Monumentos
import com.example.proyectotfg.Clases.Rutas
import com.example.proyectotfg.OnClickListener
import com.example.proyectotfg.Principal.Turista.MainActivityChat
import com.example.proyectotfg.R
import com.example.proyectotfg.databinding.ActivityMainDetallesderutaBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivityDetallesderuta : AppCompatActivity() ,OnClickListener{
    var listamonumentos=ArrayList<Monumentos>()
    private val db = Firebase.firestore
    var numeromonumento:Long=0
    var totalmonumentos:Long=0
    var cero:Long=0

    private lateinit var mAdapter: MonumentosAdapterVista
    private lateinit var mGridLayout: GridLayoutManager
    private lateinit var mBinding: ActivityMainDetallesderutaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityMainDetallesderutaBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        rellenarcampos()
        var turista=intent.extras?.getInt("turista",0)
        if (turista==1){
            mBinding.tvdatoguia.visibility=View.VISIBLE
            mBinding.tvNombredeguia.visibility=View.VISIBLE
            mBinding.tvNombredeguia.text=intent.extras?.getString("email")
            mBinding.textView24.visibility=View.VISIBLE
            mBinding.layoutopinion.visibility=View.VISIBLE
            mBinding.btnmensaje.visibility=View.VISIBLE

        }else{
            mBinding.btnmensaje.visibility=View.GONE
            mBinding.tvdatoguia.visibility=View.GONE
            mBinding.tvNombredeguia.visibility=View.GONE
            mBinding.ratingBar.visibility=View.GONE
            mBinding.layoutopinion.visibility=View.GONE
        }
        mBinding.btnanterior.setOnClickListener {
            if (numeromonumento!=cero){
                numeromonumento--
                setupRecyclerView(0)
            }
        }

        mBinding.btnmensaje.setOnClickListener {
            crearchatmio()

        }

        mBinding.btnsiguiente.setOnClickListener {
            if (numeromonumento<totalmonumentos-1){
                numeromonumento++
                setupRecyclerView(1)
            }
        }
        mBinding.btnopinar.setOnClickListener {
            Comprobar()
        }
    }

    fun crearchatmio(){
        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        var guia=intent.extras?.getString("email").toString()
        Log.d("guia",guia)

        db.collection("users").document(email!!).get().addOnSuccessListener {

            var numchat = it.get("Num_chat") as Long
            var nuevo=numchat
            nuevo++
            var cont=0
            while (cont<numchat){
                if (it.get("Chats.chat$cont.Receptor").toString()==guia){
                    cont=-1
                    break
                }
                Log.d("mensajes","dentro $cont")
                cont++
            }

            Log.d("mensajes",cont.toString())
            if (cont!=-1){
                db.collection("users").document(email).update(
                    mapOf(
                        "Chats.chat$numchat.Num_mensajes" to 0,
                        "Chats.chat$numchat.Receptor" to guia,
                        "Num_chat" to nuevo
                    )
                )
                crearchatguia()
            }else{
                AlertDialog.Builder(this).apply {
                    setTitle("Info")
                    setMessage("Esa conversación ya existe")
                    setPositiveButton("Aceptar",null)
                }.show()
            }


        }



    }

    fun crearchatguia(){
        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        var guia=intent.extras?.getString("guia").toString()
        Log.d("guia",guia)

        db.collection("users").document(guia).get().addOnSuccessListener {

            var numchat = it.get("Num_chat") as Long
            var nuevo=numchat
            nuevo++
            var cont=0
            while (cont<numchat){
                if (it.get("Chats.chat$cont.Receptor").toString()==email){
                    cont=-1
                    break
                }
                cont++
            }
            if (cont!=-1){
                db.collection("users").document(guia).update(
                    mapOf(
                        "Chats.chat$numchat.Num_mensajes" to 0,
                        "Chats.chat$numchat.Receptor" to email,
                        "Num_chat" to nuevo
                    )
                )
                var persona=intent.extras?.getString("email").toString()
                val intent= Intent(this, MainActivityChat::class.java).apply {
                    putExtra("guia",persona)
                }
                startActivity(intent)
            }


        }



    }



    private fun Comprobar() {
        var email=intent.extras?.getString("email").toString()

        var numruta=intent.extras?.getString("numruta").toString()
        var nombreruta=intent.extras?.getString("nombreruta").toString()
        var persona=intent.extras?.getString("persona").toString()
        var valor=mBinding.ratingBar.rating
        db.collection("users").document(email).get().addOnSuccessListener {
            var numopiniones=it.get("Rutas.ruta$numruta.Num_opiniones") as Long
            var cont:Long=0
            var comprobante:Long=-1
            while (cont<numopiniones){
                Log.d("opinion","${it.get("Rutas.ruta$numruta.Opiniones.opinion$cont.persona").toString()}//////$persona")
                if (it.get("Rutas.ruta$numruta.Opiniones.opinion$cont.persona").toString()==persona){

                        db.collection("users").document(email).update(
                            mapOf(
                                "Rutas.ruta$numruta.Opiniones.opinion$cont.valor" to valor,
                                )
                        )


                    cont=-1
                    break
                }
                cont++
            }
            if (cont!=comprobante){
                db.collection("users").document(email).get().addOnSuccessListener {
                    var numopiniones1=it.get("Rutas.ruta$numruta.Num_opiniones") as Long
                    var nuevo=numopiniones1
                    nuevo++
                    db.collection("users").document(email).update(
                        mapOf(
                            "Rutas.ruta$numruta.Num_opiniones" to nuevo,
                            "Rutas.ruta$numruta.Opiniones.opinion$numopiniones1.nombreruta" to nombreruta,
                            "Rutas.ruta$numruta.Opiniones.opinion$numopiniones1.persona" to persona,
                            "Rutas.ruta$numruta.Opiniones.opinion$numopiniones1.valor" to valor,
                            "Rutas.ruta$numruta.Opiniones.opinion$numopiniones1.numruta" to numruta,

                            )
                    )
                }
            }else{

            }
        }
    }

    private fun rellenarcampos() {
        var titulo=intent.extras?.get("nombreruta").toString()
        var Provincia=intent.extras?.get("etprovincia").toString()
        var Localidad=intent.extras?.get("localidad").toString()
        var Lugar=intent.extras?.get("etlugardeinicio").toString()
        var Distancia=intent.extras?.get("etkms").toString()
        if (titulo.isNotEmpty()){
            if (Provincia.isNotEmpty()){
                if (Lugar.isNotEmpty()){
                    if (Distancia.isNotEmpty()){
                        if (Localidad.isNotEmpty()){
                            cambiarimagen()
                            setupRecyclerView(0)
                            mBinding.tvNombredetalles.text=titulo
                            mBinding.tvProvinciadetalles.text=Provincia
                            mBinding.tvLocalidaddetalles.text=Localidad
                            mBinding.tvLugardeiniciodetalles.text=Lugar
                            mBinding.tvDistancia.text=Distancia
                        }else{
                            cambiarimagen()
                            setupRecyclerView(0)
                            mBinding.tvLocalidaddetalles.visibility= View.GONE
                            mBinding.textView27.visibility= View.GONE
                            mBinding.tvNombredetalles.text=titulo
                            mBinding.tvProvinciadetalles.text=Provincia
                            mBinding.tvLugardeiniciodetalles.text=Lugar
                            mBinding.tvDistancia.text="$Distancia kms"

                        }
                    }
                }
            }
        }
    }

    private fun cambiarimagen() {
        var email=intent.extras?.getString("email")
        db.collection("users").document(email!!).get().addOnSuccessListener {
            var num=intent.extras?.getString("numruta")
            Glide.with(this)

                .load("https://firebasestorage.googleapis.com/v0/b/proyectotfg-87d7e.appspot.com/o/Rutas%2Fruta$num%2Fportada.jpg?alt=media")
                .apply {
                    override(600,460)
                }
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_account_circle)
                .into(mBinding.imgruta)

        }
    }

    private fun setupRecyclerView(boton: Int) {
        mAdapter= MonumentosAdapterVista(ArrayList(),this)
        mGridLayout= GridLayoutManager(this,1)
        obtenerDatos2(boton)
        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager=mGridLayout
            adapter=mAdapter
        }

    }

    private fun obtenerDatos2(boton:Int) {
        listamonumentos.clear()
        var email=intent.extras?.getString("email")
        Log.d("Datos",email.toString())
        val docRef = db.collection("users").document(email!!)
        docRef.get()
            .addOnSuccessListener { document ->
                var numrutas=intent.extras?.getString("numruta")
                totalmonumentos=document.get("Rutas.ruta$numrutas.Num_monumentos") as Long
                var nombre=document.get("Rutas.ruta$numrutas.Monumentos.monumento$numeromonumento.Nombre").toString()
                var total=document.get("Rutas.ruta$numrutas.Num_borrados") as Long
                if (totalmonumentos==total){
                    mBinding.textView31.text="No hay monumentos"
                    mBinding.layoutmonumentos.visibility=View.GONE
                }else{
                    mBinding.layoutmonumentos.visibility=View.VISIBLE
                    if (nombre.isEmpty()){
                        if (boton==0){
                            numeromonumento--
                        }
                        if (boton==1){
                            numeromonumento++
                        }

                    }
                    if (numeromonumento<totalmonumentos){
                        var nombre=document.get("Rutas.ruta$numrutas.Monumentos.monumento$numeromonumento.Nombre").toString()

                        Log.d("comprobacion",nombre)
                        if(nombre.isNotEmpty()){
                            var rutas: Monumentos = Monumentos(document.get("Rutas.ruta$numrutas.Monumentos.monumento$numeromonumento.Breve Drescripcion").toString(),
                                document.get("Rutas.ruta$numrutas.Monumentos.monumento$numeromonumento.Fecha de construccion").toString()
                                ,document.get("Rutas.ruta$numrutas.Monumentos.monumento$numeromonumento.Nombre").toString(),
                                numrutas.toString(),numeromonumento.toString())

                            listamonumentos.add(rutas)
                        }
                    }



                    mAdapter.setStores(listamonumentos)
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
        val dialog=layoutInflater.inflate(R.layout.dialogomonumento,null)
        dialog.findViewById<TextView>(R.id.tvNombredialogo).text=Monumento.titulo
        dialog.findViewById<TextView>(R.id.tvconstrucciondialogo).text=Monumento.fechadeconst
        dialog.findViewById<TextView>(R.id.tvbrevedescripciondialogo).text=Monumento.descripcion

            Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/proyectotfg-87d7e.appspot.com/o/" +
                        "Rutas%2Fruta${Monumento.numruta}%2FMonumentos%2Fmonumento${Monumento.nummonumento}%2Fportada.jpg?alt=media")
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(dialog.findViewById<ImageView>(R.id.imgmonumento))


        var dialogq = MaterialAlertDialogBuilder(this).apply {
            setTitle("Nueva ruta")
            setView(dialog)
            setCancelable(false)
            setPositiveButton("Aceptar",null)
            setNegativeButton("Cancelar",null)

        }.show()
    }

    override fun editarruta(rutas: Rutas) {
        TODO("Not yet implemented")
    }

    override fun addruta(rutas: Rutas) {
        TODO("Not yet implemented")
    }

    override fun borrarmonumento(fotos: Monumentos) {
        TODO("Not yet implemented")
    }
}