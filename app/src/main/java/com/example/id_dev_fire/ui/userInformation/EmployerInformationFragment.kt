package com.example.id_dev_fire.ui.userInformation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Employer
import com.google.firebase.firestore.FirebaseFirestore


class EmployerInformationFragment : Fragment() {

    private val args by navArgs<EmployerInformationFragmentArgs>()
    private var mFirestoreClass = FirebaseFirestore.getInstance()

    @ExperimentalStdlibApi
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_employer_information, container, false)

        mFirestoreClass.collection("employers")
            .document(args.idEmployer).get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val thisEmployer = it.result!!.toObject(Employer::class.java)
                    if(thisEmployer != null){
                        root.findViewById<TextView>(R.id.nameUser_tv).setText(
                            thisEmployer.getEmployerFirstName().uppercase()
                                    +" "+thisEmployer.getEmployerLastName().uppercase())
                        root.findViewById<TextView>(R.id.roleUser_tv).setText(
                            thisEmployer.getEmployerRole())
                        root.findViewById<TextView>(R.id.phoneUser_tv).setText(
                            "+212"+thisEmployer.getEmployerPhone())
                        root.findViewById<TextView>(R.id.emailUser_tv).setText(
                            thisEmployer.getEmployerEmail())
                        root.findViewById<TextView>(R.id.projectUser_tv).setText(
                            thisEmployer.getEmployerProject())
                    }else{

                    }
                }else{

                }
            }.addOnFailureListener {

            }

        // Call the Employer
        root.findViewById<ImageView>(R.id.callUser_iv).setOnClickListener {
            val phone = root.findViewById<TextView>(R.id.phoneUser_tv).text
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:"+phone)
            startActivity(callIntent)
        }

        // Send Mail to the Employer
        root.findViewById<ImageView>(R.id.sendEmail_iv).setOnClickListener {

            val mailIntent = Intent(Intent.ACTION_SENDTO,Uri.fromParts(
                "mailto",
                root.findViewById<TextView>(R.id.emailUser_tv).text.toString(),
                null))

            mailIntent.putExtra(Intent.EXTRA_SUBJECT, " ")
            mailIntent.putExtra(Intent.EXTRA_TEXT, " ")
            startActivity(mailIntent)

        }


        return root
    }

}