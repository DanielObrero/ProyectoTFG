package com.example.proyectotfg

import com.example.proyectotfg.Clases.Chats
import com.example.proyectotfg.Clases.Fotos
import com.example.proyectotfg.Clases.Monumentos
import com.example.proyectotfg.Clases.Rutas

interface OnClickListener {
    fun borrarfoto(foto:Fotos)
    fun editarmonumento(Monumento: Monumentos)
    fun editarruta(rutas: Rutas)
    fun addruta(rutas: Rutas)
    fun borrarmonumento(fotos: Monumentos)
    fun verchat(chat: Chats)
}