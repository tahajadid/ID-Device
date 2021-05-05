package com.example.id_dev_fire

import android.app.Application
import android.content.Intent
import com.example.id_dev_fire.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class Home : Application() {

    override fun onCreate() {
        super.onCreate()

        val actualUser = FirebaseAuth.getInstance().currentUser

        if(actualUser != null){
            val intentMain = Intent(this, MainActivity::class.java)
            intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intentMain)

        }else{
            val intentLogin = Intent(this, LoginActivity::class.java)
            intentLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intentLogin)
        }
    }
}