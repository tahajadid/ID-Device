package com.example.id_dev_fire.ui.singleDevice

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Order
import com.example.id_dev_fire.ui.orders.OrdersFragment
import com.google.firebase.firestore.FirebaseFirestore

class ListOrderedAdapter(var OrderList : ArrayList<Order>) : RecyclerView.Adapter<ListOrderedAdapter.MyViewHolder>() {

    private var mFirestore = FirebaseFirestore.getInstance()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_actual_order, parent, false))
    }

    override fun getItemCount(): Int {
        return OrderList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentOrder= OrderList[position]
        holder.itemView.findViewById<TextView>(R.id.orderedby_tv).setText(currentOrder.getorderfullNamecurrentOwner())
        holder.itemView.findViewById<TextView>(R.id.orderedbyfrom_tv).setText(
            currentOrder.getorderDateStart()!!.date.toString()+" - "+
                    (currentOrder.getorderDateStart()!!.month+1).toString()+" - "+
                    currentOrder.getorderDateStart()!!.year.toString()
        )

        holder.itemView.findViewById<TextView>(R.id.orderedbyto_tv).setText(
            currentOrder.getorderDateEnd()!!.date.toString()+" - "+
                    (currentOrder.getorderDateEnd()!!.month+1).toString()+" - "+
                    currentOrder.getorderDateEnd()!!.year.toString())


        holder.itemView.findViewById<LinearLayoutCompat>(R.id.rowOrderedLayout).setOnClickListener {

            val action = SingleDeviceFragmentDirections.actionNavSingleDeviceFragmentToEmployerInformationFragment(
                currentOrder.getorderCurrentOwner().toString())
            Log.d("tstts",currentOrder.getorderCurrentOwner().toString())
            holder.itemView.findNavController().navigate(action)

        }

    }

    fun setOrderData(order: ArrayList<Order>){
        OrderList = order
        notifyDataSetChanged()
    }


}