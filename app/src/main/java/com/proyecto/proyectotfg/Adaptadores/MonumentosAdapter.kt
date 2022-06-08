package com.proyecto.proyectotfg.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.proyecto.proyectotfg.Clases.Monumentos
import com.proyecto.proyectotfg.OnClickListener
import com.proyecto.proyectotfg.R
import com.proyecto.proyectotfg.databinding.MonumentosBinding

class MonumentosAdapter(private var rutas:ArrayList<Monumentos>,var listener: OnClickListener) :
RecyclerView.Adapter<MonumentosAdapter.ViewHolder>(){

    private lateinit var mContext:Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext=parent.context

        val view=LayoutInflater.from(mContext).inflate(R.layout.monumentos,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character=rutas.get(position)
        with(holder){
setlistener(character)

            binding.tvName.text=character.titulo
            val prefs=mContext.getSharedPreferences("com.exameple.pruebafirebase.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
            var email=prefs?.getString("email",null)
            var partes=email!!.split("@")
            Glide.with(mContext)
                .load("https://firebasestorage.googleapis.com/v0/b/proyectotfg-87d7e.appspot.com/o/"+
                        "Rutas%2F${partes[0]}%40${partes[1]}%2Fruta${character.numruta}%2FMonumentos%2Fmonumento${character.nummonumento}%2Fportada.jpg?alt=media")
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.imgPhoto)


        }
    }
    fun setStores(stores: ArrayList<Monumentos>) {
        this.rutas=stores
        notifyDataSetChanged()
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding= MonumentosBinding.bind(view)
        fun setlistener(fotos: Monumentos){
            with(binding.root){
                setOnClickListener{
                    listener.editarmonumento(fotos)
                    true
                }
                setOnLongClickListener {
                    listener.borrarmonumento(fotos)
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int =rutas.size

}