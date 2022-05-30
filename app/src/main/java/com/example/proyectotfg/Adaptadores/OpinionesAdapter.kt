package com.example.proyectotfg.Adaptadores

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proyectotfg.Clases.Monumentos
import com.example.proyectotfg.Clases.Opiniones
import com.example.proyectotfg.Clases.Rutas
import com.example.proyectotfg.OnClickListener
import com.example.proyectotfg.R
import com.example.proyectotfg.databinding.OpinionesBinding
import com.example.proyectotfg.databinding.RutascreadasBinding

class OpinionesAdapter(private var opiniones:ArrayList<Opiniones>) :
RecyclerView.Adapter<OpinionesAdapter.ViewHolder>(){

    private lateinit var mContext:Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext=parent.context

        val view=LayoutInflater.from(mContext).inflate(R.layout.opiniones,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character=opiniones.get(position)
        with(holder){

            binding.ratingBar2.setIsIndicator(true)
        binding.tvnombreruta.text=character.nombreruta
        binding.tvpersona.text=character.persona
        binding.tvvalor.text="${character.valor}/5"
        binding.ratingBar2.rating=character.valor.toFloat()







        }
    }
    fun setStores(stores: ArrayList<Opiniones>) {
        this.opiniones=stores
        notifyDataSetChanged()
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding= OpinionesBinding.bind(view)

    }

    override fun getItemCount(): Int =opiniones.size

}