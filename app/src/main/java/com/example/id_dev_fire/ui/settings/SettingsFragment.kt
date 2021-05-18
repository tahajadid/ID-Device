package com.example.id_dev_fire.ui.settings

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.provider.ContactsContract
import android.text.InputType
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.ui.mims.MimsFragmentDirections
import com.example.id_dev_fire.ui.orders.OrdersViewModel
import com.google.firebase.firestore.FirebaseFirestore


class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    lateinit var mProgressDialog: Dialog
    lateinit var idEmployer : String
    lateinit var newPhone : EditText

    private lateinit var mFireStoreClass : FirestoreClass

    @ExperimentalStdlibApi
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        showProgressDialog()
        settingsViewModel =
                ViewModelProvider(this).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)

        val emailView : TextView = root.findViewById(R.id.email_setting)
        val phoneView : TextView = root.findViewById(R.id.phone_setting)
        val namaView : TextView = root.findViewById(R.id.nameSetting_tv)
        val roleView : TextView = root.findViewById(R.id.roleSetting_tv)
        val genderView : TextView = root.findViewById(R.id.genderSetting_tv)
        val projectView : TextView = root.findViewById(R.id.projectSetting_tv)

        val editPhone : ImageView = root.findViewById(R.id.editPhone_iv)
        val editPwd : TextView = root.findViewById(R.id.changePwd_label)


        settingsViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            namaView.text = it[0].getEmployerFirstName().uppercase() + " " +
                    it[0].getEmployerLastName().uppercase()
            roleView.text = it[0].getEmployerRole()
            emailView.text = it[0].getEmployerEmail()
            phoneView.text = "+212 " + it[0].getEmployerPhone().toString()
            genderView.text = it[0].getEmployerGender()
            projectView.text = it[0].getEmployerProject()
            idEmployer = it[0].getEmployerId()
            hideProgressDialog()

            editPhone.setOnClickListener{
                editNumberPhone(idEmployer)
            }

            editPwd.setOnClickListener{
                val action = SettingsFragmentDirections.actionNavSettingsToChangePasswordFragment()
                it.findNavController().navigate(action)
            }

        })
        return root
    }

    fun editNumberPhone(id : String){
        val builder = AlertDialog.Builder(this.requireContext())
        newPhone = EditText(this.requireContext())
        newPhone.inputType = InputType.TYPE_CLASS_PHONE
        builder.setView(newPhone)

        builder.setPositiveButton("Yes") { _, _ ->
            mFireStoreClass.editPhoneEmployerFirebase(this,"e","e")

        }
        builder.setNegativeButton("No") { _, _ -> }

        builder.setTitle("Change Number Phone")
        builder.create().show()
    }


    private fun showProgressDialog(){
        mProgressDialog = Dialog(this.requireContext())

        mProgressDialog.setContentView(R.layout.dialog_progress_data)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }

    private fun hideProgressDialog() {
        mProgressDialog.hide()
    }
}