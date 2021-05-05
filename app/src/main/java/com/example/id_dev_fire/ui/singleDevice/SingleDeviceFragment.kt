package com.example.id_dev_fire.ui.singleDevice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.id_dev_fire.R
import com.google.firebase.firestore.FirebaseFirestore


class SingleDeviceFragment : Fragment() {

    private val args by navArgs<SingleDeviceFragmentArgs>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_single_device, container, false)

        root.findViewById<TextView>(R.id.nameSingleDevice_tv).setText(args.deviceSelected.getdevName())
        root.findViewById<TextView>(R.id.supportedOsSingleDevice_tv).setText(args.deviceSelected.getdevSupportedOS())
        root.findViewById<TextView>(R.id.featuresSingleDevice_tv).setText(args.deviceSelected.getdevFeatures())
        root.findViewById<TextView>(R.id.serviceName_tv).setText(args.deviceSelected.getdevServiceName())

        root.findViewById<Button>(R.id.orderDevice_button).setOnClickListener {

            val action = SingleDeviceFragmentDirections
                    .actionNavSingleDeviceFragmentToOrderDeviceFragment(
                            args.deviceSelected.getdevName().toString())
            it.findNavController().navigate(action)

        }

        return root
    }

}