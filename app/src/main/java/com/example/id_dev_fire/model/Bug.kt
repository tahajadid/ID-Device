package com.example.id_dev_fire.model

class Bug(
    private val id : String? = "",
    private val bugOwner : String? = "",
    private val type : String? = "",
    private val description : String? = "")
{
    fun getBugId() : String? {
        return id
    }

    fun getThisBugOwner() : String? {
        return bugOwner
    }

    fun getBugType() : String? {
        return type
    }

    fun getBugDescription() : String? {
        return description
    }
}