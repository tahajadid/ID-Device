package com.example.id_dev_fire.ui.ordersManager

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

class ListOrdersManagerAdapter : RecyclerView.Adapter<ListOrdersManagerAdapter.MyViewHolder>() {

    private var mFirestore = FirebaseFirestore.getInstance()
    private var OrderList = emptyList<Order>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_order_manager_list, parent, false))
    }

    override fun getItemCount(): Int {
        return OrderList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentOrder= OrderList[position]
        holder.itemView.findViewById<TextView>(R.id.nameDeviceOfOrderManager_tv).setText(currentOrder.getorderDeviceName())
        holder.itemView.findViewById<TextView>(R.id.personOrderedOfOrderManager_tv).setText(currentOrder.getorderDeviceOwner())
        holder.itemView.findViewById<TextView>(R.id.stateOfOrderManager_tv).setText(currentOrder.getorderDecision())

        // Change the color of the staate of the Order
        if(currentOrder.getorderDecision().equals("On Hold")) {
            holder.itemView.findViewById<TextView>(R.id.stateOfOrderManager_tv).setTextColor(R.color.design_default_color_error)
        }else if(currentOrder.getorderDecision().equals("Accepted")){
            holder.itemView.findViewById<TextView>(R.id.stateOfOrderManager_tv).setTextColor(R.color.white)
        }
        else{
            holder.itemView.findViewById<TextView>(R.id.stateOfOrderManager_tv).setTextColor(R.color.white)
        }

        holder.itemView.findViewById<LinearLayoutCompat>(R.id.rowOrderManagerLayout)
                .findViewById<ImageView>(R.id.informationOrder_iv).setOnClickListener {
                    setDecision(currentOrder,it.findFragment(),position)
                }


    }

    fun setDecision(order : Order, fragment: OrdersManagerFragment, position : Int) {

        val builder = AlertDialog.Builder(fragment.requireContext())

        builder.setPositiveButton("Yes") { _, _ ->

            val orderRef = mFirestore.collection("orders").document(order.getorderId().toString())

            orderRef.update(
                mapOf("decision" to "Accepted","orderDecision" to "Accepted")
                ).addOnCompleteListener {
                Toast.makeText(
                        fragment.requireContext(),
                        "Order was Accepted",
                        Toast.LENGTH_SHORT).show()

                notifyDataSetChanged()

            }.addOnFailureListener {
            }
        }

        builder.setNegativeButton("No") { _, _ ->

            val orderRef = mFirestore.collection("orders").document(order.getorderId().toString())

            orderRef.update(
                mapOf("decision" to "Refused","orderDecision" to "Refused")
            ).addOnCompleteListener {
                Toast.makeText(
                    fragment.requireContext(),
                    "Order was Refused",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener {
            }

        }

        builder.setTitle("Make decision")
        builder.setMessage("Did you want to accept the order of ${order.getorderDeviceName()}?")
        builder.create().show()
        notifyItemChanged(position)

    }

    fun setOrderData(order: List<Order>){
        this.OrderList = order
        notifyDataSetChanged()
    }
}