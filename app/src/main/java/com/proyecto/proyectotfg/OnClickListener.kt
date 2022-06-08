package com.proyecto.proyectotfg

import com.proyecto.proyectotfg.Clases.*

interface OnClickListener {
    fun borrarfoto(foto:Fotos)
    fun editarmonumento(Monumento: Monumentos)
    fun editarruta(rutas: Rutas)
    fun editarrutafutura(rutas: RutasFuturas)
    fun addruta(rutas: Rutas)
    fun addrutafutura(rutas: RutasFuturas)
    fun borrarmonumento(fotos: Monumentos)
    fun verchat(chat: Chats)
}