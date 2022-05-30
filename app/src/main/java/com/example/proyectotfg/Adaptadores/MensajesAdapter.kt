package com.example.proyectotfg.Adaptadores

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.util.Log
import android.view.Gravity
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
import com.example.proyectotfg.databinding.MensajesBinding
import com.example.proyectotfg.databinding.OpinionesBinding
import com.example.proyectotfg.databinding.RutascreadasBinding
import java.awt.font.TextAttribute

class MensajesAdapter(private var opiniones:ArrayList<Mensajes>) :
RecyclerView.Adapter<MensajesAdapter.ViewHolder>(){

    private lateinit var mContext:Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext=parent.context

        val view=LayoutInflater.from(mContext).inflate(R.layout.mensajes,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character=opiniones.get(position)
        with(holder){

            if (character.persona==character.mio){
                binding.tvmensajei.text=character.mensajes
                binding.tvmensajed.text=""
                binding.tvmensajed.setBackgroundColor(Color.parseColor("#C2E7FD"))


            }else{

                binding.tvmensajed.text=character.mensajes
                binding.tvmensajei.text=""
                binding.tvmensajei.setBackgroundColor(Color.parseColor("#AAD5AC"))
            }







        }
    }
    fun setStores(stores: ArrayList<Mensajes>) {
        this.opiniones=stores
        notifyDataSetChanged()
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding= MensajesBinding.bind(view)

    }

    override fun getItemCount(): Int =opiniones.size

}