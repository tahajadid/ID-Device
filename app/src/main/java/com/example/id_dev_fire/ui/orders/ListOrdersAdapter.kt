package com.example.id_dev_fire.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Order

class ListOrdersAdapter : RecyclerView.Adapter<ListOrdersAdapter.MyViewHolder>()  {

    private lateinit var Orderid : String
    private var OrderList = emptyList<Order>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_order_list, parent, false))
    }

    override fun getItemCount(): Int {
        return OrderList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentOrder= OrderList[position]
        holder.itemView.findViewById<TextView>(R.id.nameDeviceOfOrder_tv).setText(currentOrder.getorderDeviceName())
        holder.itemView.findViewById<TextView>(R.id.ownerDeviceOfOrder_tv).setText(currentOrder.getorderDeviceOwner())
        holder.itemView.findViewById<TextView>(R.id.stateOrder_tv).setText(currentOrder.getorderDecision())

    }

    fun setOrderData(order: List<Order>){
        this.OrderList = order
        notifyDataSetChanged()
    }

}