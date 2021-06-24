package com.example.id_dev_fire.ui.register

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.model.Employer
import com.example.id_dev_fire.model.TokenDevice
import com.example.id_dev_fire.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

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

        // TextView
        employerEmail = findViewById(R.id.registerEmail_et)
        employerPassword = findViewById(R.id.registerPassword_et)
        employerPassword_2 = findViewById(R.id.registerPassword2_et)
        firstName = findViewById(R.id.registerFirstName_et)
        lastName = findViewById(R.id.registerLastName_et)
        phone = findViewById(R.id.employerPhoneRegister_et)

        // Spinner
        genderSpinner = findViewById(R.id.registerGender_spinner)
        roleSpinner = findViewById(R.id.registerRole_spinner)
        tribSpinner = findViewById(R.id.tribRegister_spinner)

        // Button
        btn_addEmployer = findViewById(R.id.buttonRegister)

        // Get the gender selected
        genderSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val text: String = p0?.getItemAtPosition(p2).toString()
                genderSelected = text
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Noth..
            }
        }

        // Get the role selected
        roleSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val value : String = p0?.getItemAtPosition(p2).toString()
                roleSelected = value
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Noth..
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
                // Noth..
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

            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(actualEmail).addOnCompleteListener {
                if(it.isSuccessful){
                    if(it.result!!.signInMethods.isEmpty()){
                        // the Email adr doesn't exist
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(actualEmail,actualPwd).
                        addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                retrieveAndStoreToken(FirebaseAuth.getInstance().currentUser.uid)
                                // Set an Employer Object
                                val employer = Employer(
                                    FirebaseAuth.getInstance().currentUser.uid,
                                    actualFirstName,
                                    actualLastName,
                                    actualEmail,
                                    actualphone.toLong(),
                                    this.genderSelected,
                                    this.roleSelected,
                                    this.tribSelected,
                                    false
                                )

                                // Get Firebase instance and put the Employer Object to add it
                                FirestoreClass().addEmployerActivityFirebase(this,employer)
                                hideProgressDialog()

                                val intent = Intent(this, LoginActivity::class.java)
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

                    }else{
                        hideProgressDialog()
                        Toast.makeText(this,
                            "This Email seems already used !",
                            Toast.LENGTH_SHORT).show()
                    }


                }
            }.addOnFailureListener {

            }


        }

    }

    private fun checkInformationDetails() : Boolean{

        return when{

            TextUtils.isEmpty(employerEmail.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                    this,
                    "please enter your E-mail adress",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(firstName.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                        this,
                        "please enter your First name",
                        Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(lastName.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                    this,
                    "please enter your Last name",
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

            TextUtils.isEmpty(phone.text.toString().trim(){ it <= ' ' }) -> {
                Toast.makeText(
                    this,
                    "please enter your Phone number",
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

    private fun retrieveAndStoreToken(uid : String) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener{
                if (it.isSuccessful){
                    val token : String? = it.result
                    val tokenDevice = TokenDevice(
                        uid,
                        token.toString()
                    )
                    // We add the token & if the userId exists we just update it
                    FirestoreClass().addTokenFirebase(tokenDevice)
                }
            }.addOnFailureListener {
                // Noth..
            }
    }

}