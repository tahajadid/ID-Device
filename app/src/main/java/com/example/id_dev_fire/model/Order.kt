package com.example.id_dev_fire.model

import java.util.*

class Order(
        val id : String? = "",
        val deviceName : String? = "",
        val deviceOwner : String? ="",
        val fullNameDeviceOwner : String? ="",
        val currentOwner : String?="",
        val startDay : String?="",
        val endDay : String?="",
        val reason : String? = "",
        // var declaration because it can be changed so it's not a value
        var decision : String? = "") {

    fun getorderId() : String? {
        return id
    }

    fun getorderDeviceName() : String? {
        return deviceName
    }

    fun getorderDeviceOwner() : String? {
        return deviceOwner
    }


    fun getorderCurrentOwner() : String? {
        return currentOwner
    }

    fun getorderFullNameDeviceOwner() : String? {
        return fullNameDeviceOwner
    }

    fun getorderReason() : String? {
        return reason
    }

    fun getorderDecision() : String? {
        return decision
    }

    fun getorderDateStart() : String? {
        return startDay
    }

    fun getorderDateEnd() : String? {
        return endDay
    }

    fun setOrderDecision(dec : String?)  {
        this.decision = dec
    }

}

