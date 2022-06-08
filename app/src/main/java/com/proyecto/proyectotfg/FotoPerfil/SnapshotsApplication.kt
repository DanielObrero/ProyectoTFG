package com.proyecto.proyectotfg.FotoPerfil

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseUser

/****
 * Project: Snapshots
 * From: com.cursosandroidant.snapshots
 * Created by Alain Nicolás Tello on 02/02/22 at 2:52 PM
 * Course: Android Practical with Kotlin from zero.
 * All rights reserved 2021.
 * My website: www.alainnicolastello.com
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/

class SnapshotsApplication : Application() {
    companion object {

        const val PATH_PERFILES = "Perfiles"
        const val PATH_RUTAS = "Rutas"
        const val PROPERTY_LIKE_LIST = "likeList"

        lateinit var currentUser: FirebaseUser
    }
}