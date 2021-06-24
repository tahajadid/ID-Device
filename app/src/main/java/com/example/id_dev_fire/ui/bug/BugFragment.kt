package com.example.id_dev_fire.ui.bug

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.model.Bug
import com.example.id_dev_fire.model.Employer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import java.util.UUID.randomUUID

class BugFragment : Fragment() {

    lateinit var descriptionBug : EditText
    lateinit var btnAddBug : Button
    lateinit var grp_radio : RadioGroup
    lateinit var uploadImgae : ImageView

    lateinit var imageUri : Uri
    lateinit var imageToShow : ImageView
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference

    var mFireAuth = FirebaseAuth.getInstance()

    // Default value
    var reasonSelected : String = "Not Selected"

    // Default name of BugOwner
    var fullNameOwner : String = "Anonyme"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_bug, container, false)

        grp_radio = root.findViewById(R.id.group_radioButton)
        btnAddBug = root.findViewById(R.id.addBug_btn)
        descriptionBug = root.findViewById(R.id.descriptionBug_et)
        uploadImgae = root.findViewById(R.id.uploadImage_iv)
        imageToShow = root.findViewById(R.id.imageUploaded_iv)

        // Storage
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        grp_radio.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{
            group, checkedId ->
            val radio : RadioButton = root.findViewById(checkedId)
            reasonSelected = radio.text.toString()
        })

        uploadImgae.setOnClickListener {
            choosePicture()
        }

        FirebaseFirestore.getInstance().collection("employers")
            .document(FirebaseAuth.getInstance().currentUser.uid).get()
            .addOnCompleteListener {

                if(it.isSuccessful){
                    val actualEmployer = it.result!!.toObject(Employer::class.java)
                    fullNameOwner = actualEmployer!!.getEmployerFirstName() + " " +actualEmployer!!.getEmployerLastName()
                }else {

                }
            }

        btnAddBug.setOnClickListener {
            val actualDescription = descriptionBug.text.toString().trim(){ it <= ' ' }

            // We always pass anything at the id, cause we will set it as the id of document
            // When it will be created on mFirestoreClass

            val bug = Bug(fullNameOwner,fullNameOwner,reasonSelected,actualDescription)
            val mFirestoreClass = FirestoreClass()
            mFirestoreClass.addBugFirebase(this,bug)
            clearData()
        }

        return root
    }

    private fun choosePicture() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, 1)

    }

    private fun clearData() {
        descriptionBug.text.clear()
        imageToShow.setImageResource(android.R.color.transparent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode==RESULT_OK && data!=null && data.data!=null ){
            imageUri = data.data!!
            imageToShow.setImageURI(imageUri)
            uploadPicture()
        }

    }

    private fun uploadPicture() {

        val randomKey = randomUUID()
        val newRfe = storageReference.child("images/bug/"+"bug-"+
                mFireAuth.currentUser.email+"-"+randomKey.toString());

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