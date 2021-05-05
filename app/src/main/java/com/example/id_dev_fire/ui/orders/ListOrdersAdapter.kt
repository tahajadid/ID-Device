package com.example.id_dev_fire.ui.orders

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Order
import com.google.firebase.firestore.FirebaseFirestore

class ListOrdersAdapter : RecyclerView.Adapter<ListOrdersAdapter.MyViewHolder>()  {

    private var mFirestore = FirebaseFirestore.getInstance()
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

        // Change the color of the staate of the Order
        if(currentOrder.getorderDecision().equals("On Hold")) {
            holder.itemView.findViewById<TextView>(R.id.stateOrder_tv).setTextColor(R.color.design_default_color_error)
        }else if(currentOrder.getorderDecision().equals("Accepted")){
            holder.itemView.findViewById<TextView>(R.id.stateOrder_tv).setTextColor(R.color.white)
        }
        else{
            holder.itemView.findViewById<TextView>(R.id.stateOrder_tv).setTextColor(R.color.white)
        }

        // When you click on delete Image
        holder.itemView.findViewById<LinearLayoutCompat>(R.id.rowOrderLayout)
                .findViewById<ImageView>(R.id.imageView_device).setOnClickListener {
                deletOrder(currentOrder,it.findFragment(),position)

        }
    }

    fun deletOrder(order : Order,fragment: OrdersFragment, position : Int) {

        val builder = AlertDialog.Builder(fragment.requireContext())
        builder.setPositiveButton("Yes") { _, _ ->

            val orderRef = mFirestore.collection("orders").document(order.getorderId().toString())

            orderRef.delete().addOnCompleteListener {
                        Toast.makeText(
                                fragment.requireContext(),
                                "Successfully removed",
                                Toast.LENGTH_SHORT).show()
                /*
                 TODO
                 refresh the list of orders after deleting an item
                * */
                        OrderList.drop(position)
                        notifyItemRemoved(position)

                    }.addOnFailureListener {

                    }

        }
        builder.setNegativeButton("No") { _, _ -> }

        builder.setTitle("Delete Order")
        builder.setMessage("Are you sure you want to delete order of ${order.getorderDeviceName()}?")
        builder.create().show()

    }

    fun setOrderData(order: List<Order>){
        this.OrderList = order
        notifyDataSetChanged()
    }

}