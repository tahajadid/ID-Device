package com.example.id_dev_fire.ui.evs

import android.app.Dialog
import android.os.Bundle
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
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.ui.list.ListDeviceAdapter
import com.example.id_dev_fire.ui.mims.MimsViewModel
import com.google.firebase.firestore.FirebaseFirestore

class EvsFragment : Fragment() {

    private lateinit var evsViewModel: EvsViewModel
    lateinit var mProgressDialog: Dialog
    private var mFirestore = FirebaseFirestore.getInstance()
    private var AllData: MutableList<Device> = arrayListOf()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        evsViewModel =
                ViewModelProvider(this).get(EvsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_evs, container, false)

        // Recyclerview
        val adapter = ListDeviceAdapter()
        val recyclerView : RecyclerView = root.findViewById(R.id.recyclerview_evs_device)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        showProgressDialog()

        // Observe all the LiveData returned from the ViewModel
        evsViewModel.readAllData.observe(viewLifecycleOwner, Observer { device ->
            adapter.setDeviceData(device)
            hideProgressDialog()
        })

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