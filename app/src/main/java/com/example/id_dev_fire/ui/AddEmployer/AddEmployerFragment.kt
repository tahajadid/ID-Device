package com.example.id_dev_fire.ui.AddEmployer

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.model.Employer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AddEmployerFragment : Fragment() {

    lateinit var employerEmail : EditText
    lateinit var employerPassword : EditText
    lateinit var employerPassword_2 : EditText
    lateinit var firstName : EditText
    lateinit var lastName : EditText
    lateinit var phone : EditText

    lateinit var genderSpinner: Spinner
    lateinit var roleSpinner: Spinner
    lateinit var tribSpinner: Spinner

    lateinit var genderSelected : String
    lateinit var roleSelected : String
    lateinit var tribSelected : String

    lateinit var btn_addEmployer : Button
    lateinit var mProgressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_add_employer, container, false)

        employerEmail = root.findViewById(R.id.employerEmail_et)
        employerPassword = root.findViewById(R.id.employerPassword_et)
        employerPassword_2 = root.findViewById(R.id.employerPassword2_et)
        firstName = root.findViewById(R.id.firstName_et)
        lastName = root.findViewById(R.id.lastName_et)
        phone = root.findViewById(R.id.employerPhone_et)

        genderSpinner = root.findViewById(R.id.gender_spinner)
        roleSpinner = root.findViewById(R.id.role_spinner)
        btn_addEmployer = root.findViewById(R.id.addEmployer_button)

        // Get the gender selected
        genderSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val text: String = p0?.getItemAtPosition(p2).toString()
                genderSelected = text
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // Get the role selected
        roleSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val text: String = p0?.getItemAtPosition(p2).toString()
                roleSelected = text
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // Get the project selected
        tribSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val text: String = p0?.getItemAtPosition(p2).toString()
                tribSelected = text
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // Click on Add Employer Button
        btn_addEmployer.setOnClickListener{
            addEmployer()
        }
        return root
    }

    private fun addEmployer() {

        if(checkInformationDetails() && checkPassword()) {

            val actualEmail = employerEmail.text.toString().trim(){ it <= ' ' }
            val actualPwd = employerPassword.text.toString().trim(){ it <= ' ' }
            val actualFirstName = firstName.text.toString().trim(){ it <= ' ' }
            val actualLastName = lastName.text.toString().trim(){ it <= ' ' }
            val actualphone = phone.text.toString().trim(){ it <= ' ' }

            showProgressDialog()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(actualEmail,actualPwd).
            addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val firebaseUser : FirebaseUser = task.result!!.user!!

                    // Set an Employer Object
                    val employer = Employer(
                            firebaseUser.uid,
                            actualFirstName,
                            actualLastName,
                            actualEmail,
                            actualphone.toLong(),
                            this.genderSelected,
                            this.roleSelected,
                            this.tribSelected
                    )

                    // Get Firebase instance and put the Employer Object to add it
                    FirestoreClass().addEmployerFirebase(this,employer)
                    hideProgressDialog()
                    // Reset values and set it empty
                    resetValues()

                } else {
                    hideProgressDialog()
                    Toast.makeText(
                            this.context,
                            "Please Try Again :( ",
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkInformationDetails() : Boolean{

        return when{

            TextUtils.isEmpty(employerEmail.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                        this.context,
                        "please enter an Email",
                        Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(employerPassword.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                        this.context,
                        "please enter a password",
                        Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(employerPassword_2.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                        this.context,
                        "please enter second field of the password",
                        Toast.LENGTH_SHORT
                ).show()
                false
            }

            else -> true
        }

    }

    private fun checkPassword() : Boolean {
        if (employerPassword.text.toString().equals(employerPassword_2.text.toString())) return true
        else {
            Toast.makeText(
                    this.context,
                    "Not the same password !",
                    Toast.LENGTH_SHORT
            ).show()
            return false
        }
    }

    private fun resetValues() {
        firstName.text.clear()
        lastName.text.clear()
        employerEmail.text.clear()
        employerPassword.text.clear()
        employerPassword_2.text.clear()
        phone.text.clear()
    }

    private fun showProgressDialog(){
        mProgressDialog = Dialog(this.requireContext())
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    private fun hideProgressDialog() {
        mProgressDialog.hide()
    }
}