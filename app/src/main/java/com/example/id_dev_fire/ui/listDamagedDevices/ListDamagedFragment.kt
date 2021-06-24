package com.example.id_dev_fire.ui.listDamagedDevices

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
import com.example.id_dev_fire.model.DamagedDevice

class ListDamagedFragment : Fragment() {

    private lateinit var damagedViewModel: ListDamagedViewModel
    private lateinit var damagedList :ArrayList<DamagedDevice>
    lateinit var thisRecyclerView : RecyclerView
    lateinit var adapter: ListDamagedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        damagedViewModel =
            ViewModelProvider(this).get(ListDamagedViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_list_damaged, container, false)

        // Recyclerview
        damagedList = ArrayList()
        adapter = ListDamagedAdapter(damagedList)

        val recyclerView : RecyclerView = root.findViewById(R.id.list_damagedDevices)
        thisRecyclerView = recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        // Observe all the LiveData returned from the ViewModel

        damagedViewModel.readAllData.observe(viewLifecycleOwner, Observer { damaged ->
            adapter.setDamagedData(damaged as ArrayList<DamagedDevice>)

        })

        // Add menu
        setHasOptionsMenu(true)

        return root
    }

}