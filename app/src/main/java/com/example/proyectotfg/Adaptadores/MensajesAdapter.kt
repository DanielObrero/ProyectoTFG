package com.example.proyectotfg.Adaptadores

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proyectotfg.Clases.Mensajes
import com.example.proyectotfg.Clases.Monumentos
import com.example.proyectotfg.Clases.Opiniones
import com.example.proyectotfg.Clases.Rutas
import com.example.proyectotfg.OnClickListener
import com.example.proyectotfg.R
import com.example.proyectotfg.databinding.OpinionesBinding
import com.example.proyectotfg.databinding.RutascreadasBinding

class MensajesAdapter(private var opiniones:ArrayList<Mensajes>) :
RecyclerView.Adapter<MensajesAdapter.ViewHolder>(){

    private lateinit var mContext:Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext=parent.context

        val view=LayoutInflater.from(mContext).inflate(R.layout.opiniones,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character=opiniones.get(position)
        with(holder){

            if (character.persona==character.mio){
                //derecha
            }else{
                //izquierda
            }







        }
    }
    fun setStores(stores: ArrayList<Mensajes>) {
        this.opiniones=stores
        notifyDataSetChanged()
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding= OpinionesBinding.bind(view)

    }

    override fun getItemCount(): Int =opiniones.size

}