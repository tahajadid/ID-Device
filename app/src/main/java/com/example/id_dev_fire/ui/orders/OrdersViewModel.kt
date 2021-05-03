package com.example.id_dev_fire.ui.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class OrdersViewModel(application: Application) : AndroidViewModel(application) {

    var readAllData = MutableLiveData<List<Order>>()

    private var mFirestore = FirebaseFirestore.getInstance()
    private var mFirestoreAuth = FirebaseAuth.getInstance()
    private var mFirestoreUser : FirebaseUser

    private var currentOwner_uid : String
    private var AllData: MutableList<Order> = arrayListOf()

    init {

        mFirestoreUser= mFirestoreAuth.currentUser
        currentOwner_uid = mFirestoreUser.uid

        mFirestore.collection("orders")
                .whereEqualTo("deviceOwner","Manager EVS")
                .get().addOnCompleteListener {

                    if (it.isSuccessful){
                        for (result in it.result!!) {
                            val orderInfo = result.toObject(Order::class.java)
                            AllData.add(orderInfo)
                        }
                    }
                    // Set the list of device in the LiveData var
                    readAllData.value = AllData

                }.addOnFailureListener {

                }
    }

}