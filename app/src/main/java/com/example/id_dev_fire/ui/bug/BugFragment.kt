package com.example.id_dev_fire.ui.bug

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

class BugFragment : Fragment() {

    lateinit var descriptionBug : EditText
    lateinit var btnAddBug : Button
    lateinit var grp_radio : RadioGroup

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


        grp_radio.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{
            group, checkedId ->
            val radio : RadioButton = root.findViewById(checkedId)
            reasonSelected = radio.text.toString()
        })

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

    private fun clearData() {
        descriptionBug.text.clear()
    }

}