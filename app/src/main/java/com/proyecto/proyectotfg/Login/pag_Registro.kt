package com.proyecto.proyectotfg.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.proyecto.proyectotfg.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.regex.Pattern

class pag_Registro : AppCompatActivity() {
    val pattern: Pattern = Patterns.EMAIL_ADDRESS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pag_registro)

        var intentemail:String=intent.getStringExtra("emailr").toString()

        var email=findViewById<EditText>(R.id.etEmail)
        var password=findViewById<EditText>(R.id.etPassword)
        var passwordr=findViewById<EditText>(R.id.etPassworR)
        var btnSiguiente=findViewById<Button>(R.id.btnSiguiente)
        email.setText(intentemail)
        if (email.text.isNotEmpty()){
            email.isEnabled=false
        }



        btnSiguiente.setOnClickListener{
            if (email.text.isNotEmpty()&&password.text.isNotEmpty()&&passwordr.text.isNotEmpty()){
                if (pattern.matcher(email.text.toString()).matches()){
                    if (password.text.toString().compareTo(passwordr.text.toString())==0){
                        datosadicionales(email.text.toString(),password.text.toString())
                    }else{
                        showAlert(2)
                    }
                }else{
                    showAlert(1)
                }
            }else{
                showAlert(0)
            }
        }
    }

    private fun datosadicionales(email: String, pass: String) {
        val datosadicionalesIntent= Intent(this, pag_DatosAdicionales::class.java).apply {
            putExtra("emailini",email)
            putExtra("passini",pass)
            putExtra("provider","BASIC")

        }
        startActivity(datosadicionalesIntent)
    }

    private fun showAlert(i:Int){

        if (i==0){
            val builder= AlertDialog.Builder(this)
            builder.setTitle("Info")
            builder.setMessage("No debe de haber campos vacios")
            builder.setPositiveButton("Aceptar",null)
            val dialog: AlertDialog =builder.create()
            dialog.show()
        }
        if (i==1){
            val builder= AlertDialog.Builder(this)
            builder.setTitle("Info")
            builder.setMessage("El correo debe ser correcto")
            builder.setPositiveButton("Aceptar",null)
            val dialog: AlertDialog =builder.create()
            dialog.show()
        }
        if (i==2){
            val builder= AlertDialog.Builder(this)
            builder.setTitle("Info")
            builder.setMessage("Los campos de contraseña deben de coincidir")
            builder.setPositiveButton("Aceptar",null)
            val dialog: AlertDialog =builder.create()
            dialog.show()
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
        val intent= Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}