package com.example.id_dev_fire.ui.listDamagedDevices

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.id_dev_fire.model.DamagedDevice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ListDamagedViewModel(application: Application) : AndroidViewModel(application)  {

    var readAllData = MutableLiveData<List<DamagedDevice>>()

    private var mFirestore = FirebaseFirestore.getInstance()
    private var mFireAuth = FirebaseAuth.getInstance()
    private var AllData: MutableList<DamagedDevice> = arrayListOf()

    init {

        mFirestore.collection("damagedDevice")
            .whereEqualTo("decDeviceOwnerDevice",mFireAuth.uid)
            .get().addOnCompleteListener {

                if (it.isSuccessful){
                    for (result in it.result!!) {
                        val newDamaged = result.toObject(DamagedDevice::class.java)
                            AllData.add(newDamaged)
                    }
                }
                // Set the list of device in the LiveData var
                readAllData.value = AllData

            }.addOnFailureListener {
            }

    }

}