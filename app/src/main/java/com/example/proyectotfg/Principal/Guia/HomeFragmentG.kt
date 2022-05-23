package com.example.proyectotfg.Principal.Guia

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyectotfg.Adaptadores.RutasAdapter
import com.example.proyectotfg.Clases.Fotos
import com.example.proyectotfg.Clases.Monumentos
import com.example.proyectotfg.Clases.Rutas
import com.example.proyectotfg.Login.MainActivity
import com.example.proyectotfg.OnClickListener
import com.example.proyectotfg.R
import com.example.proyectotfg.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.common.io.Resources.getResource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragmentG : Fragment(),OnClickListener {
    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mAdapter: RutasAdapter
    private lateinit var mGridLayout: GridLayoutManager
    var listarutas=ArrayList<Rutas>()
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    mBinding.btnturista.setOnClickListener {
        mBinding.btnturista.isEnabled=false
        mBinding.btnguia.isEnabled=true
        mBinding.btnturista.setBackgroundColor(Color.parseColor("#FF3700B3"))
        mBinding.btnguia.setBackgroundColor(Color.parseColor("#FF6200EE"))
        mBinding.clGuia.visibility=View.GONE
        mBinding.clTurista.visibility=View.VISIBLE
    }
        mBinding.btnguia.setOnClickListener {
            mBinding.btnguia.isEnabled=false
            mBinding.btnturista.isEnabled=true
            mBinding.btnguia.setBackgroundColor(Color.parseColor("#FF3700B3"))
            mBinding.btnturista.setBackgroundColor(Color.parseColor("#FF6200EE"))
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
                    val intent= Intent(context, MainActivityadd::class.java).apply {
                        putExtra("email",email)
                        putExtra("nombreruta",dialog.findViewById<EditText>(R.id.editTextTextPersonName).text.toString())
                    }
                    startActivity(intent)
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
                setupRecyclerView()
            }

        }.start()
    }

    private fun setupRecyclerView() {
        mAdapter= RutasAdapter(ArrayList(),this)
        mGridLayout= GridLayoutManager(context,2)

        obtenerDatos()


        mBinding.recyclerViewG.apply {
            setHasFixedSize(true)
            layoutManager=mGridLayout
            adapter=mAdapter
        }

    }

    private fun obtenerDatos() {

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
                    var rutas: Rutas = Rutas(document.get("Rutas.ruta$cont.Nombre").toString(),
                        document.get("Rutas.ruta$cont.Lugar de inicio").toString(),
                                document.get("Rutas.ruta$cont.Provincia").toString(),
                                document.get("Rutas.ruta$cont.Localidad").toString(),
                                document.get("Rutas.ruta$cont.Kms").toString(),cont.toString())

                    listarutas.add(rutas)

                    cont++
                }






                mAdapter.setStores(listarutas)
            }
            .addOnFailureListener { exception ->
                Log.d("rutas","fallo")
            }




    }

    override fun borrarfoto(foto: Fotos) {
        TODO("Not yet implemented")
    }

    override fun editarmonumento(Monumento: Monumentos) {
        TODO("Not yet implemented")
    }

    override fun editarruta(rutas: Rutas) {
        val prefs=activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email=prefs?.getString("email",null)
        var intent=Intent(context,MainActivityverruta::class.java).apply {
            putExtra("nombreruta",rutas.titulo)
            putExtra("etprovincia",rutas.provincia)
            putExtra("etkms",rutas.kms)
            putExtra("etlugardeinicio",rutas.lugardeinicio)
            putExtra("localidad",rutas.localidad)
            putExtra("numruta",rutas.numruta)
            putExtra("email",email)
        }

        startActivity(intent)
    }


}
