package com.example.id_dev_fire.firestoreClass

import android.app.Dialog
import android.widget.ProgressBar
import android.widget.Toast
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Cupboard
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.model.Employer
import com.example.id_dev_fire.ui.AddCupboard.AddCupboardFragment
import com.example.id_dev_fire.ui.AddDevice.AddDeviceFragment
import com.example.id_dev_fire.ui.AddEmployer.AddEmployerFragment
import com.example.id_dev_fire.ui.register.RegisterActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    lateinit var mProgressDialog: Dialog
    private val mFirestoreClass = FirebaseFirestore.getInstance()

    fun addEmployerFirebase(fragment: AddEmployerFragment,employerInfo: Employer){

        mFirestoreClass.collection("employers")
                .document(employerInfo.id)
                .set(employerInfo, SetOptions.merge())
                .addOnSuccessListener {

                    Toast.makeText(
                            fragment.context,
                            "The Employer was added ^^ ",
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

        mFirestoreClass.collection("employers")
                .document(employerInfo.id)
                .set(employerInfo, SetOptions.merge())
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

        mFirestoreClass.collection("devices")
                .add(deviceInfo)
                .addOnSuccessListener {

                    Toast.makeText(
                            fragment.context,
                            "The Device was added ^^ ",
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

        mFirestoreClass.collection("cupboards")
            .add(cupboardInfo)
            .addOnSuccessListener {

                Toast.makeText(
                    fragment.context,
                    "The Cupboard was added ^^ ",
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