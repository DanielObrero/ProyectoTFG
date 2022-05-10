package com.example.proyectotfg.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectotfg.R
import com.example.proyectotfg.Principal.Guia.MenuActivityG
import com.example.proyectotfg.Principal.Turista.MenuActivityT
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {
    var progressBar: ProgressBar?=null
    var tvdatossesion: TextView?=null
    var authlayout: LinearLayout?=null
    val pattern: Pattern = Patterns.EMAIL_ADDRESS
    private  val GOOGLE_SIGN_IN=100
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
        session()
        progressBar!!.visibility=View.GONE
        tvdatossesion!!.visibility=View.GONE
    }

    override fun onStart() {
        super.onStart()
        var authLayout: LinearLayout =findViewById(R.id.authLayout)
        authLayout.visibility=View.VISIBLE
    }

    private fun session(){
        val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email=prefs.getString("email",null)
        val prefs2=getSharedPreferences("Tipodeusu", Context.MODE_PRIVATE)
        val TU=prefs.getString("tipo",null)

        if(email!=null&&TU!=null){
            var authLayout: LinearLayout =findViewById(R.id.authLayout)
            authLayout.visibility= View.INVISIBLE
            showHome(email,TU)
        }
    }

    private fun setup(){
        title="Autenticacion"


        var signUpButton: Button=findViewById(R.id.LogOutButton)
        var loginButton: Button=findViewById(R.id.loginButton)
        var googleButton: Button=findViewById(R.id.GoogleButton)
        var contraseñaButton: Button=findViewById(R.id.ContraseñaButton)
        var emailEditText: EditText=findViewById(R.id.emailEditText)
        var PasswordEditText: EditText=findViewById(R.id.passwordEditText)
        authlayout=findViewById(R.id.authLayout)
        progressBar=findViewById(R.id.progressBar)
        tvdatossesion=findViewById(R.id.tvdatossesion)

        signUpButton.setOnClickListener{
            showRegister("")
        }
        loginButton.setOnClickListener{
            if (emailEditText.text.isNotEmpty() && PasswordEditText.text.isNotEmpty()){

                if (pattern.matcher(emailEditText.text.toString()).matches()){
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(emailEditText.text.toString(),
                            PasswordEditText.text.toString()).addOnCompleteListener{

                            if(it.isSuccessful){
                                Comprobacion(emailEditText.text.toString())



                            } else{
                                showAlert(0)
                            }
                        }
                }else{
                    showAlert(1)
                }

            } else {
                showAlert(2)
            }
        }
        googleButton.setOnClickListener {

            progressBar!!.visibility=View.VISIBLE
            val googleConf=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("630915479165-n7rff65cs04rnrql6ekp3ocu11uiji8r.apps.googleusercontent.com")
                .requestEmail()
                .build()
            val googleClient=GoogleSignIn.getClient(this,googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN)
        }
        contraseñaButton.setOnClickListener {
            val dialog=layoutInflater.inflate(R.layout.cambio_password,null)

            var dialogq =MaterialAlertDialogBuilder(this).apply {
                setTitle("Cambio de contraseña")
                setView(dialog)
                setPositiveButton("Enviar") { _, i ->

                    if (dialog.findViewById<EditText>(R.id.etcorreoCC).text.toString()
                            .isNotEmpty()
                    ) {
                        FirebaseAuth.getInstance()
                            .sendPasswordResetEmail(dialog.findViewById<EditText>(R.id.etcorreoCC).text.toString())
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
                    } else {
                        Snackbar.make(it, "Debe introducir un correo", Snackbar.LENGTH_SHORT).show()
                    }
                }

            }.show()


            //
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==GOOGLE_SIGN_IN){
            progressBar!!.visibility=View.GONE
        val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account=task.getResult(ApiException::class.java)

                if(account!=null){
                    val credential=GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{

                        if(it.isSuccessful){
                            tvdatossesion!!.visibility=View.VISIBLE
                            progressBar!!.visibility=View.VISIBLE
                            authlayout!!.visibility=View.GONE
                            Comprobacion(account.email.toString())

                        } else{
                            //showAlert()
                        }
                    }
                }
            }catch (e:ApiException){
                showAlert(0)
            }

    }else{
            progressBar!!.visibility=View.GONE
        }

    }

    private fun showAlert(i:Int){

        if (i==0){
            val builder=AlertDialog.Builder(this)
            builder.setTitle("Error")
            builder.setMessage("Se ha producido un error autenticando al usuario")
            builder.setPositiveButton("Aceptar",null)
            val dialog: AlertDialog=builder.create()
            dialog.show()
        }
        if (i==1){
            val builder=AlertDialog.Builder(this)
            builder.setTitle("Info")
            builder.setMessage("El correo debe ser correcto")
            builder.setPositiveButton("Aceptar",null)
            val dialog: AlertDialog=builder.create()
            dialog.show()
        }
        if (i==2){
            val builder=AlertDialog.Builder(this)
            builder.setTitle("Info")
            builder.setMessage("Los campos email o contraseña no pueden estar vacios")
            builder.setPositiveButton("Aceptar",null)
            val dialog: AlertDialog=builder.create()
            dialog.show()
        }


    }

    private fun showHome(email:String,comp:String){
        progressBar!!.visibility=View.GONE
        tvdatossesion!!.visibility=View.GONE
        authlayout!!.visibility=View.VISIBLE
        var homeIntent: Intent? = null
        if (comp=="0"){
            homeIntent=Intent(this, MenuActivityG::class.java).apply {
                putExtra("email",email)
                putExtra("tipo",comp)

            }

        }
        if (comp=="1"){
            homeIntent=Intent(applicationContext, MenuActivityT::class.java).apply {
                putExtra("email",email)
                putExtra("tipo",comp)

            }

        }
        startActivity(homeIntent)

    }

    private fun showRegister(email:String){

        val registerIntent=Intent(this, pag_Registro::class.java).apply {
            putExtra("emailr",email)

        }
        startActivity(registerIntent)
    }

    fun Comprobacion(email: String){
        var ia:Int=0
        val docRef = db.collection("users")
        docRef.get()
            .addOnSuccessListener { document ->
                for (i in document.documents){
                    ia++
                    if (i.id.compareTo(email)==0) {
                        if (i.get("Tipo de Usuario").toString().compareTo("Guia")==0){
                            showHome(email,"0")
                        }
                        if (i.get("Tipo de Usuario").toString().compareTo("Turista")==0){
                            showHome(email,"1")
                        }
                        ia=0


                        break
                    }
                }
                if (ia==document.size()){
                    progressBar!!.visibility=View.VISIBLE
                    tvdatossesion!!.visibility=View.GONE
                    authlayout!!.visibility=View.GONE
                        showaditional(email)
                    }

            }
            .addOnFailureListener { exception ->
            }
    }

    private fun showaditional(email: String) {
        val registerIntent=Intent(this, pag_DatosAdicionales::class.java).apply {
            putExtra("emailr",email)
            putExtra("provider","GOOGLE")
        }
        startActivity(registerIntent)
    }
    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle("Info")
            setMessage("¿Desea salir de la aplicación?")
            setPositiveButton("Aceptar", { _,_ ->
                finish()

            })
            setNegativeButton("Cancelar",null)
                .show()
        }
    }


}