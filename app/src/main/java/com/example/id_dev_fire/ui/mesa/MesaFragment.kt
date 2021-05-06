package com.example.id_dev_fire.ui.mesa

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.ui.list.ListDeviceAdapter
import com.google.firebase.firestore.FirebaseFirestore

class MesaFragment : Fragment() {

    private lateinit var mesaViewModel: MesaViewModel
    lateinit var mProgressDialog: Dialog


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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main,menu)
        menu.findItem(R.id.app_bar_search).setVisible(true)
    }

}