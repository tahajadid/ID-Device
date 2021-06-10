package com.example.id_dev_fire.ui.ordersManager

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.id_dev_fire.model.Employer
import com.example.id_dev_fire.model.Order
import com.example.id_dev_fire.notificationClasses.PushNotification
import com.example.id_dev_fire.notificationClasses.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrdersManagerViewModel(application: Application) : AndroidViewModel(application) {

    var readAllData = MutableLiveData<List<Order>>()
    lateinit var idManager : String

    private var mFirestore = FirebaseFirestore.getInstance()
    private var mFirestoreAuth = FirebaseAuth.getInstance()
    private var mFirestoreUser : FirebaseUser

    private var AllDataOrders : MutableList<Order> = arrayListOf()

    init {

        mFirestoreUser = mFirestoreAuth.currentUser

        mFirestore.collection("employers")
                .whereEqualTo("id",mFirestoreUser.uid)
                .get().addOnCompleteListener {

                    if (it.isSuccessful){
                        for (manager in it.result!!) {
                            val managerInfo = manager.toObject(Employer::class.java)
                            idManager = managerInfo.getEmployerId()
                        }
                        mFirestore.collection("orders")
                                .whereEqualTo("deviceOwner",idManager)
                                .get().addOnCompleteListener {

                                    if (it.isSuccessful){

                                        for (result in it.result!!) {
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

    private fun sendNotification(notification : PushNotification) = CoroutineScope(Dispatchers.IO).launch{

        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d("testNotif","#### Successful" )
            }else{
                Log.d("testNotif", response.errorBody().toString() )
            }

        }catch (e : Exception){
            Log.d("testNotif",e.toString() )
        }

    }

}