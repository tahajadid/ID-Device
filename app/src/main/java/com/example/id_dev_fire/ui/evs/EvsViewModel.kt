package com.example.id_dev_fire.ui.evs

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.id_dev_fire.firestoreClass.ImpactOrder
import com.example.id_dev_fire.model.Device
import com.google.firebase.firestore.FirebaseFirestore

class EvsViewModel(application: Application) : AndroidViewModel(application) {

    var readAllData = MutableLiveData<List<Device>>()
    private var mFirestore = FirebaseFirestore.getInstance()
    private var AllData: MutableList<Device> = arrayListOf()

    init {
        if(readAllData.value.isNullOrEmpty()) {
            mFirestore.collection("devices")
                .whereEqualTo("projectName","EVS")
                .get().addOnCompleteListener {

                    if (it.isSuccessful){
                        for (result in it.result!!) {
                            val devInfo = result.toObject(Device::class.java)
                            AllData.add(devInfo)
                        }
                    }
                    // Set the list of device in the LiveData var
                    readAllData.value = AllData

                }.addOnFailureListener {

                }
        }

    }


}