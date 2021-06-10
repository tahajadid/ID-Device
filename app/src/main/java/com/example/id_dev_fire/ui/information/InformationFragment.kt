package com.example.id_dev_fire.ui.information

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.id_dev_fire.BuildConfig
import com.example.id_dev_fire.R
import com.google.firebase.firestore.FirebaseFirestore

class InformationFragment : Fragment() {

    private val mFirestoreClass = FirebaseFirestore.getInstance()
    private var actualVersion = BuildConfig.VERSION_NAME

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_information, container, false)
        root.findViewById<TextView>(R.id.versionApplication).setText("version : "+ BuildConfig.VERSION_NAME)
        val lastVersion_tv = root.findViewById<TextView>(R.id.lastVersion_tv)
        val description_tv = root.findViewById<TextView>(R.id.descriptionApplication_tv)
        val linkDownload = root.findViewById<TextView>(R.id.linkdownload)


        mFirestoreClass.collection("information").document("my_app")
            .get().addOnCompleteListener {
                val version = it.result!!["version"]
                Log.d("testVersion",version.toString())
                if(version!!.toString().equals(actualVersion)){
                    // You have the last version
                    linkDownload.visibility = View.INVISIBLE
                    description_tv.setText("Your Application is Updated")
                    lastVersion_tv.setText("Last version : "+version.toString())

                }else{
                    // show link
                    linkDownload.setText(it.result!!["link"].toString())
                    description_tv.setText("Please follow the link bellow to Download the last version :")
                    lastVersion_tv.setText("Last version : "+version.toString())

                }
            }.addOnFailureListener {
                //
            }
        return root
    }


}
