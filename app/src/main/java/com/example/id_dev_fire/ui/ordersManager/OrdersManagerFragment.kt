package com.example.id_dev_fire.ui.ordersManager

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
import com.example.id_dev_fire.model.Order

class OrdersManagerFragment : Fragment() {


    private lateinit var ordersManagerViewModel: OrdersManagerViewModel
    lateinit var mProgressDialog: Dialog
    private lateinit var orderManagerList:ArrayList<Order>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        ordersManagerViewModel =
                ViewModelProvider(this).get(OrdersManagerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_orders_manager, container, false)

        // Recyclerview
        orderManagerList = ArrayList()
        val adapter = ListOrdersManagerAdapter(orderManagerList)
        val recyclerView : RecyclerView = root.findViewById(R.id.recyclerview_orders_manager)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        showProgressDialog()

        // Observe all the LiveData returned from the ViewModel
        ordersManagerViewModel.readAllData.observe(viewLifecycleOwner, Observer { order ->
            adapter.setOrderData(order)
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