package com.example.proyectotfg.Adaptadores

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proyectotfg.Clases.*
import com.example.proyectotfg.OnClickListener
import com.example.proyectotfg.R
import com.example.proyectotfg.databinding.ChatsBinding
import com.example.proyectotfg.databinding.OpinionesBinding
import com.example.proyectotfg.databinding.RutascreadasBinding

class ChatsAdapter(private var opiniones:ArrayList<Chats>, var listener: OnClickListener) :
RecyclerView.Adapter<ChatsAdapter.ViewHolder>(){

    private lateinit var mContext:Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext=parent.context

        val view=LayoutInflater.from(mContext).inflate(R.layout.chats,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character=opiniones.get(position)
        with(holder){
            setlisener(character)
            binding.tvnombre.text=character.persona







        }
    }
    fun setStores(stores: ArrayList<Chats>) {
        this.opiniones=stores
        notifyDataSetChanged()
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding= ChatsBinding.bind(view)
        fun setlisener(chats: Chats){
            with(binding.root){
                setOnClickListener {
                    listener.verchat(chats)
                }
            }
        }
    }

    override fun getItemCount(): Int =opiniones.size

}