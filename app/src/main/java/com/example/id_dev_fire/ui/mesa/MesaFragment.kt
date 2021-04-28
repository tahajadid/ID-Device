package com.example.id_dev_fire.ui.mesa

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Cupboard
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.ui.list.ListDeviceAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

class MesaFragment : Fragment() {

    private lateinit var mesaViewModel: MesaViewModel
    lateinit var mProgressDialog: Dialog
    private var mFirestore = FirebaseFirestore.getInstance()
    private var AllData: MutableList<Device> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mesaViewModel =
            ViewModelProvider(this).get(MesaViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mesa, container, false)

        // Recyclerview
        val adapter = ListDeviceAdapter()
        val recyclerView : RecyclerView = root.findViewById(R.id.recyclerview_mesa_device)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        showProgressDialog()
        // DeviceViewModel
        mesaViewModel = ViewModelProvider(this).get(MesaViewModel::class.java)

        // Observe all the LiveData returned from the ViewModel
        mesaViewModel.readAllData.observe(viewLifecycleOwner, Observer { device ->
            adapter.setDeviceData(device)
            hideProgressDialog()
        })

        /*
        showProgressDialog()
        mFirestore.collection("devices")
                .get().addOnCompleteListener {
                    hideProgressDialog()
                    if (it.isSuccessful){
                        for (resul in it.result!!) {
                            val dev = resul.toObject(Device::class.java)
                            AllData.add(dev)
                        }
                        adapter.setDeviceData(AllData)
                    }

                }.addOnFailureListener {
                    hideProgressDialog()
                    Log.d("taag","There is an Error} --")
                }

         */

        return root
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