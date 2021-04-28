package com.example.id_dev_fire.model

class Cupboard (
        val id : String = "",
        val cupboardName : String = "") {

    fun getName() : String {
        return cupboardName
    }
}