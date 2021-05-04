package com.example.id_dev_fire.ui.OrderDevice

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.model.Order
import com.example.id_dev_fire.ui.singleDevice.SingleDeviceFragmentArgs
import com.example.id_dev_fire.ui.singleDevice.SingleDeviceFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class OrderDeviceFragment : Fragment() {

    lateinit var order: Order
    lateinit var reason : EditText
    lateinit var buttonMakeOrder : Button
    lateinit var datePickerFrom: DatePicker
    lateinit var datePickerTo: DatePicker

    private lateinit var deviceOwner : String
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
        val currentD = sdf.format(Date())
        val today  = Calendar.getInstance()

        // Initialize the two value of Start/End day to avoid the lateinit propriety of two variables

        root.findViewById<TextView>(R.id.toDate_tv).setText(currentD.toString())
        root.findViewById<TextView>(R.id.fromDate_tv).setText(currentD.toString())

        // Initialize the Picker to know the choice of user
        datePickerFrom.init(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH)){
            view, year, month, day ->
            val month = month + 1
            dateStart = Date(year,month,day)
            val actual = day.toString()+"-"+month.toString()+"-"+year.toString()
            val actualDateStart = root.findViewById<TextView>(R.id.fromDate_tv)
            actualDateStart.setText(actual)
        }

        datePickerTo.init(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH)){
            view, year, month, day ->
            val month = month + 1
            dateEnd = Date(year,month,day)
            val actual = day.toString()+"-"+month.toString()+"-"+year.toString()
            val actualDateEnd = root.findViewById<TextView>(R.id.toDate_tv)
            actualDateEnd.setText(actual)
        }

        buttonMakeOrder.setOnClickListener {
            getAllInformation()
        }

        return root
    }


    fun getAllInformation() {

        mFirestore.collection("devices")
                .whereEqualTo("devName",args.deviceNameForOrder)
                .get().addOnCompleteListener {

                    if (it.isSuccessful){
                        for (result in it.result!!) {
                            val devInfo = result.toObject(Device::class.java)
                            deviceOwner = devInfo.deviceOwner.toString()
                        }
                    }

                    mFirestoreUser= mFirestoreAuth.currentUser
                    currentOwner_uid = mFirestoreUser.uid

                    /*
                    * This step just to see if Document in Firestore was created two times
                    * */

                    val dateStart_to_put = dateStart.toString()
                    val dateEnd_to_put = dateEnd.toString()
                    val reason_to_put = reason.text.toString()

                    // Create an Order Object
                    order = Order(
                            args.deviceNameForOrder,
                            args.deviceNameForOrder,
                            deviceOwner,
                            currentOwner_uid,
                            dateStart_to_put,
                            dateEnd_to_put,
                            reason_to_put,
                            "On Hold"
                    )

                    FirestoreClass().addOrderFirebase(this,order)

                    val action = OrderDeviceFragmentDirections.actionNavOrderDeviceFragmentToNavOrders()
                    // take the id of the selected device
                    findNavController().navigate(action)

                }.addOnFailureListener {
                    // There is an Error !
                }

    }

}