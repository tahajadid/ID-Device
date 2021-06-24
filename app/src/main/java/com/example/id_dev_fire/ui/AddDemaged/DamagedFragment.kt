package com.example.id_dev_fire.ui.AddDemaged

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.model.DamagedDevice
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.model.Employer
import com.example.id_dev_fire.model.Trib
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class DamagedFragment : Fragment() {

    lateinit var description : EditText
    lateinit var title : EditText

    lateinit var projectSpinner: Spinner
    lateinit var deviceSpinner: Spinner

    lateinit var uploadImgae : ImageView
    lateinit var buttonAddComplaint : Button

    lateinit var imageUri : Uri
    lateinit var imageView_1 : ImageView
    lateinit var imageView_2 : ImageView
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    var numberImageLoaded = 0

    lateinit var adapterProjects : ArrayAdapter<String>
    lateinit var adapterDevices : ArrayAdapter<String>

    lateinit var projectSelected : String
    lateinit var deviceSelected : String
    lateinit var nameDeviceOwner : String
    lateinit var idDeviceOwner : String
    lateinit var declaredById : String
    lateinit var declaredByFullName : String
    lateinit var nameDevice : String
    lateinit var idDevice : String

    var newlistProjects = mutableListOf<String>()
    var newlistDevices = mutableListOf<String>()
    var allListDevices = mutableListOf<Device>()

    private val mFirestore = FirebaseFirestore.getInstance()
    private val mFireauth = FirebaseAuth.getInstance()
    private val mFirestreClass = FirestoreClass()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_damaged, container, false)

        // Set an init values to the important variables
        projectSelected = " "
        nameDeviceOwner = "Anonyme"
        idDeviceOwner = " "

        description = root.findViewById(R.id.damagedDescription_et)
        title = root.findViewById(R.id.damagedTitle_et)

        projectSpinner = root.findViewById(R.id.project_spinner)
        deviceSpinner = root.findViewById(R.id.devices_spinner)

        buttonAddComplaint = root.findViewById(R.id.addComplaint_button)

        uploadImgae = root.findViewById(R.id.uploadImageDamaged_iv)
        imageView_1 = root.findViewById(R.id.damagedImageUploaded1_iv)
        imageView_2 = root.findViewById(R.id.damagedImageUploaded2_iv)

        // Storage
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        getAllProject()


        uploadImgae.setOnClickListener {
            choosePicture()
        }

        // Set the list of Project and Devices in spinners with an Adapter
        adapterProjects = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_spinner_item,
            newlistProjects
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            projectSpinner.adapter = adapter
            adapter.notifyDataSetChanged()
        }


        adapterDevices = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_spinner_item,
            newlistDevices
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            deviceSpinner.adapter = adapter
            adapter.notifyDataSetChanged()
        }


        // Get the project selected
        projectSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val newText: String = p0?.getItemAtPosition(p2).toString()
                projectSelected = newText
                getAllDevices()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                val newText: String = p0?.getItemAtPosition(0).toString()
                projectSelected = newText
            }
        }

        // Get the device selected
        deviceSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val text: String = p0?.getItemAtPosition(0).toString()
                deviceSelected = text
                nameDeviceOwner = allListDevices[p2].getdevFullNameOwner().toString()
                idDeviceOwner = allListDevices[p2].getdevOwner().toString()
                nameDevice = allListDevices[p2].getdevName().toString()
                idDevice = allListDevices[p2].getdevId().toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                val text: String = p0?.getItemAtPosition(0).toString()
                deviceSelected = text
            }
        }

        getInfoDeclaredBy(mFireauth.uid.toString())

        // Click on Add Dmaged Device Button
        buttonAddComplaint.setOnClickListener{
            if(checkInformation()){

                val damag = DamagedDevice(
                    idDevice,
                    nameDevice,
                    declaredById,
                    declaredByFullName,
                    title.text.toString(),
                    description.text.toString(),
                    idDeviceOwner)

                mFirestreClass.addDamagedDevice(this, damag)

                clearData()
            }
        }

        return root
    }

    private fun getInfoDeclaredBy(uid: String) {

        mFirestore.collection("employers").document(uid)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    val newEmployer = it.result!!.toObject(Employer::class.java)
                    declaredById = newEmployer!!.getEmployerId()
                    declaredByFullName = newEmployer.getEmployerFirstName() + " " + newEmployer.getEmployerLastName()
                }else{
                    declaredById = " "
                    declaredByFullName = "Anonyme"
                }
            }.addOnFailureListener {
                declaredById = " "
                declaredByFullName = "Anonyme"
            }
    }

    private fun checkInformation(): Boolean {

        return when{

            TextUtils.isEmpty(title.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                    requireContext(),
                    "please enter a title",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(description.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                    requireContext(),
                    "please enter a Description",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            else -> true
        }

    }


    private fun getAllProject() {
        // Get list of Project from database
        mFirestore.collection("trib").orderBy("nameTrib")
            .get().addOnSuccessListener {
                for (ourTrib in it.toObjects(Trib::class.java)) {
                    newlistProjects.add(ourTrib.getNameTrib())
                    adapterProjects.notifyDataSetChanged()
                }

            }.addOnFailureListener {
                // Noth..
            }
    }


    private fun getAllDevices() {
        // Get list of Project from database
        mFirestore.collection("devices").whereEqualTo("devProjectName",projectSelected)
            .get().addOnSuccessListener {
                // We need to clear the old list then add the new devices when the selected project
                // Was changed
                newlistDevices.clear()

                for (ourDev in it.toObjects(Device::class.java)) {
                    newlistDevices.add(ourDev.getdevName().toString())
                    allListDevices.add(ourDev)
                    adapterDevices.notifyDataSetChanged()
                }

            }.addOnFailureListener {
                // Noth..
            }
    }


    private fun choosePicture() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, 1)

    }

    private fun clearData() {
        description.text.clear()
        title.text.clear()
        imageView_2.setImageResource(android.R.color.transparent)
        imageView_1.setImageResource(android.R.color.transparent)
        numberImageLoaded = 0
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode== Activity.RESULT_OK && data!=null && data.data!=null ){

            if(numberImageLoaded == 0){
                imageUri = data.data!!
                imageView_1.setImageURI(imageUri)
                uploadPicture()
                numberImageLoaded++
            } else{
                imageUri = data.data!!
                imageView_2.setImageURI(imageUri)
                uploadPicture()
            }

        }

    }

    private fun uploadPicture() {

        val randomKey = UUID.randomUUID()
        val newRfe = storageReference.child("images/damaged/"+"damaged-"+
                FirebaseAuth.getInstance().currentUser.email+"-"+randomKey.toString());

        newRfe.putFile(imageUri)
            .addOnSuccessListener {

                Toast.makeText(
                    requireContext(),
                    "Image added",
                    Toast.LENGTH_LONG).show()

            }
            .addOnFailureListener {

                Toast.makeText(
                    requireContext(),
                    "Retry to add the image",
                    Toast.LENGTH_LONG).show()

            }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
    }

}