package com.proyecto.proyectotfg.Principal.Guia

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.proyecto.proyectotfg.Login.MainActivity
import com.proyecto.proyectotfg.R
import com.proyecto.proyectotfg.FotoPerfil.SnapshotsApplication
import com.proyecto.proyectotfg.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileFragmentG : Fragment() {



    var email2:String?=null
    private lateinit var mBinding: FragmentProfileBinding
    private val db = Firebase.firestore
    private lateinit var mSnapshotsStorageRef: StorageReference
    private var mPhotoSelectedUri: Uri? = null
    val galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK) {
            mPhotoSelectedUri = it.data?.data


                postSnapshot(email2?:"",it.data?.data)







        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs =
            activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email = prefs?.getString("email", null)
        email2 = email
        cerrarsesion(email ?: "")
        cargardatos(email ?: "")

        cambiarimagen(email ?: "")
        mBinding.btnManual.setOnClickListener {
                    val dialog = layoutInflater.inflate(R.layout.botonesmanual, null)
                    var dial = AlertDialog.Builder(requireContext()).apply {
                        setTitle("Ayuda para el usuario")
                        setPositiveButton("Cerrar", null)
                        setView(dialog)
                        dialog.findViewById<Button>(R.id.btninicio).setOnClickListener {
                            val dialogi = layoutInflater.inflate(R.layout.ayudahome, null)
                            var dia = AlertDialog.Builder(requireContext()).apply {
                                setTitle("Ayuda de Guía")
                                setView(dialogi)
                                setPositiveButton("Cerrar",null)
                                dialogi.findViewById<Button>(R.id.btng).setOnClickListener {
                                    dialogi.findViewById<Button>(R.id.btng).isEnabled = false
                                    dialogi.findViewById<Button>(R.id.btnturista).isEnabled = true
                                    dialogi.findViewById<LinearLayout>(R.id.layoutguia).visibility =
                                        View.VISIBLE
                                    dialogi.findViewById<LinearLayout>(R.id.layoutturista).visibility =
                                        View.GONE
                                }
                                dialogi.findViewById<Button>(R.id.btnturista).setOnClickListener {
                                    dialogi.findViewById<Button>(R.id.btnturista).isEnabled = false
                                    dialogi.findViewById<Button>(R.id.btng).isEnabled = true
                                    dialogi.findViewById<LinearLayout>(R.id.layoutguia).visibility =
                                        View.GONE
                                    dialogi.findViewById<LinearLayout>(R.id.layoutturista).visibility =
                                        View.VISIBLE
                                }
                            }.show()
                        }
                        dialog.findViewById<Button>(R.id.btnchats).setOnClickListener {
                            val dialogc = layoutInflater.inflate(R.layout.ayudachat, null)
                            var dia = AlertDialog.Builder(requireContext()).apply {
                                setTitle("Ayuda de Guía")
                                setView(dialogc)
                                setPositiveButton("Cerrar",null)
                            }.show()
                        }
                        dialog.findViewById<Button>(R.id.btnopiniones).setOnClickListener {
                            val dialogo = layoutInflater.inflate(R.layout.ayudaopiniones, null)
                            var dia = AlertDialog.Builder(requireContext()).apply {
                                setTitle("Ayuda de Guía")
                                setView(dialogo)
                                setPositiveButton("Cerrar",null)
                            }.show()
                        }
                        dialog.findViewById<Button>(R.id.btnperfil).setOnClickListener {
                            val dialop = layoutInflater.inflate(R.layout.ayudaperfil, null)
                            var dia = AlertDialog.Builder(requireContext()).apply {
                                setTitle("Ayuda de Guía")
                                setView(dialop)
                                setPositiveButton("Cerrar",null)
                            }.show()
                        }
                    }.show()









        }
        mBinding.btnCambiarPass.setOnClickListener{
            cambiodecontraseña(email?:"")
        }
        mBinding.imageButton.setOnClickListener {
            var dialogq = MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("Advertencia")
                setCancelable(false)
                setMessage("¿Deseas cambias la foto de perfil?")
                setPositiveButton("Aceptar") { _, i ->
                    setupFirebase(email?:"")
                    openGallery(email?:"")
                }
                setNegativeButton("Cancelar",null)

            }.show()






        }

    }

    fun cambiodecontraseña(email:String){


            var dialogq = MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("Info")
                setMessage("Comprueba la bandeja de entrada de su correo, si no aparece, mire en la carpeta de Spam")
                setPositiveButton("Aceptar") { _, i ->
                        FirebaseAuth.getInstance()
                            .sendPasswordResetEmail(email)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Datos enviados correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Ha ocurrido un error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                }

            }.show()
    }


    fun cargardatos(email:String){
        val docRef = db.collection("users").document(email)
        docRef.get()
            .addOnSuccessListener { document ->
                mBinding.tvNombre.text="${document.get("nombre")} ${document.get("Primer Apellido")} ${document.get("Segundo Apellido")}"
                mBinding.tvEmail.text=document.id
                mBinding.tvTelefono.text=document.get("Telefono").toString()
            }
            .addOnFailureListener { exception ->
            }
    }

    fun cambiarimagen(email:String){
            Glide.with(requireContext())

                .load("https://firebasestorage.googleapis.com/v0/b/proyectotfg-87d7e.appspot.com/o/Perfiles%2F$email?alt=media")
                .apply {
                    override(600,460)
                }
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_account_circle)
                .into(mBinding.imageButton)

    }

    private fun cerrarsesion(email:String) {



        var logOutButton: Button =mBinding.btnCerrarSesion
        logOutButton.setOnClickListener {
            val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)!!.edit()
            prefs.clear()
            prefs.apply()
            FirebaseAuth.getInstance().signOut()
            val intent= Intent(context, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    fun onBackPressed() {

    }

    private fun openGallery(email:String) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResult.launch(intent)



    }
    private fun setupFirebase(email:String) {
        mSnapshotsStorageRef = FirebaseStorage.getInstance().reference.child(SnapshotsApplication.PATH_PERFILES)
       // mSnapshotsDatabaseRef = FirebaseDatabase.getInstance().reference.child(email)
    }
    private fun postSnapshot(email:String,foto:Uri?) {
        if (foto != null) {

            val myStorageRef = mSnapshotsStorageRef.child(email)

            myStorageRef.putFile(mPhotoSelectedUri!!)
                .addOnProgressListener {
                    mBinding.imageButton.visibility=View.INVISIBLE
                    mBinding.progressBar2.visibility=View.VISIBLE

                }
                .addOnCompleteListener {
                    mBinding.progressBar2.visibility=View.GONE
                    mBinding.imageButton.visibility=View.VISIBLE

                }
                .addOnSuccessListener {
                    object : CountDownTimer(1000,1000){
                        override fun onTick(p0: Long) {

                        }

                        override fun onFinish() {
                            cambiarimagen(email)
                        }
                    }

                }
                .addOnFailureListener {

                }
        }
    }




}