package com.example.id_dev_fire.ui.ChangePassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.id_dev_fire.R
import com.example.id_dev_fire.ui.settings.SettingsViewModel

class ChangePasswordFragment : Fragment() {

    lateinit var changePasswordViewModel: ChangePasswordViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        changePasswordViewModel =
            ViewModelProvider(this).get(ChangePasswordViewModel::class.java)

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_change_password, container, false)

        val oldPassword : EditText = root.findViewById(R.id.oldPassword)
        val newPassword : EditText = root.findViewById(R.id.newPassword)
        val newPassword_2 : EditText = root.findViewById(R.id.newPassword_2)

        val btn : Button = root.findViewById(R.id.change_btn)

        btn.setOnClickListener {

            if (checkBothPasswords(newPassword.text.toString(),newPassword_2.text.toString())){

                if(changePasswordViewModel.checkInfo(oldPassword.text.toString(),newPassword.text.toString())){
                    Toast.makeText(
                        this.requireContext(),
                        "Password Changed !!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToNavSettings()
                    it.findNavController().navigate(action)
                }else{
                    Toast.makeText(
                        this.requireContext(),
                        "Maybe you forgot the Old Password",
                        Toast.LENGTH_SHORT
                    ).show() }
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

    fun checkBothPasswords(firstPwd : String, secondPws : String) : Boolean {
        return firstPwd.equals(secondPws)
    }

}