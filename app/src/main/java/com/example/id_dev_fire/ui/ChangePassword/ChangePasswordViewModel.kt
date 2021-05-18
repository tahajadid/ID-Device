package com.example.id_dev_fire.ui.ChangePassword

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.properties.Delegates

class ChangePasswordViewModel : ViewModel() {

    var returnValue by Delegates.notNull<Boolean>()

    fun checkInfo(oldPwd : String,newPwd : String) : Boolean {
        returnValue = false
        var userr = FirebaseAuth.getInstance().currentUser
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userr.email,oldPwd).
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userr.updatePassword(newPwd)
                returnValue = true
            } else {
                returnValue = false
            }
        }
        return returnValue
    }
}