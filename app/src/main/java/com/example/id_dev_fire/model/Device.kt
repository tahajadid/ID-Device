package com.example.id_dev_fire.model

class Device (

    val id : String = "",
    val deviceName : String = "",
    val version : String = "",
    val supportedOS : String = "",
    val features : String = "",
    val cupboard : String = "",
    val deviceOwner : String = "",
    val state : String = "",
    val currentState : String = "") {

    fun getdevName() : String {
        return deviceName
    }

    fun getvdeVersion() : String {
        return version
    }

    fun getdevFeatures() : String {
        return features
    }

    fun getdevCupboard() : String {
        return cupboard
    }

    fun getdevOwner() : String {
        return deviceOwner
    }

    fun getdevSupportedOS() : String {
        return supportedOS
    }

    fun getdevState() : String {
        return state
    }

    fun getdevCurrentState() : String {
        return currentState
    }

}