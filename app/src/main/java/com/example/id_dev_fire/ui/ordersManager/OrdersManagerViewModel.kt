package com.example.id_dev_fire.ui.ordersManager

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.id_dev_fire.model.Employer
import com.example.id_dev_fire.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class OrdersManagerViewModel(application: Application) : AndroidViewModel(application) {

    var readAllData = MutableLiveData<List<Order>>()
    lateinit var tribManager : String

    private var mFirestore = FirebaseFirestore.getInstance()
    private var mFirestoreAuth = FirebaseAuth.getInstance()
    private var mFirestoreUser : FirebaseUser

    private var AllDataOrders : MutableList<Order> = arrayListOf()

    init {

        mFirestoreUser = mFirestoreAuth.currentUser

        mFirestore.collection("employers")
                .whereEqualTo("id",mFirestoreUser.uid)
                .get().addOnCompleteListener {
                    /* ------------------ */
                    Log.d("tahaa","enter collection employers")

                    if (it.isSuccessful){
                        /* ------------------ */
                        Log.d("tahaa","enter if it succ")
                        for (manager in it.result!!) {
                            /* ------------------ */
                            Log.d("tahaa","enter for Loop")
                            val managerInfo = manager.toObject(Employer::class.java)
                            tribManager = managerInfo.getEmployerRole() + " " + managerInfo.getEmployerProject()
                            Log.d("devv", "--------- ${tribManager}")
                        }

                        mFirestore.collection("orders")
                                .whereEqualTo("deviceOwner",tribManager.toString())
                                .get().addOnCompleteListener {

                                    /* ------------------ */
                                    Log.d("tahaa","enter Orders collection")
                                    if (it.isSuccessful){

                                        /* ------------------ */
                                        Log.d("tahaa","enter orders if succ")
                                        for (result in it.result!!) {
                                            /* ------------------ */
                                            Log.d("tahaa","enter orders for Loop")
                                            val orderInfo = result.toObject(Order::class.java)
                                            AllDataOrders.add(orderInfo)
                                        }
                                    }
                                    // Set the list of device in the LiveData var
                                    readAllData.value = AllDataOrders

                                }.addOnFailureListener {

                                }
                    }



                }.addOnFailureListener{

                }

    }

}