package com.proyecto.proyectotfg.Principal.Guia

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.proyecto.proyectotfg.Adaptadores.MonumentosAdapter
import com.proyecto.proyectotfg.OnClickListener
import com.proyecto.proyectotfg.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.proyectotfg.Clases.*
import com.proyecto.proyectotfg.databinding.ActivityMainActivityvistademonumentosBinding

class MainActivityvistademonumentos : AppCompatActivity() , OnClickListener{


    var listamonumentos=ArrayList<Monumentos>()
    private val db = Firebase.firestore
    private lateinit var mAdapter: MonumentosAdapter
    private lateinit var mGridLayout: GridLayoutManager

    private lateinit var mBinding: ActivityMainActivityvistademonumentosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainActivityvistademonumentosBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        object : CountDownTimer(3000,1000){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                setupRecyclerView()
            }

        }.start()

        mBinding.btnaddnuevomonumento.setOnClickListener {
            var nuevaruta=intent.extras?.getInt("nuevaruta")

                if (nuevaruta==0){
                    crearmonumento()
                }else{
                    crearmonumento2()
                }


            var nombreruta=intent.extras?.getString("nombreruta")
            var numruta=intent.extras?.getString("numruta")
            var email=intent.extras?.getString("email")
            val intent= Intent(this, MainActivityaddmonument::class.java).apply {
                putExtra("nombreruta",nombreruta)
                putExtra("email",email)
                putExtra("compro",0)
                putExtra("nuevo",0)
                putExtra("comproadd",1)
                putExtra("comproba",0)
                putExtra("nuevaruta",nuevaruta)
                putExtra("numruta",numruta)
                putExtra("email",email)
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)


        }
        mBinding.btnguardarmonumentos.setOnClickListener {
            var nuevaruta=intent.extras?.getInt("nuevaruta")
            if (nuevaruta==0){
                val intent= Intent(this, MainActivityadd::class.java).apply {
                    putExtra("acabado",1)
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }else{
                finish()
            }

        }
    }



    private fun setupRecyclerView() {
        mAdapter= MonumentosAdapter(ArrayList(),this)
        mGridLayout= GridLayoutManager(this,1)
        var comrp=intent.extras?.getInt("comproba")

        if (comrp!=null){
            if (comrp==1){

                obtenerDatos2()
            }else{
                obtenerDatos()
            }

        }else{
            obtenerDatos()
        }



        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager=mGridLayout
            adapter=mAdapter
        }

    }
    private fun obtenerDatos() {
        listamonumentos.clear()
        var email=intent.extras?.getString("email")

        val docRef = db.collection("users").document(email!!)
        docRef.get()
            .addOnSuccessListener { document ->
                var numrutas=document.get("Num_rutas") as Long

                numrutas--

                var nummonumentos=document.get("Rutas.ruta$numrutas.Num_monumentos") as Long

                var cont2:Long=0
                while (cont2<nummonumentos){


                    if(document.get("Rutas.ruta$numrutas.Monumentos.monumento$cont2.Nombre").toString().isNotEmpty()){
                        var rutas: Monumentos = Monumentos(document.get("Rutas.ruta$numrutas.Monumentos.monumento$cont2.Breve Drescripcion").toString(),
                            document.get("Rutas.ruta$numrutas.Monumentos.monumento$cont2.Fecha de construccion").toString()
                            ,document.get("Rutas.ruta$numrutas.Monumentos.monumento$cont2.Nombre").toString(),
                            numrutas.toString(),cont2.toString())

                        listamonumentos.add(rutas)
                    }

                    cont2++
                }
                mAdapter.setStores(listamonumentos)
            }
            .addOnFailureListener { exception ->

            }




    }
    private fun obtenerDatos2() {
        listamonumentos.clear()
        var email=intent.extras?.getString("email")

        val docRef = db.collection("users").document(email!!)
        docRef.get()
            .addOnSuccessListener { document ->
                var numrutas=intent.extras?.getString("numruta")

                var nummonumentos=document.get("Rutas.ruta$numrutas.Num_monumentos") as Long

                var cont2:Long=0
                while (cont2<nummonumentos){


                    if(document.get("Rutas.ruta$numrutas.Monumentos.monumento$cont2.Nombre").toString().isNotEmpty()){
                        var rutas: Monumentos = Monumentos(document.get("Rutas.ruta$numrutas.Monumentos.monumento$cont2.Breve Drescripcion").toString(),
                            document.get("Rutas.ruta$numrutas.Monumentos.monumento$cont2.Fecha de construccion").toString()
                            ,document.get("Rutas.ruta$numrutas.Monumentos.monumento$cont2.Nombre").toString(),
                            numrutas.toString(),cont2.toString())

                        listamonumentos.add(rutas)
                    }

                    cont2++
                }
                mAdapter.setStores(listamonumentos)
            }
            .addOnFailureListener { exception ->

            }




    }
    fun crearmonumento(){
        var email=intent.extras?.getString("email").toString()
        var ruta=intent.extras?.getString("nombreruta").toString()
        db.collection("users").document(email).get().addOnSuccessListener {
            var numeroruta = it.get("Num_rutas") as Long
            numeroruta--
            var nummonumentos =
                it.get("Rutas.ruta$numeroruta.Num_monumentos") as Long
            var nuevonum=nummonumentos
            nuevonum++
            db.collection("users").document(email).update(
                mapOf(
                    "Rutas.ruta$numeroruta.Num_monumentos" to nuevonum,
                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Num_fotos" to 0,
                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Nombre" to "",
                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Fecha de construccion" to "",
                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Breve Drescripcion" to "",

                )
            )
        }
    }

    fun crearmonumento2(){
        var email=intent.extras?.getString("email").toString()
        var ruta=intent.extras?.getString("nombreruta").toString()
        db.collection("users").document(email).get().addOnSuccessListener {
            var numeroruta = intent.extras?.getString("numruta")
            var nummonumentos =
                it.get("Rutas.ruta$numeroruta.Num_monumentos") as Long
            var nuevonum=nummonumentos
            nuevonum++
            db.collection("users").document(email).update(
                mapOf(
                    "Rutas.ruta$numeroruta.Num_monumentos" to nuevonum,
                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Num_fotos" to 0,
                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Nombre" to "",
                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Fecha de construccion" to "",
                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Breve Drescripcion" to "",

                    )
            )
        }
    }

    override fun borrarfoto(foto: Fotos) {
        TODO("Not yet implemented")
    }

    override fun editarmonumento(Monumento: Monumentos) {
        var nuevaruta=intent.extras?.getInt("nuevaruta")

        var email=intent.extras?.getString("email")
        val intent= Intent(this, MainActivityaddmonument::class.java).apply {
            putExtra("titulo",Monumento.titulo)
            putExtra("descripcion",Monumento.descripcion)
            putExtra("fechadeconstr",Monumento.fechadeconst)
            putExtra("nummonumento",Monumento.nummonumento)
            putExtra("numruta",Monumento.numruta)
            putExtra("compro",1)
            putExtra("nuevo",1)
            putExtra("nuevaruta",nuevaruta)
            putExtra("comproadd",0)
            putExtra("comproba",1)
            putExtra("email",email)
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
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
        var dialogo= AlertDialog.Builder(this).apply {
            setTitle("Información")
            setCancelable(false)
            setPositiveButton("Aceptar"){ _,i ->
                val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                var email=prefs?.getString("email",null)

                db.collection("users").document(email!!).get().addOnSuccessListener {
                 var numborrados=it.get("Rutas.ruta${fotos.numruta}.Num_borrados") as Long
                 numborrados++
                    db.collection("users").document(email!!).update(
                        mapOf(
                            "Rutas.ruta${fotos.numruta}.Monumentos.monumento${fotos.nummonumento}.Nombre" to "",
                            "Rutas.ruta${fotos.numruta}.Num_borrados" to numborrados
                        )
                    ).addOnSuccessListener {
                        var dialogo= AlertDialog.Builder(context).apply {
                            setTitle("Información")
                            setCancelable(false)
                            setPositiveButton("Aceptar",null)
                            setMessage("El monumento se ha borrado correctamente se ha borrado correctamente")
                        }.show()
                        setupRecyclerView()
                    }
                }


            }
            setNegativeButton("Cancelar",null)
            setMessage("¿Desea borrar el monumento?")
        }.show()
    }

    override fun verchat(chat: Chats) {
        TODO("Not yet implemented")
    }
}