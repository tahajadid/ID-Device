package com.example.id_dev_fire.model

class DamagedDevice(

    val idDamagedDevice : String? = "",
    val nameDevice : String? = "",
    val declaredById : String? = "",
    val declaredByFullName : String? = "",
    val title : String? = "",
    val description : String? = "",
    val deviceOwnerId : String? = "") {

    fun getDecIdDamagedDevice() : String? {
        return idDamagedDevice
    }

    fun getDecNameDevice() : String? {
        return nameDevice
    }

    fun getDecDeclaredById() : String? {
        return declaredById
    }

    fun getDecDeclaredByFullName() : String? {
        return declaredByFullName
    }

    fun getDecTitle() : String? {
        return title
    }

    fun getDecDescriptionDevices() : String? {
        return description
    }

    fun getDecDeviceOwnerDevice() : String? {
        return deviceOwnerId
    }

}