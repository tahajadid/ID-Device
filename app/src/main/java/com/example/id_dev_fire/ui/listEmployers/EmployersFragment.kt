package com.example.id_dev_fire.ui.listEmployers

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Employer
import com.google.firebase.firestore.FirebaseFirestore


class EmployersFragment : Fragment() {

    private lateinit var employerViewModel: EmployersViewModel
    lateinit var mProgressDialog: Dialog
    private lateinit var employerList:ArrayList<Employer>
    lateinit var thisRecyclerView : RecyclerView
    lateinit var adapter: EmployersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        employerViewModel =
            ViewModelProvider(this).get(EmployersViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_employers, container, false)

        // Recyclerview
        employerList = ArrayList()
        adapter = EmployersAdapter(employerList)

        val recyclerView : RecyclerView = root.findViewById(R.id.list_employers)
        thisRecyclerView = recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        showProgressDialog()

        // Observe all the LiveData returned from the ViewModel

        employerViewModel.readAllData.observe(viewLifecycleOwner, Observer { employer ->
            adapter.setEmployerData(employer as ArrayList<Employer>)
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

}