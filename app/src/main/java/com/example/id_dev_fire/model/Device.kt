package com.example.id_dev_fire.model

import android.os.Parcel
import android.os.Parcelable

data class Device (

    val id : String? = "",
    val deviceName : String? = "",
    val version : String? = "",
    val supportedOS : String? = "",
    val features : String? = "",
    val cupboard : String? = "",
    val deviceOwner : String? = "",
    val state : String? = "",
    val currentState : String? = "") : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()) {
    }

    fun getdevId() : String? {
        return id
    }

    fun getdevName() : String? {
        return deviceName
    }

    fun getvdeVersion() : String? {
        return version
    }

    fun getdevFeatures() : String? {
        return features
    }

    fun getdevCupboard() : String? {
        return cupboard
    }

    fun getdevOwner() : String? {
        return deviceOwner
    }

    fun getdevSupportedOS() : String? {
        return supportedOS
    }

    fun getdevState() : String? {
        return state
    }

    fun getdevCurrentState() : String? {
        return currentState
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(deviceName)
        parcel.writeString(version)
        parcel.writeString(supportedOS)
        parcel.writeString(features)
        parcel.writeString(cupboard)
        parcel.writeString(deviceOwner)
        parcel.writeString(state)
        parcel.writeString(currentState)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Device> {
        override fun createFromParcel(parcel: Parcel): Device {
            return Device(parcel)
        }

        override fun newArray(size: Int): Array<Device?> {
            return arrayOfNulls(size)
        }
    }

}