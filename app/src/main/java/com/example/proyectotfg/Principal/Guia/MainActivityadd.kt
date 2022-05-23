package com.example.proyectotfg.Principal.Guia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proyectotfg.FotoPerfil.SnapshotsApplication
import com.example.proyectotfg.Login.MainActivity
import com.example.proyectotfg.R
import com.example.proyectotfg.databinding.ActivityMainActivityaddBinding
import com.example.proyectotfg.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivityadd : AppCompatActivity() {
    var email2:String?=null
    private val db = Firebase.firestore
    private lateinit var mSnapshotsStorageRef: StorageReference
    private var mPhotoSelectedUri: Uri? = null
    val galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK) {
            mPhotoSelectedUri = it.data?.data


            postSnapshot(email2?:"",it.data?.data)







        }
    }


    private lateinit var mBinding: ActivityMainActivityaddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainActivityaddBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        var i=intent.extras?.getInt("acabado",0)
        if (i==1){
            finish()

        }
        val email=intent.extras?.getString("email").toString()
        email2=email
        var nombreruta=intent.extras?.getString("nombreruta").toString()
        mBinding.etNombreruta.setText(nombreruta)
        mBinding.checkBox.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                mBinding.etLocalidad.visibility= View.VISIBLE
                mBinding.tvLocalidad.visibility=View.VISIBLE
            }else{
                mBinding.etLocalidad.visibility= View.INVISIBLE
                mBinding.tvLocalidad.visibility=View.INVISIBLE
            }
        }
        //cambiarimagen(email)
        mBinding.btnportada.setOnClickListener {
            if (mBinding.etNombreruta.text.isNotEmpty()){
                setupFirebase(email?:"")
                openGallery(email?:"")
            }else{
                var dialogq = MaterialAlertDialogBuilder(this).apply {
                    setTitle("Info")
                    setCancelable(false)
                    setMessage("El Nombre de ruta no puede estar vacio, para poder cambiar o añadir una foto a la portada")
                    setPositiveButton("Aceptar") { _, i ->
                    }

                }.show()
            }

        }
        mBinding.btnguardar.setOnClickListener {
            if (mBinding.etNombreruta.text.isNotEmpty()){
                if (mBinding.etlugarinicio.text.isNotEmpty()){
                    if (mBinding.etkms.text.isNotEmpty()){
                        if (mBinding.etProvincia.text.isNotEmpty()){
                            if (mBinding.checkBox.isChecked){
                                if (mBinding.etLocalidad.text.isNotEmpty()){
                                    añadirnuevaruta()
                                }else{
                                    var dialogq = MaterialAlertDialogBuilder(this).apply {
                                        setTitle("Info")
                                        setCancelable(false)
                                        setMessage("El campo Localidad no puede estar vacio")
                                        setPositiveButton("Aceptar") { _, i ->
                                        }

                                    }.show()
                                }
                            }else{
                                añadirnuevaruta()
                            }
                        }else{
                            var dialogq = MaterialAlertDialogBuilder(this).apply {
                                setTitle("Info")
                                setCancelable(false)
                                setMessage("El campo Provincia no puede estar vacio")
                                setPositiveButton("Aceptar") { _, i ->
                                }

                            }.show()
                        }
                    }else{
                        var dialogq = MaterialAlertDialogBuilder(this).apply {
                            setTitle("Info")
                            setCancelable(false)
                            setMessage("El campo Kms de ruta no puede estar vacio")
                            setPositiveButton("Aceptar") { _, i ->
                            }

                        }.show()
                    }
                }else{
                    var dialogq = MaterialAlertDialogBuilder(this).apply {
                        setTitle("Info")
                        setCancelable(false)
                        setMessage("El campo Lugar de inicio no puede estar vacio")
                        setPositiveButton("Aceptar") { _, i ->
                        }

                    }.show()
                }

            }else{
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
    fun cambiarimagen(email:String){
        db.collection("users").document(email).get().addOnSuccessListener {
            var num=it.get("Num_rutas").toString()
            Glide.with(this)

                .load("https://firebasestorage.googleapis.com/v0/b/proyectotfg-87d7e.appspot.com/o/Rutas%2Fruta$num%2Fportada.jpg?alt=media")
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
    private fun openGallery(email:String) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResult.launch(intent)



    }
    private fun setupFirebase(email:String) {
        db.collection("users").document(email).get().addOnSuccessListener {
            var num=it.get("Num_rutas").toString()
            mSnapshotsStorageRef = FirebaseStorage.getInstance().reference.child(SnapshotsApplication.PATH_RUTAS).child("ruta$num")
        }

        // mSnapshotsDatabaseRef = FirebaseDatabase.getInstance().reference.child(email)
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
                    Log.d("Foto","Fallo")
                }
        }
    }

    fun añadirnuevaruta(){
        val email=intent.extras?.getString("email").toString()
        var docref2=db.collection("users").document(email)
        var docref=db.collection("users").document(email)
        docref.get()
            .addOnSuccessListener {
                var numerorutas=it.get("Num_rutas")
                var nuevonumerorutas:Long=numerorutas as Long
                nuevonumerorutas++
                var loca=""
                if (mBinding.etNombreruta.text.isNotEmpty()){
                    loca=mBinding.etLocalidad.text.toString()
                }
                docref2.update(
                    mapOf(
                        "Num_rutas" to nuevonumerorutas,
                        "Rutas.ruta$numerorutas.Nombre" to mBinding.etNombreruta.text.toString(),
                        "Rutas.ruta$numerorutas.Num_monumentos" to 0,
                        "Rutas.ruta$numerorutas.Kms" to mBinding.etkms.text.toString(),
                        "Rutas.ruta$numerorutas.Provincia" to mBinding.etProvincia.text.toString(),
                        "Rutas.ruta$numerorutas.Lugar de inicio" to mBinding.etlugarinicio.text.toString(),
                        "Rutas.ruta$numerorutas.Localidad" to loca

                    )
                ).addOnSuccessListener {
                    var dialogq = MaterialAlertDialogBuilder(this).apply {
                        setTitle("Nueva ruta")
                        setCancelable(false)
                        setMessage("¿Desea añadir monumentos a la ruta?")
                        setPositiveButton("Aceptar") { _, i ->

                            val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                            var email=prefs?.getString("email",null)
                            val intent= Intent(context, MainActivityaddmonument::class.java).apply {
                                putExtra("nombreruta",mBinding.etNombreruta.text.toString())
                                putExtra("email",email)
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }
                        setNegativeButton("Cancelar"){_, i ->
                            finish()
                        }

                    }.show()
                }
            }
            .addOnFailureListener{

            }
    }
}