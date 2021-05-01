package com.example.id_dev_fire.ui.AddDevice

import android.app.Dialog
import android.nfc.Tag
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.MutableInt
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.model.Cupboard
import com.example.id_dev_fire.model.Device
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates

class AddDeviceFragment : Fragment() {

    lateinit var nameDevice : EditText
    lateinit var versionDevice : EditText
    lateinit var supportedOSDevice : EditText
    lateinit var featuresDevice : EditText

    lateinit var buttonAddDevice : Button

    lateinit var cupboardSpinner: Spinner
    lateinit var managerSpinner: Spinner

    lateinit var cupboardSelected : String
    lateinit var managerSelected : String

    lateinit var lastCup : Cupboard
    var newlist = mutableListOf<String>()

    private val mFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get list of Cupboards from database

        mFirestore.collection("cupboards")
                .get().addOnCompleteListener {
                    if (it.isSuccessful){
                        for (res in it.result!!) {
                            val cup = res.toObject(Cupboard::class.java)
                            Log.d("taag","doc : ${cup.getName()} --")
                            newlist.add(cup.getName())
                        }
                    }

                }.addOnFailureListener {
                    Log.d("taag","There is an Error} --")
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_add_device, container, false)

        nameDevice = root.findViewById(R.id.deviceName_et)
        versionDevice = root.findViewById(R.id.deviceVersion_et)
        supportedOSDevice = root.findViewById(R.id.deviceSupportedOS_et)
        featuresDevice = root.findViewById(R.id.deviceFeature_et)

        cupboardSpinner = root.findViewById(R.id.cupboard_spinner)
        managerSpinner = root.findViewById(R.id.owner_spinner)

        buttonAddDevice = root.findViewById(R.id.addDevice_button)

        val li = newlist
        val listStable = mutableListOf("EVS_1","EVS_2","MESA_1","MiMs_1")

        // Put the list of cupboard from Firestore into the dropdown
        ArrayAdapter(
                requireActivity().applicationContext,
                android.R.layout.simple_spinner_item,
                listStable
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cupboardSpinner.adapter = adapter
        }

        // Get the cupboard selected

        cupboardSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val text: String = p0?.getItemAtPosition(p2).toString()
                cupboardSelected = text
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                val text: String = p0?.getItemAtPosition(0).toString()
                cupboardSelected = text
            }
        }

        // Get the owner selected
        managerSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val text: String = p0?.getItemAtPosition(p2).toString()
                managerSelected = text
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // Click on Add Employer Button
        buttonAddDevice.setOnClickListener{
            addDevice()
        }

        return root
    }

    private fun addDevice() {

        if(checkInformationDetails()) {

            val actualName = nameDevice.text.toString().trim(){ it <= ' ' }
            val actualVersion = versionDevice.text.toString().trim(){ it <= ' ' }
            val actualOS = supportedOSDevice.text.toString().trim(){ it <= ' ' }
            val actualFeature = featuresDevice.text.toString().trim(){ it <= ' ' }

            val device = Device(
                    actualName,
                    actualName,
                    actualVersion,
                    actualOS,
                    actualFeature,
                    cupboardSelected,
                    managerSelected,
                    "Available",
                    "Not Reserved")

            FirestoreClass().addDeviceFirebase(this,device)
            resetValue()

        }else {
            TODO()
        }
    }

    private fun checkInformationDetails() : Boolean{

        return when{

            TextUtils.isEmpty(nameDevice.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                        this.context,
                        "please enter name of the device",
                        Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(versionDevice.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                        this.context,
                        "please enter the version of the device",
                        Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(supportedOSDevice.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                        this.context,
                        "please enter the supported OS of the device",
                        Toast.LENGTH_SHORT
                ).show()
                false
            }

            else -> true
        }

    }

    private fun resetValue() {

        nameDevice.text.clear()
        versionDevice.text.clear()
        supportedOSDevice.text.clear()
        featuresDevice.text.clear()
    }

}