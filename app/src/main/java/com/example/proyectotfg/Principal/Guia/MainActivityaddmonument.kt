package com.example.proyectotfg.Principal.Guia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proyectotfg.Adaptadores.FotosAdapter
import com.example.proyectotfg.Adaptadores.MonumentosAdapter
import com.example.proyectotfg.Adaptadores.RutasAdapter
import com.example.proyectotfg.Clases.Fotos
import com.example.proyectotfg.Clases.Monumentos
import com.example.proyectotfg.Clases.Rutas
import com.example.proyectotfg.FotoPerfil.SnapshotsApplication
import com.example.proyectotfg.Login.MainActivity
import com.example.proyectotfg.OnClickListener
import com.example.proyectotfg.R
import com.example.proyectotfg.databinding.ActivityMainActivityaddBinding
import com.example.proyectotfg.databinding.DialogoAddmonumentoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivityaddmonument : AppCompatActivity() , OnClickListener{
    var portada:Int=0
    var email2:String?=null
    private val db = Firebase.firestore
    private lateinit var mAdapter: MonumentosAdapter
    private lateinit var mGridLayout: GridLayoutManager
    private lateinit var mAdapter2: FotosAdapter
    private lateinit var mGridLayout2: GridLayoutManager
    private lateinit var mSnapshotsStorageRef: StorageReference
    private lateinit var mSnapshotsStorageRef2: StorageReference

var compro=0
    var fotosvista=0
    var listafotos=ArrayList<Fotos>()
    private var mPhotoSelectedUri: Uri? = null
    val galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK) {
            mPhotoSelectedUri = it.data?.data

            postSnapshot(email2?:"",it.data?.data)

        }
    }


    private lateinit var mBinding: DialogoAddmonumentoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogoAddmonumentoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        compro= intent.extras?.getInt("compro")!!
        if (compro==1){
            compro=0
            cambiarimagen2()
            comprobareditar()
        }else if(compro==0){

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

                            "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Num_fotos" to 0,
                            "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Nombre" to "",
                            "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Fecha de construccion" to "",
                            "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Breve Drescripcion" to "",
                            "Rutas.ruta$numeroruta.Num_monumentos" to nuevonum
                        )
                    )
                }

        }


        mBinding.btnguardarmonumento.setOnClickListener {
            if (mBinding.editTextTextPersonName2.text.isNotEmpty()){
                if (mBinding.editTextNumber.text.isNotEmpty()){
                    if (mBinding.editTextTextMultiLine.text.isNotEmpty()){
                        if (compro==0){
                            guardarmonumento()
                        }else{
                            editarmonumento()
                        }

                        var email=intent.extras?.getString("email")
                        val intent= Intent(this, MainActivityvistademonumentos::class.java).apply {
                            putExtra("email",email)
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)

                    }else{
                        var dialogq = MaterialAlertDialogBuilder(this).apply {
                            setTitle("Info")
                            setCancelable(false)
                            setMessage("El campo Breve descripción de ruta no puede estar vacio")
                            setPositiveButton("Aceptar") { _, i ->
                            }

                        }.show()
                    }
                }else{
                    var dialogq = MaterialAlertDialogBuilder(this).apply {
                        setTitle("Info")
                        setCancelable(false)
                        setMessage("El campo fecha de construcción no puede estar vacio")
                        setPositiveButton("Aceptar") { _, i ->
                        }

                    }.show()
                }
            }else{
                var dialogq = MaterialAlertDialogBuilder(this).apply {
                    setTitle("Info")
                    setCancelable(false)
                    setMessage("El campo Nombre de monumento no puede estar vacio")
                    setPositiveButton("Aceptar") { _, i ->
                    }

                }.show()
            }

        }


            mBinding.btnaddportadamonuemto.setOnClickListener {
                if (mBinding.editTextTextPersonName2.text.isNotEmpty()){
                    var nombreruta=intent.extras?.getString("nombreruta").toString()
                    portada=0
                    openGallery()
                    setupFirebase(nombreruta)
                }else{
                    var dialogq = MaterialAlertDialogBuilder(this).apply {
                        setTitle("Nueva ruta")
                        setCancelable(false)
                        setMessage("Para añadir una foto de portada, el campo Nombre de monumento debe estar relleno")
                        setPositiveButton("Aceptar",null)
                        setNegativeButton("Cancelar",null)

                    }.show()
                }


            }

        mBinding.btnverfotos.setOnClickListener {
            if (fotosvista==0){
                mBinding.layoutdatos.visibility=View.GONE
                mBinding.layoutportada.visibility=View.GONE
                mBinding.recyclerViewfotos.visibility=View.VISIBLE
                mBinding.btnverfotos.text="VER DATOS"
                fotosvista=1
            }
            else if (fotosvista==1){
                mBinding.layoutdatos.visibility=View.VISIBLE
                mBinding.layoutportada.visibility=View.VISIBLE
                mBinding.recyclerViewfotos.visibility=View.GONE
                mBinding.btnverfotos.text="VER FOTOS"
                fotosvista=0
            }


        }
        mBinding.btnaddfoto.setOnClickListener {
            if (mBinding.editTextTextPersonName2.text.isNotEmpty()){
                var nombreruta=intent.extras?.getString("nombreruta").toString()
                portada=1
                openGallery()
                setupFirebase(nombreruta)
            }else{
                var dialogq = MaterialAlertDialogBuilder(this).apply {
                    setTitle("Nueva ruta")
                    setCancelable(false)
                    setMessage("Para añadir una nueva foto, el campo Nombre de monumento debe estar relleno")
                    setPositiveButton("Aceptar",null)
                    setNegativeButton("Cancelar",null)

                }.show()
            }

        }
    }

    private fun comprobareditar() {
        var titulo=intent.extras?.getString("titulo")
        var descrip=intent.extras?.getString("descripcion")
        var fecha=intent.extras?.getString("fechadeconstr")

        if (titulo!!.isNotEmpty()){
            if (descrip!!.isNotEmpty()){
                if (fecha!!.isNotEmpty()){
                    mBinding.editTextTextPersonName2.setText(titulo)
                    mBinding.editTextNumber.setText(fecha)
                    mBinding.editTextTextMultiLine.setText(descrip)
                }
            }
        }
    }

    fun cambiarimagen() {

        var email = intent.extras?.getString("email").toString()
        db.collection("users").document(email).get().addOnSuccessListener {
            var num = it.get("Num_rutas").toString().toInt()
            num--
            var num2 = it.get("Rutas.ruta$num.Num_monumentos").toString().toInt()
            num2--
            Glide.with(this)

                .load(
                    "https://firebasestorage.googleapis.com/v0/b/proyectotfg-87d7e.appspot.com/o/Rutas%2F" +
                            "ruta$num%2FMonumentos%2Fmonumento$num2%2Fportada.jpg?alt=media"
                )
                .apply {
                    override(600, 460)
                }
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_account_circle)
                .into(mBinding.imgportadamonument)
        }
    }

        fun cambiarimagen2(){

            var email=intent.extras?.getString("email").toString()
            db.collection("users").document(email).get().addOnSuccessListener {
                var num=it.get("Num_rutas").toString().toInt()
                num--
                var num2=it.get("Rutas.ruta$num.Num_monumentos").toString().toInt()
                num2--
                Glide.with(this)

                    .load("https://firebasestorage.googleapis.com/v0/b/proyectotfg-87d7e.appspot.com/o/Rutas%2F" +
                            "ruta$num%2FMonumentos%2Fmonumento$num2%2Fportada.jpg?alt=media")
                    .apply {
                        override(600,460)
                    }
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.ic_account_circle)
                    .into(mBinding.imgportadamonument)
            }


    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResult.launch(intent)



    }
    private fun setupFirebase(nombreruta:String) {
        var email=intent.extras?.getString("email").toString()
        db.collection("users").document(email).get().addOnSuccessListener {
            var num=it.get("Num_rutas").toString().toInt()
            num--
            var num2=it.get("Rutas.ruta$num.Num_monumentos").toString().toInt()
            num2--
            mSnapshotsStorageRef = FirebaseStorage.getInstance().reference.child(SnapshotsApplication.PATH_RUTAS).child("ruta$num").child("Monumentos").child("monumento$num2")
        }

        // mSnapshotsDatabaseRef = FirebaseDatabase.getInstance().reference.child(email)
    }
    private fun postSnapshot(email:String,foto: Uri?) {

        if (foto != null) {
            if (portada==0){
                val myStorageRef = mSnapshotsStorageRef.child("portada.jpg")

                myStorageRef.putFile(mPhotoSelectedUri!!)
                    .addOnProgressListener {


                    }
                    .addOnCompleteListener {

                        cambiarimagen()

                    }
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener {
                        Log.d("Foto","Fallo")
                    }
            }else{
                var email=intent.extras?.getString("email").toString()
                var ruta=intent.extras?.getString("nombreruta").toString()
                db.collection("users").document(email).get().addOnSuccessListener {
                    var numeroruta=it.get("Num_rutas") as Long
                    numeroruta--
                    var nummonumentos=it.get("Rutas.ruta$numeroruta.Num_monumentos") as Long
                    nummonumentos--
                    var numerofotos=it.get("Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Num_fotos") as Long
                    var nuevonumfotos=numerofotos
                    nuevonumfotos++
                    val myStorageRef = mSnapshotsStorageRef.child("$numerofotos.jpg")
                    db.collection("users").document(email).update(
                        mapOf(

                            "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentos.Num_fotos" to nuevonumfotos
                        )
                    )
                    myStorageRef.putFile(mPhotoSelectedUri!!)
                        .addOnProgressListener {


                        }
                        .addOnCompleteListener {
                        mBinding.btnverfotos.visibility=View.VISIBLE
                        setupRecyclerViewfotos()


                        }
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener {
                            Log.d("Foto","Fallo")
                        }
                }

            }

        }
    }


    fun guardarmonumento(){
        var email=intent.extras?.getString("email").toString()
        var ruta=intent.extras?.getString("nombreruta").toString()
        db.collection("users").document(email).get().addOnSuccessListener {
            var numeroruta = it.get("Num_rutas") as Long
            numeroruta--
            var nummonumentos =
                it.get("Rutas.ruta$numeroruta.Num_monumentos") as Long
            var nummonumentoanterior=nummonumentos
            nummonumentoanterior--
            nummonumentos++
            db.collection("users").document(email).update(
                mapOf(
                    "Rutas.ruta$numeroruta.Num_monumentos" to nummonumentos,
                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentoanterior.Nombre" to mBinding.editTextTextPersonName2.text.toString(),
                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentoanterior.Fecha de construccion" to mBinding.editTextNumber.text.toString(),
                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentoanterior.Breve Drescripcion" to mBinding.editTextTextMultiLine.text.toString(),

                )
            ).addOnCompleteListener{


            }
        }
    }

    fun editarmonumento(){
        var email=intent.extras?.getString("email").toString()
        var ruta=intent.extras?.getString("nombreruta").toString()
        db.collection("users").document(email).get().addOnSuccessListener {
            var numeroruta = it.get("Num_rutas") as Long
            numeroruta--

            var nummonumentoanterior=intent.extras?.get("nummonumento")
            db.collection("users").document(email).update(
                mapOf(


                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentoanterior.Nombre" to mBinding.editTextTextPersonName2.text.toString(),
                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentoanterior.Fecha de construccion" to mBinding.editTextNumber.text.toString(),
                    "Rutas.ruta$numeroruta.Monumentos.monumento$nummonumentoanterior.Breve Drescripcion" to mBinding.editTextTextMultiLine.text.toString(),

                    )
            ).addOnCompleteListener{


            }
        }
    }


    private fun setupRecyclerViewfotos() {
        mAdapter2= FotosAdapter(ArrayList(),this)
        mGridLayout2= GridLayoutManager(this,2)

        obtenerDatosfotos()


        mBinding.recyclerViewfotos.apply {
            setHasFixedSize(true)
            layoutManager=mGridLayout2
            adapter=mAdapter2
        }

    }

    private fun obtenerDatosfotos() {
        var nombreruta=intent.extras?.getString("nombreruta").toString().trim().split(" ")
        var real=""
        var o=0
        for (i in nombreruta){
            if (o==(nombreruta.size-1)){
                real+=i
            }else{
                real+=i+"%20"
            }
            Log.d("real",real)
            o++
        }

        Log.d("Fotos","$listafotos")
        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        var numjuego:Int=0
        val docRef = db.collection("users").document(email!!)
        docRef.get()
            .addOnSuccessListener { document ->
                var numrutas=document.get("Num_rutas") as Long
                var cont=numrutas
                cont--
                listafotos.clear()

                var nummonumentos=document.get("Rutas.ruta$cont.Num_monumentos") as Long
                var cont2=nummonumentos
                cont2--
                    var numerofotos=document.get("Rutas.ruta$cont.Monumentos.monumento$cont2.Num_fotos").toString()
                var cont1:Long=0

                if (numerofotos.isNotEmpty()){
                    while (cont1<numerofotos.toLong()){

                            var rutas: Fotos = Fotos(mBinding.editTextTextPersonName2.text.toString(),real,cont1)

                            listafotos.add(rutas)



                        cont1++
                    }
                }
                Log.d("Fotos"," Despues $listafotos")
                mAdapter2.setStores(listafotos)
            }
            .addOnFailureListener { exception ->
                Log.d("rutas","fallo")
            }




    }

    override fun borrarfoto(foto: Fotos) {

        var ruta="/Rutas/${foto.nombreruta}/Monumentos/${foto.nombremonumento}/${foto.num}.jpg"
        mSnapshotsStorageRef2=FirebaseStorage.getInstance().reference.child(ruta)
        Log.d("Fotos",ruta)






        mSnapshotsStorageRef2.delete().addOnSuccessListener {


        }.addOnCompleteListener{
            Log.d("Fotos","Bien")
            mAdapter2.borrar(foto)
setupRecyclerViewfotos()

        }
            .addOnFailureListener{
                Log.d("Fotos","Mal")
        }


    }

    override fun editarmonumento(Monumento: Monumentos) {

    }

    override fun editarruta(rutas: Rutas) {

    }
}