package com.example.id_dev_fire.ui.AddCupboard

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.model.Cupboard

class AddCupboardFragment : Fragment() {

    lateinit var cupboardName : EditText

    lateinit var buttonCupboard : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_add_cupboard, container, false)

        cupboardName = root.findViewById(R.id.cupboardName_et)
        buttonCupboard = root.findViewById(R.id.addCupboard_button)

        buttonCupboard.setOnClickListener {
            addCupboard()
        }
        return root
    }

    private fun addCupboard() {

        if(checkInformationDetails()){

            val actualName = cupboardName.text.toString().trim(){ it <= ' ' }

            // We always pass anything at the id, cause we will set it as the id of document
            // When it will be created on mFirestoreClass
            val cup = Cupboard(
                actualName,
                actualName
            )

            FirestoreClass().addCupboardFirebase(this,cup)
            clearFields()
        }
    }

    private fun checkInformationDetails() : Boolean{

        return when{

            TextUtils.isEmpty(cupboardName.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                    this.context,
                    "please enter a Cupboard",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            else -> true
        }

    }

    private fun clearFields() {
        cupboardName.text.clear()
    }

}