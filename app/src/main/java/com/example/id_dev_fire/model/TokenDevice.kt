package com.example.id_dev_fire.model

class TokenDevice (
    val id : String? = "",
    val deviceToken : String? = "")  {

    fun getTokDeviceToken() : String? {
        return deviceToken
    }

    fun getTokId() : String? {
        return id
    }

}