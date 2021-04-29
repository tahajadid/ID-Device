package com.example.id_dev_fire.ui.login

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.id_dev_fire.MainActivity
import com.example.id_dev_fire.R
import com.example.id_dev_fire.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var email : EditText
    lateinit var pwd : EditText
    lateinit var forgetPassword : TextView
    lateinit var loginButton : Button
    lateinit var mProgressDialog: Dialog
    lateinit var signup : TextView

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
            finish()
        }


        loginButton.setOnClickListener{

            when{
                TextUtils.isEmpty(email.text.toString().trim(){ it <= ' ' }) -> {
                    Toast.makeText(
                            this,
                            "please enter your User Name",
                            Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    showProgressDialog()

                    val actuaUser = email.text.toString().trim(){ it <= ' ' }
                    val actuaPwd = pwd.text.toString().trim(){ it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(actuaUser,actuaPwd).
                    addOnCompleteListener { task ->

                        hideProgressDialog()
                        if (task.isSuccessful) {
                            Toast.makeText(
                                    this,
                                    "Login Succefully  !! ",
                                    Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                    this,
                                    "Please Try Again :) ",
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
            setMessage("Please Contact the Administrator : karim.karim@idemia.com")
            setPositiveButton("Ok"){ _, _ ->
                Toast.makeText(this@LoginActivity, "Thank's", Toast.LENGTH_SHORT).show()
            }
        }.create().show()
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