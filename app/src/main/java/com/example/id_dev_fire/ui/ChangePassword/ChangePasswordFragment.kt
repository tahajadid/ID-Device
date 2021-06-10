package com.example.id_dev_fire.ui.ChangePassword

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.id_dev_fire.R
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordFragment : Fragment() {

    lateinit var oldPassword : EditText
    lateinit var newPassword : EditText
    lateinit var newPassword_2 : EditText

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_change_password, container, false)

        //
        oldPassword = root.findViewById(R.id.oldPassword)
        newPassword = root.findViewById(R.id.newPassword)
        newPassword_2 = root.findViewById(R.id.newPassword_2)

        val btn : Button = root.findViewById(R.id.change_btn)

        btn.setOnClickListener {

            if (checkBothPasswords(newPassword.text.toString(),newPassword_2.text.toString())){

                var userr = FirebaseAuth.getInstance().currentUser
                FirebaseAuth.getInstance().signInWithEmailAndPassword(userr.email,oldPassword.text.toString()).
                addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        userr.updatePassword(newPassword.text.toString())
                            .addOnCompleteListener { task->
                            Toast.makeText(
                            this.requireContext(),
                            "Your password was changed !!",
                            Toast.LENGTH_SHORT
                        ).show()
                        clearInputs()

                        val action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToNavSettings()
                        it.findNavController().navigate(action)
                        }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this.requireContext(),
                                    "Maybe you forgot the Old Password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {

                    }
                }
            }else{
                Toast.makeText(
                    this.requireContext(),
                    "The new passwords doesn't match",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        return root
    }

    private fun clearInputs() {
        oldPassword.text.clear()
        newPassword.text.clear()
        newPassword_2.text.clear()
    }

    fun checkBothPasswords(firstPwd : String, secondPws : String) : Boolean {
        return firstPwd.equals(secondPws)
    }

}