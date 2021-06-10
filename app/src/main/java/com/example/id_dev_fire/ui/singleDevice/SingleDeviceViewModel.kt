package com.example.id_dev_fire.ui.singleDevice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.id_dev_fire.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class SingleDeviceViewModel(idDevice : String, application: Application) : AndroidViewModel(
    application
)  {

    var readAllData = MutableLiveData<List<Order>>()

    val calendar = Calendar.getInstance()
    val today = Date(calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))

    private var mFirestore = FirebaseFirestore.getInstance()
    private var AllData: MutableList<Order> = arrayListOf()

    init {

        // Need to add orderBy("orderDateStart", Query.Direction.DESCENDING)
        mFirestore.collection("orders")
                .whereEqualTo("idDevice",idDevice)
                .get().addOnCompleteListener {

                    if (it.isSuccessful){

                        for(res in it.result.toObjects(Order::class.java)){
                            if(res.getorderDateEnd()!!.after(today)){
                                AllData.add(res)
                            }else{
                                // Noth..
                            }
                        }

                    }

                // Set the list of device in the LiveData var
                readAllData.value = AllData
                }.addOnFailureListener {

                }
    }

}