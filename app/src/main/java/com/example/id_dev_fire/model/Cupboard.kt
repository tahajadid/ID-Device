package com.example.id_dev_fire.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Cupboard (
        val id : String = "",
        val cupboardName : String = "") {

    fun getName() : String {
        return cupboardName
    }
}