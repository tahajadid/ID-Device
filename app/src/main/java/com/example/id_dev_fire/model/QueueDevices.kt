package com.example.id_dev_fire.model

class QueueDevices(

    private val idDevice : String? = "",
    var currentNumOrder : Int = 0,
    var NumOrdersOnHold : Int = 0,
    var listOrders : MutableList<String> = mutableListOf()

){

    fun getQueueDevicesIdDevice() : String? {
        return idDevice
    }

    fun getQueueDevicesCurrentNumberOrder() : Int {
        return currentNumOrder
    }

    fun getQueueDevicesNumOrdersOnHold() : Int {
        return NumOrdersOnHold
    }

    fun getQueueDevicesListOrders() : MutableList<String> {
        return listOrders
    }

}