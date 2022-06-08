package com.proyecto.proyectotfg.Adaptadores

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.proyectotfg.Clases.Mensajes
import com.proyecto.proyectotfg.R
import com.proyecto.proyectotfg.databinding.MensajesBinding

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