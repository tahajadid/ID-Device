package com.example.id_dev_fire.ui.login

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.id_dev_fire.MainActivity
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.model.Employer
import com.example.id_dev_fire.model.TokenDevice
import com.example.id_dev_fire.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {

    lateinit var email : EditText
    lateinit var pwd : EditText
    lateinit var forgetPassword : TextView
    lateinit var loginButton : Button
    lateinit var mProgressDialog: Dialog
    lateinit var signup : TextView
    val actualUser = FirebaseAuth.getInstance().currentUser

    override fun onStart() {
        super.onStart()
        showProgressDialog()

        if(actualUser != null){

            FirebaseFirestore.getInstance().collection("employers")
                .document(FirebaseAuth.getInstance().currentUser.uid).get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        hideProgressDialog()
                        val actualEmployer = it.result!!.toObject(Employer::class.java)

                        if (actualEmployer != null) {
                            val flagAccess = actualEmployer!!.getEmployerFlagAccess()
                            if(flagAccess!!){
                                val intentMain = Intent(this, MainActivity::class.java)
                                intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intentMain)
                                finish()
                            }else {
                                hideProgressDialog()
                                Toast.makeText(
                                        this,
                                "You didn't get access from the admin",
                                Toast.LENGTH_SHORT
                                ).show()
                            }
                            }else{
                            hideProgressDialog()
                            Toast.makeText(
                                this,
                                "User doesn't exist, Please try again !",
                                Toast.LENGTH_SHORT
                            ).show()
                            }
                    }else{
                        hideProgressDialog()
                        Toast.makeText(
                            this,
                            "Please try again !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                 }

        }else{
            hideProgressDialog()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.buttonLogin)
        email = findViewById(R.id.userEmail)
        pwd = findViewById(R.id.userPassword)
        forgetPassword = findViewById(R.id.forgetPassword_label)
        signup = findViewById(R.id.signup_label)

        forgetPassword.setOnClickListener {
            forgetPasswordResponse()
        }

        signup.setOnClickListener {
            val intentregister = Intent(this, RegisterActivity::class.java)
            startActivity(intentregister)
            finishAffinity()
        }

        loginButton.setOnClickListener{
            when{
                TextUtils.isEmpty(email.text.toString().trim(){ it <= ' ' }) -> {
                    Toast.makeText(
                            this,
                            "please enter your Email adress",
                            Toast.LENGTH_SHORT
                    ).show()

                }
                else -> {
                    showProgressDialog()

                    val actuaUser = email.text.toString().trim(){ it <= ' ' }
                    val actuaPwd = pwd.text.toString().trim(){ it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(actuaUser,actuaPwd).
                    addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            FirebaseFirestore.getInstance().collection("employers")
                                .document(FirebaseAuth.getInstance().currentUser.uid).get()
                                .addOnCompleteListener {
                                    if(it.isSuccessful){
                                        hideProgressDialog()
                                        val actualEmployer = it.result!!.toObject(Employer::class.java)
                                        val flagAccess = actualEmployer!!.getEmployerFlagAccess()
                                        if(flagAccess){
                                            retrieveAndStoreToken(FirebaseAuth.getInstance().currentUser.uid)
                                            Toast.makeText(
                                                this,
                                                "Welcome to ID-Device",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                            finishAffinity()
                                        }else{
                                            hideProgressDialog()
                                            Toast.makeText(
                                                this,
                                                "You didn't get access from the admin",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }else{
                                        hideProgressDialog()
                                        Toast.makeText(
                                            this,
                                            "User doesn't exist, Please try again !",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }.addOnFailureListener {
                                }
                        } else {
                            hideProgressDialog()
                            Toast.makeText(
                                    this,
                                    "The Email or Password is wrong",
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

        }
    }

    private fun forgetPasswordResponse() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setIcon(R.drawable.ic_menu_share)
            setTitle("Hello !")
            setMessage("Please Contact the Administrator : admin.maroc@idemia.com")
            setPositiveButton("Ok"){ _, _ ->
                Toast.makeText(this@LoginActivity, "Thank's", Toast.LENGTH_SHORT).show()
            }
        }.create().show()
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