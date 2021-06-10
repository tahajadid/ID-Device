package com.example.id_dev_fire.ui.OrderDevice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.model.Employer
import com.example.id_dev_fire.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class OrderDeviceFragment : Fragment() {

    lateinit var order: Order
    lateinit var reason : EditText
    lateinit var buttonMakeOrder : Button
    lateinit var datePickerFrom: DatePicker
    lateinit var datePickerTo: DatePicker

    private lateinit var deviceOwner : String
    private lateinit var fullNameDeviceOwner : String
    private lateinit var currentOwner_uid : String

    private lateinit var dateStart : Date
    private lateinit var dateEnd : Date

    private val mFirestore = FirebaseFirestore.getInstance()
    private var mFirestoreAuth = FirebaseAuth.getInstance()
    private lateinit var mFirestoreUser : FirebaseUser

    private val args by navArgs<OrderDeviceFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_order_device, container, false)
        reason = root.findViewById(R.id.reasonOrder_et)
        datePickerFrom = root.findViewById(R.id.calendarFrom)
        datePickerTo = root.findViewById(R.id.calendarTo)
        buttonMakeOrder = root.findViewById(R.id.PlaceOrder_button)

        // Make the name of device on Top
        root.findViewById<TextView>(R.id.nameDevOrder_tv).setText(args.deviceNameForOrder)


        // Getting the current date
        val sdf = SimpleDateFormat("d-M-yyyy")
        val currentDay = sdf.format(Date())
        val today  = Calendar.getInstance()

        // Initialize the two value of Start/End day to avoid the lateinit propriety of two variables

        root.findViewById<TextView>(R.id.toDate_tv).setText(currentDay.toString())
        root.findViewById<TextView>(R.id.fromDate_tv).setText(currentDay.toString())

        dateEnd = Date(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH))
        dateStart = Date(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH))

        datePickerFrom.init(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH)){
            view, year, month, day ->
            dateStart = Date(year,month,day)

            // Plot the Current Day by default
            val actualStart = day.toString()+"-"+(month+1).toString()+"-"+year.toString()
            val actualDateStart = root.findViewById<TextView>(R.id.fromDate_tv)
            actualDateStart.setText(actualStart)
        }

        datePickerTo.init(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH)){
            view, year, month, day ->
            dateEnd = Date(year,month,day)

            // Plot the Current Day by default
            val actualEnd = day.toString()+"-"+(month+1).toString()+"-"+year.toString()
            val actualDateEnd = root.findViewById<TextView>(R.id.toDate_tv)
            actualDateEnd.setText(actualEnd)
        }

        buttonMakeOrder.setOnClickListener {
            getAllInformation()
        }

        return root
    }


    fun checkDate() : Boolean {

        // We check if the period is exact
        return dateEnd.after(dateStart) || dateEnd.equals(dateStart)
    }

    fun getAllInformation() {

        if(checkDate()){

            mFirestore.collection("devices")
                    .whereEqualTo("devName",args.deviceNameForOrder)
                    .get().addOnCompleteListener {

                        if (it.isSuccessful){
                            for (result in it.result!!) {
                                val devInfo = result.toObject(Device::class.java)
                                deviceOwner = devInfo.deviceOwner.toString()
                                fullNameDeviceOwner = devInfo.fullNameOwner.toString()
                            }
                        }

                        mFirestoreUser= mFirestoreAuth.currentUser
                        currentOwner_uid = mFirestoreUser.uid

                        mFirestore.collection("employers").document(mFirestoreUser.uid)
                            .get().addOnCompleteListener {
                                val CurrentEmployer = it.result!!.toObject(Employer::class.java)
                                val fullNameCurrentOwner : String = CurrentEmployer!!.getEmployerFirstName()+" "+
                                        CurrentEmployer.getEmployerLastName()
                                /*
                                * This step just to see if Document in Firestore was created two times
                                * */

                                val reason_to_put = reason.text.toString()

                                // Create an Order Object
                                // We pass at first the device name as an id
                                order = Order(
                                    args.deviceNameForOrder,
                                    args.idDeviceToPut,
                                    args.deviceNameForOrder,
                                    deviceOwner,
                                    fullNameDeviceOwner,
                                    currentOwner_uid,
                                    fullNameCurrentOwner,
                                    dateStart,
                                    dateEnd,
                                    reason_to_put,
                                    "On Hold"
                                )

                                FirestoreClass().addOrderFirebase(this,order)

                                val action = OrderDeviceFragmentDirections.actionNavOrderDeviceFragmentToNavOrders()

                                // take the id of the selected device
                                findNavController().navigate(action)
                            }.addOnFailureListener {
                                // Noth..
                            }


                    }.addOnFailureListener {

                    }
        }else {
            Toast.makeText(
                    this.context,
                    "The End is after the Start Date ",
                    Toast.LENGTH_SHORT
            ).show()
        }

    }

}