package com.example.proyectotfg

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class pag_DatosAdicionales : AppCompatActivity() {
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pag_datos_adicionales)

        var emailini:String=""
        var passini:String=""
        var proveedor=intent.getStringExtra("provider")
        if (proveedor.toString().compareTo("BASIC")==0){
             emailini= intent.getStringExtra("emailini").toString()
            passini=intent.getStringExtra("passini").toString()
        }
        if (proveedor.toString().compareTo("GOOGLE")==0){
            emailini= intent.getStringExtra("emailr").toString()

        }
        var etnombre=findViewById<EditText>(R.id.etNombre)
        var etPapellido=findViewById<EditText>(R.id.etPApellido)
        var etSapellido=findViewById<EditText>(R.id.etSApellido)
        var etTelefono=findViewById<EditText>(R.id.etTelefono)
        var rbTurista=findViewById<RadioButton>(R.id.rbTurista)
        var rbGuia=findViewById<RadioButton>(R.id.rbGuia)
        var btnRegistrar=findViewById<Button>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {

            if (etnombre.text.isNotEmpty() && etPapellido.text.isNotEmpty() &&
                etSapellido.text.isNotEmpty() && etTelefono.text.isNotEmpty()){
                    if (etTelefono.text.length==9){
                        if(rbGuia.isChecked || rbTurista.isChecked){
                                if (rbTurista.isChecked){
                                registrarcuenta(emailini,passini,proveedor.toString(),
                                    etnombre.text.toString(),etPapellido.text.toString(),etSapellido.text.toString(),
                                    etTelefono.text.toString(),"Turista")
                                }
                                if (rbGuia.isChecked){
                                registrarcuenta(emailini.toString(),passini.toString(),proveedor.toString(),
                                    etnombre.text.toString(),etPapellido.text.toString(),etSapellido.text.toString(),
                                    etTelefono.text.toString(),"Guia")
                                }
                        } else{
                                showAlert(1)
                        }

                    }else{
                    showAlert(2)
                    }

            } else {
                showAlert(0)
            }
        }
    }

    fun registrarcuenta(email:String,pass:String,proveedor:String,nombre:String,Papellido:String,Sapellido:String,telefono:String,tipo:String){
        if (proveedor.compareTo("BASIC")==0){
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener{
                    if(it.isSuccessful){

                        subir(email,nombre, Papellido, Sapellido, telefono, tipo)
                    } else{
                        MaterialAlertDialogBuilder(this).apply {
                            setTitle("Error de Registro")
                            setMessage("Ha ocurrido un error al registrar los datos de usuario")
                            setPositiveButton("Aceptar",null).show()
                        }
                    }
                }

        }
        if (proveedor.compareTo("GOOGLE")==0){
            subir(email,nombre, Papellido, Sapellido, telefono, tipo)
        }
    }
    fun subir(email:String,nombre:String,Papellido:String,Sapellido:String,telefono:String,tipo:String){
        db.collection("users").document(email)
            .set(
                hashMapOf(
                    "nombre" to nombre,
                    "Primer Apellido" to Papellido,
                    "Segundo Apellido" to Sapellido,
                    "Telefono" to telefono,
                    "Tipo de Usuario" to tipo,

                )
            ).addOnSuccessListener { documentReference ->
                MaterialAlertDialogBuilder(this).apply {
                    setTitle("Nuevo Usuario")
                    setMessage("Usuario Registrado con existo")
                    setPositiveButton("Aceptar",null).show()
                }
                showHome(email)
            }
            .addOnFailureListener { e ->
                MaterialAlertDialogBuilder(this).apply {
                    setTitle("Error de Datos")
                    setMessage("Fallo al guardar los datos")
                    setPositiveButton("Aceptar",null).show()
                }
            }
    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle("Info")
            setMessage("¿Desea volver a la página de inicio?")
            setPositiveButton("Aceptar", { _,_ ->
                showLogin()
            })
            setNegativeButton("Cancelar",null)
                .show()
        }
    }

    private fun showLogin() {
        val intent= Intent(this,MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
    private fun showHome(email:String){

        val homeIntent=Intent(this,HomeActivity::class.java).apply {
            putExtra("email",email)
        }
        startActivity(homeIntent)
    }

    private fun showAlert(i:Int){
        if (i==0){
            val builder= AlertDialog.Builder(this)
            builder.setTitle("Info")
            builder.setMessage("No puede haber ningun campo vacio")
            builder.setPositiveButton("Aceptar",null)
            val dialog: AlertDialog =builder.create()
            dialog.show()
        }
        if (i==1){
            val builder= AlertDialog.Builder(this)
            builder.setTitle("Info")
            builder.setMessage("Se debe de seleccionar un tipo de Usuario")
            builder.setPositiveButton("Aceptar",null)
            val dialog: AlertDialog =builder.create()
            dialog.show()
        }
        if (i==2){
            val builder= AlertDialog.Builder(this)
            builder.setTitle("Info")
            builder.setMessage("El campo teléfono debe de tener 9 caracteres")
            builder.setPositiveButton("Aceptar",null)
            val dialog: AlertDialog =builder.create()
            dialog.show()
        }

    }
}