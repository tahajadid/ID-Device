package com.example.id_dev_fire.ui.singleDevice

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.ui.evs.EvsFragmentDirections
import com.example.id_dev_fire.ui.settings.SettingsViewModel
import com.google.firebase.firestore.FirebaseFirestore


class SingleDeviceFragment : Fragment() {

    private var mFirestore = FirebaseFirestore.getInstance()
    private val args by navArgs<SingleDeviceFragmentArgs>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_single_device, container, false)

        root.findViewById<TextView>(R.id.nameSingleDevice_tv).setText(args.nameDevice)


        mFirestore.collection("devices")
                .whereEqualTo("devName",args.nameDevice)
                .get().addOnCompleteListener {

                    if (it.isSuccessful){
                        for (result in it.result!!) {
                            val devInfo = result.toObject(Device::class.java)
                            root.findViewById<TextView>(R.id.supportedOsSingleDevice_tv).setText(devInfo.getdevSupportedOS())
                            root.findViewById<TextView>(R.id.featuresSingleDevice_tv).setText(devInfo.getdevFeatures())
                        }
                    }

                }.addOnFailureListener {

                }
        root.findViewById<Button>(R.id.orderDevice_button).setOnClickListener {

            val action = SingleDeviceFragmentDirections.actionNavSingleDeviceFragmentToOrderDeviceFragment()
            // take the id of the selected device
            it.findNavController().navigate(action)

        }

        return root
    }

}