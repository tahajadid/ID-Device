package com.example.id_dev_fire.ui.mesa

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.id_dev_fire.model.Device
import com.google.firebase.firestore.FirebaseFirestore

class MesaViewModel(application: Application): AndroidViewModel(application) {

    var readAllData = MutableLiveData<List<Device>>()
    private var mFirestore = FirebaseFirestore.getInstance()
    private var AllData: MutableList<Device> = arrayListOf()

    init {

        mFirestore.collection("devices")
                .whereEqualTo("deviceOwner","Manager MESA")
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
                    Log.d("taag","There is an Error} --")
                }
    }

}