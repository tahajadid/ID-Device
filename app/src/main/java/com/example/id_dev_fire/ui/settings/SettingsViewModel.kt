package com.example.id_dev_fire.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.id_dev_fire.model.Employer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SettingsViewModel : ViewModel() {

    var readAllData = MutableLiveData<List<Employer>>()
    private var mFirestore = FirebaseFirestore.getInstance()
    private var mFirestoreAuth = FirebaseAuth.getInstance()
    private lateinit var mFirestoreUser : FirebaseUser
    private var AllData: MutableList<Employer> = arrayListOf()

    init {

        mFirestoreUser= mFirestoreAuth.currentUser
            mFirestore.collection("employers")
                .whereEqualTo("id",mFirestoreUser.uid)
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