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
import com.example.proyectotfg.Clases.Rutas
import com.example.proyectotfg.OnClickListener
import com.example.proyectotfg.R
import com.example.proyectotfg.databinding.RutascreadasBinding

class RutasAdapter(private var rutas:ArrayList<Rutas>,var listener: OnClickListener) :
RecyclerView.Adapter<RutasAdapter.ViewHolder>(){

    private lateinit var mContext:Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext=parent.context

        val view=LayoutInflater.from(mContext).inflate(R.layout.rutascreadas,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character=rutas.get(position)
        with(holder){
setlistener(character)

            binding.tvName.text=character.titulo


            Glide.with(mContext)
                .load("https://firebasestorage.googleapis.com/v0/b/proyectotfg-87d7e.appspot.com/o/" +
                        "Rutas%2Fruta${character.numruta}%2Fportada.jpg?alt=media")
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(binding.imgPhoto)


        }
    }
    fun setStores(stores: ArrayList<Rutas>) {
        this.rutas=stores
        notifyDataSetChanged()
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding= RutascreadasBinding.bind(view)
        fun setlistener(fotos: Rutas){
            with(binding.root){
                setOnClickListener{
                    listener.editarruta(fotos)
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int =rutas.size

}