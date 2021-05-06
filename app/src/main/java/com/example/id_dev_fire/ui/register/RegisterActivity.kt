package com.example.id_dev_fire.ui.register

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.example.id_dev_fire.MainActivity
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.model.Employer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        employerEmail = findViewById(R.id.registerEmail_et)
        employerPassword = findViewById(R.id.registerPassword_et)
        employerPassword_2 = findViewById(R.id.registerPassword2_et)
        firstName = findViewById(R.id.registerFirstName_et)
        lastName = findViewById(R.id.registerLastName_et)
        phone = findViewById(R.id.employerPhoneRegister_et)

        genderSpinner = findViewById(R.id.registerGender_spinner)
        roleSpinner = findViewById(R.id.registerRole_spinner)
        tribSpinner = findViewById(R.id.tribRegister_spinner)
        btn_addEmployer = findViewById(R.id.buttonRegister)

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

        // Get the role selected
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
            registerEmployer()
        }

    }

    private fun registerEmployer() {

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
                    // Set an Employer Object
                    val employer = Employer(
                            FirebaseAuth.getInstance().currentUser.uid,
                            actualFirstName,
                            actualLastName,
                            actualEmail,
                            actualphone.toLong(),
                            this.genderSelected,
                            this.roleSelected,
                            this.tribSelected
                    )

                    // Get Firebase instance and put the Employer Object to add it
                    FirestoreClass().addEmployerActivityFirebase(this,employer)
                    hideProgressDialog()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    hideProgressDialog()
                    Toast.makeText(
                            this,
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
                        this,
                        "please enter an Email",
                        Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(employerPassword.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                        this,
                        "please enter a password",
                        Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(employerPassword_2.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                        this,
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
                    this,
                    "set the same password in the two fields",
                    Toast.LENGTH_SHORT
            ).show()
            return false
        }
    }

    private fun showProgressDialog(){
        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    private fun hideProgressDialog() {
        mProgressDialog.hide()
    }
}