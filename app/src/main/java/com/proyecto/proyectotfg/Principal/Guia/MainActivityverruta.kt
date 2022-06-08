package com.proyecto.proyectotfg.Principal.Guia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.proyecto.proyectotfg.FotoPerfil.SnapshotsApplication
import com.proyecto.proyectotfg.R
import com.proyecto.proyectotfg.databinding.ActivityMainActivityverrutaBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivityverruta : AppCompatActivity(){
    private lateinit var mBinding: ActivityMainActivityverrutaBinding
    private val db = Firebase.firestore
    var email2:String?=null
    private lateinit var mSnapshotsStorageRef: StorageReference
    private var mPhotoSelectedUri: Uri? = null
    val galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK) {
            mPhotoSelectedUri = it.data?.data


            postSnapshot(email2?:"",it.data?.data)







        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainActivityverrutaBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        var email=intent.extras?.getString("email")
        var i=intent.extras?.getInt("acabado",0)

            comprobareditar()
            email2=email
            cambiarimagen(email!!)



        mBinding.btnvermonumentos.setOnClickListener {
            val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            var email=prefs?.getString("email",null)
            var numruta=intent.extras?.getString("numruta")
            var intent=Intent(this,MainActivityvistademonumentos::class.java).apply {
                putExtra("email",email)
                putExtra("numruta",numruta)
                putExtra("comproba",1)
                putExtra("nuevaruta",1)
            }

            startActivity(intent)
        }
        mBinding.btnportada.setOnClickListener {
            setupFirebase(email!!)
            openGallery(email)

        }

        mBinding.btnguardar.setOnClickListener {
            var i=intent.extras?.getInt("acabado",0)

                if (mBinding.etNombreruta.text.isNotEmpty()) {
                    if (mBinding.etlugarinicio.text.isNotEmpty()) {
                        if (mBinding.etkms.text.isNotEmpty()) {
                            if (mBinding.etProvincia.text.isNotEmpty()) {
                                if (mBinding.checkBox.isChecked) {
                                    if (mBinding.etLocalidad.text.isNotEmpty()) {
                                        añadirnuevaruta()
                                    } else {
                                        var dialogq = MaterialAlertDialogBuilder(this).apply {
                                            setTitle("Info")
                                            setCancelable(false)
                                            setMessage("El campo Localidad no puede estar vacio")
                                            setPositiveButton("Aceptar") { _, i ->
                                            }

                                        }.show()
                                    }
                                } else {
                                    añadirnuevaruta()
                                }
                            } else {
                                var dialogq = MaterialAlertDialogBuilder(this).apply {
                                    setTitle("Info")
                                    setCancelable(false)
                                    setMessage("El campo Provincia no puede estar vacio")
                                    setPositiveButton("Aceptar") { _, i ->
                                    }

                                }.show()
                            }
                        } else {
                            var dialogq = MaterialAlertDialogBuilder(this).apply {
                                setTitle("Info")
                                setCancelable(false)
                                setMessage("El campo Kms de ruta no puede estar vacio")
                                setPositiveButton("Aceptar") { _, i ->
                                }

                            }.show()
                        }
                    } else {
                        var dialogq = MaterialAlertDialogBuilder(this).apply {
                            setTitle("Info")
                            setCancelable(false)
                            setMessage("El campo Lugar de inicio no puede estar vacio")
                            setPositiveButton("Aceptar") { _, i ->
                            }

                        }.show()
                    }

                } else {
                    var dialogq = MaterialAlertDialogBuilder(this).apply {
                        setTitle("Info")
                        setCancelable(false)
                        setMessage("El campo Nombre de ruta no puede estar vacio")
                        setPositiveButton("Aceptar") { _, i ->
                        }

                    }.show()
                }


            }



    }

    private fun openGallery(email:String) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResult.launch(intent)



    }
    private fun setupFirebase(email:String) {
        db.collection("users").document(email).get().addOnSuccessListener {
            var num=intent.extras?.getString("numruta")
            val prefs=getSharedPreferences("com.exameple.pruebafirebase.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
            var mio=prefs?.getString("email",null)
            mSnapshotsStorageRef = FirebaseStorage.getInstance().reference.child(
                SnapshotsApplication.PATH_RUTAS).child(mio!!).child("ruta$num")
        }

        // mSnapshotsDatabaseRef = FirebaseDatabase.getInstance().reference.child(email)
    }

    fun cambiarimagen(email:String){
        val prefs=getSharedPreferences("com.exameple.pruebafirebase.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        var mio=prefs?.getString("email",null)
        var partes=mio!!.split("@")
        db.collection("users").document(email).get().addOnSuccessListener {
            var num=intent.extras?.getString("numruta")
            Glide.with(this)

                .load("https://firebasestorage.googleapis.com/v0/b/proyectotfg-87d7e.appspot.com/o/Rutas%2F${partes[0]}%40${partes[1]}%2Fruta$num%2Fportada.jpg?alt=media")
                .apply {
                    override(600,460)
                }
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_account_circle)
                .into(mBinding.imgportadruta)

        }



    }

    private fun postSnapshot(email:String,foto: Uri?) {
        if (foto != null) {
            val myStorageRef = mSnapshotsStorageRef.child("portada.jpg")

            myStorageRef.putFile(mPhotoSelectedUri!!)
                .addOnProgressListener {


                }
                .addOnCompleteListener {

                    cambiarimagen(email)

                }
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        }
    }

    private fun comprobareditar() {
        var titulo=intent.extras?.getString("nombreruta")
        var descrip=intent.extras?.getString("etprovincia")
        var fecha=intent.extras?.getString("etkms")
        var lugardeinicio=intent.extras?.getString("etlugardeinicio")
        var localidad=intent.extras?.getString("localidad")

        if (titulo!!.isNotEmpty()){
            if (descrip!!.isNotEmpty()){
                if (fecha!!.isNotEmpty()){
                    mBinding.etNombreruta.setText(titulo)
                    mBinding.etProvincia.setText(descrip)
                    mBinding.etkms.setText(fecha)
                    mBinding.etlugarinicio.setText(lugardeinicio)
                    if (localidad!!.isNotEmpty()){
                        mBinding.etLocalidad.setText(localidad)
                        mBinding.checkBox.callOnClick()
                    }
                }
            }
        }
    }
    fun añadirnuevaruta() {

        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email2=prefs?.getString("email",null)
        db.collection("users").document(email2!!).get().addOnSuccessListener {
            var numrutas=it.get("Num_rutas") as Long
            var cont=0
            var nombreruta=intent?.extras?.getString("nombreruta").toString()
            if (nombreruta.compareTo(mBinding.etNombreruta.text.toString())!=0){
                while (cont<numrutas){

                    if (it.get("Rutas.ruta$cont.Nombre").toString().compareTo(mBinding.etNombreruta.text.toString())==0){
                        cont=-1
                        break
                    }


                    cont++
                }

            }
            if (cont!=-1){
                val email = intent.extras?.getString("email").toString()
                var docref2 = db.collection("users").document(email)
                var docref = db.collection("users").document(email)
                docref.get()
                    .addOnSuccessListener {
                        var numerorutas = intent.extras?.getString("numruta")
                        var numeromonumento = it.get("Rutas.ruta$numerorutas.Num_monumentos")


                        var loca = ""
                        if (mBinding.etNombreruta.text.isNotEmpty()) {
                            loca = mBinding.etLocalidad.text.toString()
                        }
                        docref2.update(
                            mapOf(

                                "Rutas.ruta$numerorutas.Nombre" to mBinding.etNombreruta.text.toString(),
                                "Rutas.ruta$numerorutas.Num_monumentos" to numeromonumento,
                                "Rutas.ruta$numerorutas.Kms" to mBinding.etkms.text.toString(),
                                "Rutas.ruta$numerorutas.Provincia" to mBinding.etProvincia.text.toString(),
                                "Rutas.ruta$numerorutas.Lugar de inicio" to mBinding.etlugarinicio.text.toString(),
                                "Rutas.ruta$numerorutas.Localidad" to loca

                            )
                        ).addOnSuccessListener {
                            finish()
                        }

                    }
            }else{
                var dialogq = MaterialAlertDialogBuilder(this).apply {
                    setTitle("Info")
                    setCancelable(false)
                    setMessage("El nombre de la ruta no puede ser igual que una ya existente")
                    setPositiveButton("Aceptar") { _, i ->
                    }

                }.show()
            }

        }




    }

}