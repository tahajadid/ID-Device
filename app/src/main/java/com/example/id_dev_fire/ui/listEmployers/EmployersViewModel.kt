package com.example.id_dev_fire.ui.listEmployers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.id_dev_fire.model.Employer
import com.google.firebase.firestore.FirebaseFirestore

class EmployersViewModel(application: Application) : AndroidViewModel(application)  {

    var readAllData = MutableLiveData<List<Employer>>()

    private var mFirestore = FirebaseFirestore.getInstance()

    private var AllData: MutableList<Employer> = arrayListOf()

    init {

        mFirestore.collection("employers")
            .get().addOnCompleteListener {

                if (it.isSuccessful){
                    for (result in it.result!!) {
                        val employerInfo = result.toObject(Employer::class.java)
                        AllData.add(employerInfo)
                    }
                }
                // Set the list of device in the LiveData var
                readAllData.value = AllData

            }.addOnFailureListener {

            }
    }

}