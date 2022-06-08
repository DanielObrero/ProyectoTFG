package com.proyecto.proyectotfg.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.proyectotfg.Clases.Chats
import com.proyecto.proyectotfg.OnClickListener
import com.proyecto.proyectotfg.R
import com.proyecto.proyectotfg.databinding.ChatsBinding

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