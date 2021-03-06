package com.example.id_dev_fire.firestoreClass

import com.example.id_dev_fire.model.Order
import com.example.id_dev_fire.model.QueueDevices
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ImpactOrder {

    private val mFirestoreClass = FirebaseFirestore.getInstance()
    val calendar = Calendar.getInstance()
    val today = Date(calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))

    /*
     *  The first process when The ManagerAccept the order and should be added to the Queue
     *  Also should do some modifies on the Queue
     */


    fun acceptOrder(idOrder : String,idDevice: String){

        // If the manager accept the order
        // Steps to do :
        // 1. When admin add a Device he should add a Queue for this one
        // 2. For the first order we will Get the end of reservation and put it in the device information page
        // 3. Add the id of the order to the Queue of the device

        mFirestoreClass.collection("queueDevices")
            .document(idDevice).get().addOnCompleteListener{ it ->
                if(it.isSuccessful) {
                    val newQueue = it.result!!.toObject(QueueDevices::class.java)
                    val actualList = newQueue!!.getQueueDevicesListOrders()!!

                    if(actualList.size > 0){
                        // Not the first order of the device
                        // numOnHold == 0.. so we need to check the start day of orders with today
                        // Insert into ListOfQueue
                            addOrderToQueueFirebase(idOrder,idDevice)

                    }else{
                        // This case would be the first order of the device
                        // For this case it needs to be changed from not reserved to reserved

                        // Change state of Device
                        // On hold = OnHold + 1
                        // Insert into ListOfQueue
                        firstInitOfQueue(idOrder,idDevice)
                    }

                }
            }.addOnFailureListener {
                //
            }
    }

    fun addOrderToQueueFirebase(idOrder: String, idDevice: String){

        mFirestoreClass.collection("queueDevices")
            .document(idDevice).get().addOnCompleteListener{
                val newQueue = it.result!!.toObject(QueueDevices::class.java)
                var oldList = newQueue!!.getQueueDevicesListOrders()
                var numOrder = newQueue.getQueueDevicesCurrentNumberOrder()
                var numOnHold = newQueue.getQueueDevicesNumOrdersOnHold()
                oldList.add(idOrder)

                mFirestoreClass.collection("orders").document(idOrder)
                    .get().addOnCompleteListener {
                        val startDayOfOrder = it.result!!.toObject(Order::class.java)!!.getorderDateStart()
                        if(todayAfterOrEquals(startDayOfOrder!!)){

                            // Set the new updates
                            mFirestoreClass.collection("queueDevices")
                                .document(idDevice)
                                .update(hashMapOf(
                                    "listOrders" to oldList,
                                    "queueDevicesListOrders" to oldList,
                                    "queueDevicesCurrentNumberOrder" to numOrder+1,
                                    "currentNumOrder" to numOrder+1,
                                    "queueDevicesNumOrdersOnHold" to numOnHold+1,
                                    "numOrdersOnHold" to numOnHold+1
                                ) as Map<String, Any>).addOnCompleteListener {

                                    mFirestoreClass.collection("devices").document(idDevice)
                                        .update(
                                            hashMapOf("currentState" to "Reserved",
                                                "devCurrentState" to "Reserved") as Map<String, Any>
                                        )
                                }.addOnFailureListener {
                                    //
                                }
                        }else{

                            // Set the new updates
                            mFirestoreClass.collection("queueDevices")
                                .document(idDevice)
                                .update(hashMapOf(
                                    "listOrders" to oldList,
                                    "queueDevicesListOrders" to oldList,
                                    "queueDevicesCurrentNumberOrder" to numOrder,
                                    "currentNumOrder" to numOrder,
                                    "queueDevicesNumOrdersOnHold" to numOnHold+1,
                                    "numOrdersOnHold" to numOnHold+1
                                ) as Map<String, Any>).addOnCompleteListener {
                                    //
                                }.addOnFailureListener {
                                    //
                                }
                        }
                    }
                    .addOnFailureListener {

                    }

            }
    }

    fun firstInitOfQueue(idOrder: String, idDevice: String){

        mFirestoreClass.collection("orders").document(idOrder)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    val order = it.result!!.toObject(Order::class.java)
                    var orderStartDay = order!!.getorderDateStart()
                    if(!todayBeforeOrEquals(orderStartDay!!)){
                        // Day to take device is so far
                        mFirestoreClass.collection("queueDevices")
                            .document(idDevice).get().addOnCompleteListener{
                                val newQueue = it.result!!.toObject(QueueDevices::class.java)
                                var oldList = newQueue!!.getQueueDevicesListOrders()
                                var numOrder = newQueue.getQueueDevicesCurrentNumberOrder()
                                // First Modif -> incr NumOnHold
                                var numOnHold = newQueue.getQueueDevicesNumOrdersOnHold()

                                // Second Modif -> add the id
                                oldList.add(idOrder)

                                // Set the new updates
                                mFirestoreClass.collection("queueDevices")
                                    .document(idDevice)
                                    .update(hashMapOf(
                                        "listOrders" to oldList,
                                        "queueDevicesListOrders" to oldList,
                                        "queueDevicesCurrentNumberOrder" to numOrder - 1 ,
                                        "currentNumOrder" to numOrder - 1,
                                        "queueDevicesNumOrdersOnHold" to numOnHold +1,
                                        "numOrdersOnHold" to numOnHold +1
                                    ) as Map<String, Any>).addOnCompleteListener {
                                        //

                                    }.addOnFailureListener {
                                        //
                                    }
                            }
                    }else{
                        mFirestoreClass.collection("queueDevices")
                            .document(idDevice).get().addOnCompleteListener{
                                val newQueue = it.result!!.toObject(QueueDevices::class.java)
                                var oldList = newQueue!!.getQueueDevicesListOrders()
                                var numOrder = newQueue.getQueueDevicesCurrentNumberOrder()
                                // First Modif -> incr NumOnHold
                                var numOnHold = newQueue.getQueueDevicesNumOrdersOnHold()

                                // Second Modif -> add the id
                                oldList.add(idOrder)

                                // Set the new updates
                                mFirestoreClass.collection("queueDevices")
                                    .document(idDevice)
                                    .update(hashMapOf(
                                        "listOrders" to oldList,
                                        "queueDevicesListOrders" to oldList,
                                        "queueDevicesCurrentNumberOrder" to numOrder,
                                        "currentNumOrder" to numOrder,
                                        "queueDevicesNumOrdersOnHold" to numOnHold +1,
                                        "numOrdersOnHold" to numOnHold +1
                                    ) as Map<String, Any>).addOnCompleteListener {

                                        // Need to take device and put reserved on device state
                                        mFirestoreClass.collection("devices").document(idDevice)
                                            .update(
                                                hashMapOf("currentState" to "Reserved",
                                                    "devCurrentState" to "Reserved") as Map<String, Any>
                                            ).addOnCompleteListener {
                                                //
                                            }.addOnFailureListener {
                                                //
                                            }

                                    }.addOnFailureListener {
                                        //
                                    }
                            }
                    }
                }else{
                    // noth..
                }
            }.addOnFailureListener {
                //
            }

    }


    /*  The Second process when we need to change the state of devices automatically
     *  When the period of order ends, every login to list the devices need to chack
     *  The database and make some changes on it.
     */

    // ..Application crash when we want to test about all the devices
    // ..So we add test FOR ONLY a single device

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    fun checkDeviceState(idDevice: String) {

            mFirestoreClass.collection("queueDevices")
                .document(idDevice).get()
                .addOnCompleteListener { it ->

                    if(it.isSuccessful)
                    {
                        // We get a specific Queue with her id

                        val queue = it.result!!.toObject(QueueDevices::class.java)
                        val numCurrentOrder = queue?.getQueueDevicesCurrentNumberOrder()
                        val numOrdersOnHold = queue?.getQueueDevicesNumOrdersOnHold()
                        val listOrders = queue?.getQueueDevicesListOrders()

                        if (listOrders!!.size != 0) {

                            //Log.d("CheckState","3.1.3. order to check : ${listOrders!![numCurrentOrder!!]}")

                            // After verifying if the list is not empty
                            // We need to verify if the currentOrder is pointed on the right order
                            // That's means when the PO accept an order after the last checking

                            if( listOrders.size-1 > numCurrentOrder!!){
                                // Check the start day of the next added order
                                // Increment CurrentOrder index or leave as it is
                                mFirestoreClass.collection("orders")
                                    .document(listOrders!![numCurrentOrder!!+1])
                                    .get().addOnCompleteListener {
                                        var newtOrderStartDay =
                                            it.result!!.toObject(Order::class.java)!!.getorderDateStart()

                                        if( todayAfterOrEquals(newtOrderStartDay!!) ){
                                            // Increment index(s) & set Reserved to state of Device
                                            mFirestoreClass.collection("queueDevices")
                                                .document(idDevice)
                                                .update(
                                                    hashMapOf(
                                                        "currentNumOrder" to numCurrentOrder +1,
                                                        "queueDevicesCurrentNumberOrder" to numCurrentOrder + 1,
                                                        "queueDevicesNumOrdersOnHold" to numOrdersOnHold!!,
                                                        "numOrdersOnHold" to numOrdersOnHold!!) as Map<String, Any>
                                                ).addOnCompleteListener {
                                                    // Set the Device Resrved
                                                    mFirestoreClass.collection("devices").document(idDevice)
                                                        .update(
                                                            hashMapOf("currentState" to "Reserved",
                                                                "devCurrentState" to "Reserved??") as Map<String, Any>
                                                        )
                                                }
                                        }else{
                                            // Nothing
                                        }
                                    }.addOnFailureListener {
                                        //
                                    }
                            }else{
                                if(numOrdersOnHold!! > 0 && (numCurrentOrder!!) !=-1){
                                    // != (-1) : this is not the first order of the device

                                    mFirestoreClass.collection("orders")
                                        .document(listOrders!![numCurrentOrder!!])
                                        .get().addOnCompleteListener {

                                            if(it.isSuccessful){
                                                var currentOrderEndDay =
                                                    it.result!!.toObject(Order::class.java)!!.getorderDateEnd()
                                                var currentOrderStartDay =
                                                    it.result!!.toObject(Order::class.java)!!.getorderDateStart()

                                                if(currentOrderStartDay != null){

                                                    if(todayAfterOrEquals(currentOrderStartDay!!)){

                                                        // Need to start order
                                                        // Make Device reserved
                                                        mFirestoreClass.collection("devices").document(idDevice)
                                                            .update(
                                                                hashMapOf("currentState" to "Reserved",
                                                                    "devCurrentState" to "Reserved") as Map<String, Any>
                                                            ).addOnCompleteListener {
                                                                // After that we check if the End of Day of Order are so far or not

                                                                if (currentOrderEndDay != null) {

                                                                    if(todayAfter(currentOrderEndDay!!)){

                                                                        // The Order was achieve
                                                                        if((numOrdersOnHold-1)==0){
                                                                            // There is no order After That One
                                                                            // Set the new Modif on the QueueDevice branch

                                                                            mFirestoreClass.collection("queueDevices")
                                                                                .document(idDevice)
                                                                                .update(
                                                                                    hashMapOf(
                                                                                        "queueDevicesNumOrdersOnHold" to numOrdersOnHold-1,
                                                                                        "numOrdersOnHold" to numOrdersOnHold-1)
                                                                                            as Map<String, Any>
                                                                                ).addOnCompleteListener {

                                                                                    mFirestoreClass.collection("devices").document(idDevice)
                                                                                        .update(
                                                                                            hashMapOf("currentState" to "Available",
                                                                                                "devCurrentState" to "Available") as Map<String, Any>
                                                                                        )

                                                                                }.addOnFailureListener {
                                                                                    //
                                                                                }


                                                                        }else{

                                                                            mFirestoreClass.collection("queueDevices")
                                                                                .document(idDevice)
                                                                                .update(
                                                                                    hashMapOf(
                                                                                        "currentNumOrder" to numCurrentOrder +1,
                                                                                        "queueDevicesCurrentNumberOrder" to numCurrentOrder + 1,
                                                                                        "queueDevicesNumOrdersOnHold" to numOrdersOnHold-1,
                                                                                        "numOrdersOnHold" to numOrdersOnHold-1)
                                                                                            as Map<String, Any>
                                                                                ).addOnCompleteListener {

                                                                                    mFirestoreClass.collection("devices").document(idDevice)
                                                                                        .update(
                                                                                            hashMapOf("currentState" to "Available",
                                                                                                "devCurrentState" to "Available") as Map<String, Any>
                                                                                        )
                                                                                }.addOnFailureListener {
                                                                                    //
                                                                                }
                                                                        }
                                                                    }else{

                                                                    }
                                                                }else{

                                                                }
                                                            }.addOnFailureListener {
                                                                //
                                                            }
                                                    }else{

                                                    }
                                                }else{
                                                    // currentOrderStartDay is empty
                                                }
                                            }
                                        }.addOnFailureListener {
                                            //
                                        }
                                }else if(numOrdersOnHold!! > 0 && (numCurrentOrder!!) == -1){
                                    // == (1) this is the first order of the device

                                    mFirestoreClass.collection("orders")
                                        .document(listOrders!![numCurrentOrder!!+1])
                                        .get().addOnCompleteListener {

                                            if (it.isSuccessful) {
                                                var currentStartDay =
                                                    it.result!!.toObject(Order::class.java)!!
                                                        .getorderDateStart()

                                                if (currentStartDay != null) {

                                                    if( today.equals(currentStartDay!!) ) {

                                                        // Set the new Modif on the QueueDevice branch
                                                        mFirestoreClass.collection("queueDevices")
                                                            .document(idDevice)
                                                            .update(
                                                                hashMapOf(
                                                                    "currentNumOrder" to numCurrentOrder+1,
                                                                    "queueDevicesCurrentNumberOrder" to numCurrentOrder+1)
                                                                        as Map<String, Any>
                                                            ).addOnCompleteListener {
                                                                // Change Device to Reserved
                                                                mFirestoreClass.collection("devices").document(idDevice)
                                                                    .update(
                                                                        hashMapOf("currentState" to "Reserved",
                                                                            "devCurrentState" to "Reserved") as Map<String, Any>
                                                                    )
                                                            }.addOnFailureListener {
                                                                //
                                                            }
                                                    }else{
                                                        //
                                                    }
                                                }else{
                                                    // noth..
                                                }
                                            }
                                            else{
                                                // noth..
                                            }
                                        }.addOnFailureListener {
                                            //
                                        }

                                }else{

                                }
                            }

                        }else{

                        }
                    }

                }.addOnFailureListener {
                //
            }
    }

    fun todayAfterOrEquals(deviceDate: Date) : Boolean{
        return today.after(deviceDate) || today.equals(deviceDate)
    }

    fun todayBeforeOrEquals(deviceDate: Date) : Boolean {
        return today.before(deviceDate) || today.equals(deviceDate)
    }

    fun todayAfter(deviceDate: Date) : Boolean{
        return today.after(deviceDate)
    }


}