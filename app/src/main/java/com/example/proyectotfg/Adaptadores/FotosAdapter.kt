package com.example.proyectotfg.Adaptadores

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proyectotfg.Clases.Fotos
import com.example.proyectotfg.Clases.Monumentos
import com.example.proyectotfg.Clases.Rutas
import com.example.proyectotfg.OnClickListener
import com.example.proyectotfg.R
import com.example.proyectotfg.databinding.FotosBinding
import com.example.proyectotfg.databinding.MonumentosBinding
import com.example.proyectotfg.databinding.RutascreadasBinding

class FotosAdapter(private var fotos:ArrayList<Fotos>, var listener:OnClickListener) :
RecyclerView.Adapter<FotosAdapter.ViewHolder>(){

    private lateinit var mContext:Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext=parent.context

        val view=LayoutInflater.from(mContext).inflate(R.layout.fotos,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character=fotos.get(position)
        with(holder){


            var monumento=character.nombremonumento.trim().split(" ")
            var real=""
            var o=0
            for (i in monumento){
                if (o==(monumento.size-1)){
                    real+=i
                }else{
                    real+=i+"%20"
                }
                Log.d("real",real)
                o++
            }
            var ruta=character.nombreruta.trim().split(" ")
            var real2=""
            var o2=0
            for (i in ruta){
                if (o2==(ruta.size-1)){
                    real2+=i
                }else{
                    real2+=i+"%20"
                }
                o2++
            }
            Log.d("real",real2)
            Glide.with(mContext)
                .load("https://firebasestorage.googleapis.com/v0/b/proyectotfg-87d7e.appspot.com/o/" +
                        "Rutas%2F$real2%2FMonumentos%2F$real%2F${character.num}.jpg?alt=media")

                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(binding.imgPhoto)


        }
    }
    fun setStores(stores: ArrayList<Fotos>) {
        this.fotos=stores
        notifyDataSetChanged()


    }
    fun borrar(foto: Fotos){

        val index=fotos.indexOf(foto)
        if (index!=-1){
            fotos.removeAt(index)
            notifyItemRemoved(index)
        }

    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding= FotosBinding.bind(view)

    }

    override fun getItemCount(): Int =fotos.size

}