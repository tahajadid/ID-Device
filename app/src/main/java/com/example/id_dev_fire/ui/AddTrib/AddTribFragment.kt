package com.example.id_dev_fire.ui.AddTrib

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.model.Trib


class AddTribFragment : Fragment() {

    lateinit var tribName : EditText

    lateinit var buttonAddTrib : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_add_trib, container, false)

        tribName = root.findViewById(R.id.tribName_et)
        buttonAddTrib = root.findViewById(R.id.addTrib_button)

        buttonAddTrib.setOnClickListener {
            addTrib()
        }

        return root
    }

    private fun addTrib() {

        if(checkInformationDetails()){

            val actualName = tribName.text.toString().trim(){ it <= ' ' }

            // We always pass anything at the id, cause we will set it as the id of document
            // When it will be created on mFirestoreClass
            val tri = Trib(
                actualName,
                actualName,
                "said said"
            )

            FirestoreClass().addTrib(this,tri)
            clearFields()
        }
    }

    private fun checkInformationDetails() : Boolean{

        return when{

            TextUtils.isEmpty(tribName.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                    this.context,
                    "please enter a name of the Trib",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            else -> true
        }

    }

    private fun clearFields() {
        tribName.text.clear()
    }

}