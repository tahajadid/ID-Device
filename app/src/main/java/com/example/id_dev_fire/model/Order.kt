package com.example.id_dev_fire.model

import java.util.*

class Order(
        val id : String = "",
        val deviceName : String = "",
        val deviceOwner : String ="",
        val currentOwner : String="",
        val startDay : Date,
        val endDay : Date,
        val reason : String = "",
        val decision : String = "") {
}