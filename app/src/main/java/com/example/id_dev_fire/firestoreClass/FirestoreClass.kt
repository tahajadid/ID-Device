package com.example.id_dev_fire.firestoreClass

import android.app.Dialog
import android.widget.Toast
import com.example.id_dev_fire.model.Cupboard
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.model.Employer
import com.example.id_dev_fire.model.Order
import com.example.id_dev_fire.ui.AddCupboard.AddCupboardFragment
import com.example.id_dev_fire.ui.AddDevice.AddDeviceFragment
import com.example.id_dev_fire.ui.AddEmployer.AddEmployerFragment
import com.example.id_dev_fire.ui.OrderDevice.OrderDeviceFragment
import com.example.id_dev_fire.ui.register.RegisterActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFirestoreClass = FirebaseFirestore.getInstance()

    fun addEmployerFirebase(fragment: AddEmployerFragment,employerInfo: Employer){

        val newEmployerRef = mFirestoreClass.collection("employers").document()
        val employer = Employer(
                newEmployerRef.id,
                employerInfo.firstName,
                employerInfo.lastName,
                employerInfo.email,
                employerInfo.phone,
                employerInfo.gender,
                employerInfo.role,
                employerInfo.project)

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

        val newEmployerRef = mFirestoreClass.collection("employers").document()

        newEmployerRef.set(employerInfo)
                .addOnSuccessListener {
                    Toast.makeText(
                            activity,
                            "Welcome to ID-Device Application",
                            Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener{
                    Toast.makeText(
                            activity,
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
                deviceInfo.state,
                deviceInfo.currentState)

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
                orderInfo.deviceName,
                orderInfo.deviceOwner,
                orderInfo.currentOwner,
                orderInfo.startDay,
                orderInfo.endDay,
                orderInfo.reason,
                orderInfo.decision)

        newOrderdRef
                .set(newOrder)
                .addOnSuccessListener {

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

}