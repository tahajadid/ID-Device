package com.example.id_dev_fire.firestoreClass

import android.widget.Toast
import com.example.id_dev_fire.model.*
import com.example.id_dev_fire.notificationClasses.NotificationData
import com.example.id_dev_fire.notificationClasses.PushNotification
import com.example.id_dev_fire.notificationClasses.RetrofitInstance
import com.example.id_dev_fire.ui.AddCupboard.AddCupboardFragment
import com.example.id_dev_fire.ui.AddDevice.AddDeviceFragment
import com.example.id_dev_fire.ui.AddEmployer.AddEmployerFragment
import com.example.id_dev_fire.ui.OrderDevice.OrderDeviceFragment
import com.example.id_dev_fire.ui.bug.BugFragment
import com.example.id_dev_fire.ui.register.RegisterActivity
import com.example.id_dev_fire.ui.settings.SettingsFragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirestoreClass {

    private val mFirestoreClass = FirebaseFirestore.getInstance()

    fun addEmployerFirebase(fragment: AddEmployerFragment,employerInfo: Employer){

        val newEmployerRef = mFirestoreClass.collection("employers")
            .document(employerInfo.getEmployerId())
        val employer = Employer(
                newEmployerRef.id,
                employerInfo.firstName,
                employerInfo.lastName,
                employerInfo.email,
                employerInfo.phone,
                employerInfo.gender,
                employerInfo.role,
                employerInfo.project,
                false)

                newEmployerRef.set(employer)
                .addOnSuccessListener {

                    Toast.makeText(
                            fragment.context,
                            "The Employer was added",
                            Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener{

                    Toast.makeText(
                            fragment.context,
                            "There was a problem, please try again :(",
                            Toast.LENGTH_SHORT
                    ).show()
                }
    }

    fun addEmployerActivityFirebase(activity: RegisterActivity,employerInfo: Employer){

        val newEmployerRef = mFirestoreClass.collection("employers")
            .document(employerInfo.getEmployerId())

        val employer = Employer(
            newEmployerRef.id,
            employerInfo.firstName,
            employerInfo.lastName,
            employerInfo.email,
            employerInfo.phone,
            employerInfo.gender,
            employerInfo.role,
            employerInfo.project,
            false)

        newEmployerRef.set(employer)
                .addOnSuccessListener {
                    Toast.makeText(
                            activity,
                            "Successfully registered. Please wait access from administrator !",
                            Toast.LENGTH_SHORT
                    ).show()

                    mFirestoreClass.collection("employers").whereEqualTo("role","Administrator")
                        .get().addOnCompleteListener {

                            if(it.isSuccessful){
                                for (actualAdmin in it.result!!.toObjects(Employer::class.java))
                                {

                                    mFirestoreClass.collection("tokens")
                                        .document(actualAdmin.getEmployerId())
                                        .get().addOnCompleteListener {
                                            if(it.isSuccessful){

                                                val newToken = it.result!!.toObject(TokenDevice::class.java)
                                                if(newToken != null){

                                                    val title = "New Registration"
                                                    val message = employerInfo.getEmployerFirstName() +
                                                            " " + employerInfo.getEmployerLastName() +
                                                            " was sing up"
                                                    PushNotification(
                                                        NotificationData(title,message),
                                                        newToken!!.getTokDeviceToken().toString()
                                                    ).also{
                                                        sendNotification(it)
                                                    }

                                                }else{
                                                    // Not success
                                                }
                                            }else{
                                                // Noth..
                                            }
                                        }.addOnFailureListener {
                                            // Noth..
                                        }
                                }
                            }else{

                            }
                        }.addOnFailureListener {

                        }
                }
                .addOnFailureListener{
                    Toast.makeText(
                            activity,
                            "There was a problem, please try again :(",
                            Toast.LENGTH_SHORT
                    ).show()
                }

    }

    fun addTokenFirebase(tokenDevice: TokenDevice){

        mFirestoreClass.collection("tokens")
            .document(tokenDevice.getTokId().toString()).get().addOnCompleteListener {
                if(it.isSuccessful){
                    if(it.result!!.exists()){

                        // make update
                        mFirestoreClass.collection("tokens")
                            .document(tokenDevice.getTokId().toString()).update(
                                hashMapOf("deviceToken" to tokenDevice.getTokDeviceToken(),
                                    "tokDeviceToken" to tokenDevice.getTokDeviceToken()) as Map<String, Any>
                            )
                    }else{

                        // just set the new token
                        mFirestoreClass.collection("tokens")
                            .document(tokenDevice.getTokId().toString()).set(tokenDevice)
                    }
                }else{

                }
            }.addOnFailureListener {

            }

    }


    fun addBugFirebase(fragment: BugFragment,bug: Bug){

        val bugRef = mFirestoreClass.collection("bugs").document()
        val newBug = Bug(bugRef.id,
            bug.getThisBugOwner(),
            bug.getBugType(),
            bug.getBugDescription())

        bugRef.set(newBug)
            .addOnSuccessListener {
                Toast.makeText(
                    fragment.requireContext(),
                    "Thank's, the Bug was declared to the admin",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener{
                Toast.makeText(
                    fragment.requireContext(),
                    "There was a problem, please try again :(",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    fun addDeviceFirebase(fragment: AddDeviceFragment,deviceInfo: Device){

        val newDeviceRef = mFirestoreClass.collection("devices").document()
        val device = Device(
                newDeviceRef.id,
                deviceInfo.deviceName,
                deviceInfo.version,
                deviceInfo.supportedOS,
                deviceInfo.serviceName,
                deviceInfo.features,
                deviceInfo.cupboard,
                deviceInfo.deviceOwner,
                deviceInfo.fullNameOwner,
                deviceInfo.projectName,
                deviceInfo.state,
                deviceInfo.currentState)

        // add also an empty Queue for the Device
        addQueueForDeviceFirebase(device.id.toString())

        newDeviceRef.set(device)
                .addOnSuccessListener {

                    Toast.makeText(
                            fragment.context,
                            "The Device was added",
                            Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener{

                    Toast.makeText(
                            fragment.context,
                            "There was a problem, please try again :(",
                            Toast.LENGTH_SHORT
                    ).show()
                }
    }

    fun addQueueForDeviceFirebase(idDevice: String){
        val newDeviceRef = mFirestoreClass.collection("queueDevices")
            .document(idDevice)

        val newQueueDevices = QueueDevices(idDevice,0, 0,mutableListOf())
        newDeviceRef.set(newQueueDevices).addOnCompleteListener {
            //
        }.addOnFailureListener {
            //
        }
    }

    fun addCupboardFirebase(fragment: AddCupboardFragment,cupboardInfo: Cupboard){

        val newCupboardRef = mFirestoreClass.collection("cupboards").document()
        val newCupboard = Cupboard(newCupboardRef.id,cupboardInfo.getName())

        newCupboardRef.set(newCupboard)
            .addOnSuccessListener {

                Toast.makeText(
                    fragment.context,
                    "The Cupboard was added",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener{

                Toast.makeText(
                    fragment.context,
                    "There was a problem, please try again :(",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun addOrderFirebase(fragment: OrderDeviceFragment,orderInfo: Order){

        val newOrderdRef = mFirestoreClass.collection("orders").document()
        val newOrder = Order(
                newOrderdRef.id,
                orderInfo.idDevice,
                orderInfo.deviceName,
                orderInfo.deviceOwner,
                orderInfo.fullNameDeviceOwner,
                orderInfo.currentOwner,
                orderInfo.fullNamecurrentOwner,
                orderInfo.startDay,
                orderInfo.endDay,
                orderInfo.reason,
                orderInfo.decision)

        newOrderdRef.set(newOrder)
            .addOnSuccessListener {

                mFirestoreClass.collection("tokens")
                    .document(newOrder.getorderDeviceOwner().toString())
                    .get().addOnCompleteListener {
                        if(it.isSuccessful){

                            val newToken = it.result!!.toObject(TokenDevice::class.java)
                            if(newToken != null){

                                val title = "New Order"
                                val message = "New order by " + orderInfo.fullNamecurrentOwner + " of < " + orderInfo.deviceName+" >"
                                PushNotification(
                                    NotificationData(title,message),
                                    newToken!!.getTokDeviceToken().toString()
                                ).also{
                                    sendNotification(it)
                                }

                            }else{
                                // Not success
                            }
                        }else{
                            // Noth..
                        }
                    }.addOnFailureListener {
                            // Noth..
                    }

                Toast.makeText(
                    fragment.context,
                    "The order was added",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener{

                Toast.makeText(
                    fragment.context,
                    "There was a problem, please try again :(",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    fun editPhoneEmployerFirebase(fragment: SettingsFragment,id : String, number : String){

        val newNumber = number.toLong()
        val newEmployerRef = mFirestoreClass.collection("employers").document(id)

        newEmployerRef.update(
            mapOf("employerPhone" to newNumber,
            "phone" to newNumber))
            .addOnSuccessListener {
                Toast.makeText(
                    fragment.context,
                    "The NumberPhone was changed",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener{

                Toast.makeText(
                    fragment.context,
                    "There was a problem, please try again :(",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    private fun sendNotification(notification : PushNotification) = CoroutineScope(Dispatchers.IO).launch{

        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){

            }else{

            }

        }catch (e : Exception){
        }

    }

}