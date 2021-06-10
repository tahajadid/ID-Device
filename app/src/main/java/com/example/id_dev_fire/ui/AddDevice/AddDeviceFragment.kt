package com.example.id_dev_fire.ui.AddDevice

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.model.Cupboard
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.model.Employer
import com.google.firebase.firestore.FirebaseFirestore

class AddDeviceFragment : Fragment() {

    lateinit var nameDevice : EditText
    lateinit var versionDevice : EditText
    lateinit var supportedOSDevice : EditText
    lateinit var featuresDevice : EditText
    lateinit var serviceNameDevice : EditText

    lateinit var buttonAddDevice : Button

    lateinit var cupboardSpinner: Spinner
    lateinit var managerSpinner: Spinner

    lateinit var cupboardSelected : String
    lateinit var projectManagerSelected : String
    lateinit var idManagerSelected : String
    lateinit var fullNameManagerSelected : String

    lateinit var adapterCupboard : ArrayAdapter<String>
    lateinit var adapterManagers : ArrayAdapter<String>

    var newlistCupboards = mutableListOf<String>()
    var labelManagers = mutableListOf<String>()
    var allManagers = mutableListOf<Employer>()

    private val mFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_add_device, container, false)

        nameDevice = root.findViewById(R.id.deviceName_et)
        versionDevice = root.findViewById(R.id.deviceVersion_et)
        supportedOSDevice = root.findViewById(R.id.deviceSupportedOS_et)
        featuresDevice = root.findViewById(R.id.deviceFeature_et)
        serviceNameDevice = root.findViewById(R.id.deviceServiceName_et)

        cupboardSpinner = root.findViewById(R.id.cupboard_spinner)
        managerSpinner = root.findViewById(R.id.owner_spinner)

        buttonAddDevice = root.findViewById(R.id.addDevice_button)

        // Get the list of Cupboards and Managers from Database
        getAllCupboards()
        getAllManagers()

        // Set the list of Cupboard and Managers in spinners with an Adapter
        adapterCupboard = ArrayAdapter(
                requireActivity().applicationContext,
                android.R.layout.simple_spinner_item,
                newlistCupboards
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cupboardSpinner.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        adapterManagers = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_spinner_item,
            labelManagers
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            managerSpinner.adapter = adapter
            adapter.notifyDataSetChanged()
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
                val currentId : String = allManagers[p2].getEmployerId()
                val currentProject : String = allManagers[p2].getEmployerProject()
                fullNameManagerSelected = allManagers[p2].getEmployerFirstName() + " " + allManagers[p2].getEmployerLastName()
                idManagerSelected = currentId
                projectManagerSelected = currentProject
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

    private fun getAllManagers() {
        // Get list of Cupboards from database
        mFirestore.collection("employers").whereEqualTo("employerRole","Manager")
            .get().addOnSuccessListener {
                for (res in it.toObjects(Employer::class.java)) {
                    labelManagers.add(res.getEmployerFirstName()+" - "+res.getEmployerProject())
                    allManagers.add(res)
                    adapterManagers.notifyDataSetChanged()
                }
            }.addOnFailureListener {
            //
            }
    }

    private fun getAllCupboards() {

        // Get list of Cupboards from database
        mFirestore.collection("cupboards").orderBy("name")
            .get().addOnSuccessListener {
                    for (res in it.toObjects(Cupboard::class.java)) {
                        newlistCupboards.add(res.getName())
                        adapterCupboard.notifyDataSetChanged()
                    }
            }.addOnFailureListener {
            //
            }

    }

    private fun addDevice() {

        if(checkInformationDetails()) {

            val actualName = nameDevice.text.toString().trim(){ it <= ' ' }
            val actualVersion = versionDevice.text.toString().trim(){ it <= ' ' }
            val actualOS = supportedOSDevice.text.toString().trim(){ it <= ' ' }
            val actualFeature = featuresDevice.text.toString().trim(){ it <= ' ' }
            val actualservice = serviceNameDevice.text.toString().trim(){ it <= ' ' }

            val device = Device(
                    "0",
                    actualName,
                    actualVersion,
                    actualOS,
                    actualservice,
                    actualFeature,
                    cupboardSelected,
                    idManagerSelected,
                    fullNameManagerSelected,
                    projectManagerSelected,
                    "Available",
                    "Not Reserved")

            FirestoreClass().addDeviceFirebase(this,device)
            resetValue()

        }else {
            Toast.makeText(
                    this.context,
                    "Try again",
                    Toast.LENGTH_SHORT
            ).show()
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
        serviceNameDevice.text.clear()
    }

}