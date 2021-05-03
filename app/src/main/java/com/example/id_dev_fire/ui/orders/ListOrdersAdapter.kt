package com.example.id_dev_fire.ui.orders

import android.annotation.SuppressLint
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

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentOrder= OrderList[position]
        holder.itemView.findViewById<TextView>(R.id.nameDeviceOfOrder_tv).setText(currentOrder.getorderDeviceName())
        holder.itemView.findViewById<TextView>(R.id.ownerDeviceOfOrder_tv).setText(currentOrder.getorderDeviceOwner())
        holder.itemView.findViewById<TextView>(R.id.stateOrder_tv).setText(currentOrder.getorderDecision())

        if(currentOrder.getorderDecision().equals("On Hold")) {
            holder.itemView.findViewById<TextView>(R.id.stateOrder_tv).setTextColor(R.color.design_default_color_error)
        }else if(currentOrder.getorderDecision().equals("Accepted")){
            holder.itemView.findViewById<TextView>(R.id.stateOrder_tv).setTextColor(R.color.white)
        }
        else{
            holder.itemView.findViewById<TextView>(R.id.stateOrder_tv).setTextColor(R.color.white)
        }

    }

    fun setOrderData(order: List<Order>){
        this.OrderList = order
        notifyDataSetChanged()
    }

}