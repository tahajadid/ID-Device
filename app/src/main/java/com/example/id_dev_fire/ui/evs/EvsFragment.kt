package com.example.id_dev_fire.ui.evs

import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.util.Log
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

class EvsFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var evsViewModel: EvsViewModel
    lateinit var mProgressDialog: Dialog
    private var AllDevices: MutableList<Device> = arrayListOf()
    private var SearchDevices: MutableList<Device> = arrayListOf()
    private var mFirestore = FirebaseFirestore.getInstance()
    lateinit var thisRecyclerView : RecyclerView

    // Recyclerview
    var adapter = ListDeviceAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        evsViewModel =
                ViewModelProvider(this).get(EvsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_evs, container, false)

        view?.setBackgroundResource(R.drawable.inside_layers)
        val recyclerView : RecyclerView = root.findViewById(R.id.recyclerview_evs_device)
        thisRecyclerView = recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        showProgressDialog()

        // Observe all the LiveData returned from the ViewModel
        evsViewModel.readAllData.observe(viewLifecycleOwner, { device ->
            adapter.setDeviceData(device)
            hideProgressDialog()
        })

        // Add menu
        setHasOptionsMenu(true)

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

    override fun onCreateOptionsMenu(menu: Menu,inflater: MenuInflater) {
        inflater.inflate(R.menu.main,menu)

        val item = menu?.findItem(R.id.app_bar_search)
        val searchView : androidx.appcompat.widget.SearchView = item?.actionView as androidx.appcompat.widget.SearchView

        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        mFirestore.collection("devices")
            .orderBy("devName").startAt(newText).endAt(newText + "\uf8ff").addSnapshotListener { value, error ->
                SearchDevices = arrayListOf()
                for(a in value!!.toObjects(Device::class.java)){
                    // Log the list with the name of devices
                    SearchDevices.add(a)
                }
                AllDevices = SearchDevices
                adapter.setDeviceData(AllDevices)
            }
        return true
    }

}