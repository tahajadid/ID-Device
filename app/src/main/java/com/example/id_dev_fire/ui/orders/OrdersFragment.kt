package com.example.id_dev_fire.ui.orders

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
import com.example.id_dev_fire.model.Order
import com.google.firebase.firestore.FirebaseFirestore

class OrdersFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var ordersViewModel: OrdersViewModel
    lateinit var mProgressDialog: Dialog
    private lateinit var orderList:ArrayList<Order>
    private var mFirestore = FirebaseFirestore.getInstance()
    lateinit var thisRecyclerView : RecyclerView
    private var AllOrders: MutableList<Order> = arrayListOf()
    private var SearchOrders: MutableList<Order> = arrayListOf()
    lateinit var adapter: ListOrdersAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        ordersViewModel =
                ViewModelProvider(this).get(OrdersViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_orders, container, false)

        // Recyclerview
        orderList = ArrayList()
        adapter = ListOrdersAdapter(orderList)

        val recyclerView : RecyclerView = root.findViewById(R.id.recyclerview_orders_device)
        thisRecyclerView = recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        showProgressDialog()

        // Observe all the LiveData returned from the ViewModel

        ordersViewModel.readAllData.observe(viewLifecycleOwner, Observer { order ->
            adapter.setOrderData(order as ArrayList<Order>)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
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
        mFirestore.collection("orders")
            .orderBy("deviceName").startAt(newText).endAt(newText + "\uf8ff").addSnapshotListener { value, error ->
                SearchOrders = arrayListOf()
                for(a in value!!.toObjects(Order::class.java)){
                    // Log the list with the name of devices
                    SearchOrders.add(a)
                }
                AllOrders = SearchOrders
                adapter.setOrderData(AllOrders as ArrayList<Order>)
            }
        return true
    }

}