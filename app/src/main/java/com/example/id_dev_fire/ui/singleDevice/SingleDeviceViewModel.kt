package com.example.id_dev_fire.ui.singleDevice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.id_dev_fire.model.Device
import com.google.firebase.firestore.FirebaseFirestore

class SingleDeviceViewModel(application: Application) : AndroidViewModel(application)  {

    private var mFirestore = FirebaseFirestore.getInstance()
    private lateinit var actualDev : Device

    init {

        mFirestore.collection("devices")
                .whereEqualTo("deviceOwner","Manager EVS")
                .get().addOnCompleteListener {

                    if (it.isSuccessful){
                        for (result in it.result!!) {
                            val devInfo = result.toObject(Device::class.java)
                        }
                    }

                }.addOnFailureListener {

                }
    }

}