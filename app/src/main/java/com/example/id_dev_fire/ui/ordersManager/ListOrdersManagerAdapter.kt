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

class ListOrdersManagerAdapter(var OrdersManagerList : ArrayList<Order>) : RecyclerView.Adapter<ListOrdersManagerAdapter.MyViewHolder>() {

    @SuppressLint("ResourceAsColor")
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var mFirestore = FirebaseFirestore.getInstance()

        init {
            itemView.findViewById<LinearLayoutCompat>(R.id.rowOrderManagerLayout)
                .findViewById<ImageView>(R.id.informationOrder_iv).setOnClickListener {
                    setDecision(it.findFragment())
                }

            itemView.findViewById<LinearLayoutCompat>(R.id.rowOrderManagerLayout)
                .findViewById<ImageView>(R.id.deleteManagerOrder_iv).setOnClickListener {
                    delteOrder(it.findFragment())
                }

        }

        fun setDecision(fragment: OrdersManagerFragment) {

            val builder = AlertDialog.Builder(fragment.requireContext())

            builder.setPositiveButton("Accept") { _, _ ->

                val orderRef = mFirestore.collection("orders")
                    .document(OrdersManagerList[adapterPosition].id.toString())

                orderRef.update(
                    mapOf("decision" to "Accepted","orderDecision" to "Accepted")
                ).addOnCompleteListener {
                    Toast.makeText(
                        fragment.requireContext(),
                        "Order was Accepted",
                        Toast.LENGTH_SHORT).show()
                    OrdersManagerList[adapterPosition].setOrderDecision("Accepted")
                    notifyItemChanged(adapterPosition)
                }.addOnFailureListener {
                }

            }

            builder.setNegativeButton("Refuse") { _, _ ->

                val orderRef = mFirestore.collection("orders").document(OrdersManagerList[adapterPosition].getorderId().toString())

                orderRef.update(
                    mapOf("decision" to "Refused","orderDecision" to "Refused")
                ).addOnCompleteListener {
                    Toast.makeText(
                        fragment.requireContext(),
                        "Order was Refused",
                        Toast.LENGTH_SHORT
                    ).show()
                    OrdersManagerList[adapterPosition].setOrderDecision("Refused")
                    notifyItemChanged(adapterPosition)
                }.addOnFailureListener {
                }

            }

            builder.setTitle("Make decision")
            builder.setMessage("Did you want to accept the order of ${OrdersManagerList[adapterPosition].getorderDeviceName()}?")
            builder.create().show()

        }

        fun delteOrder(fragment: OrdersManagerFragment) {

            val builder = AlertDialog.Builder(fragment.requireContext())

            builder.setPositiveButton("Yes") { _, _ ->

                val orderRef = mFirestore.collection("orders")
                    .document(OrdersManagerList[adapterPosition].id.toString())

                orderRef.delete().addOnCompleteListener {
                    Toast.makeText(
                        fragment.requireContext(),
                        "Order was deleted",
                        Toast.LENGTH_SHORT).show()

                    OrdersManagerList.removeAt(adapterPosition)
                    notifyDataSetChanged()

                }.addOnFailureListener {
                }

            }

            builder.setNegativeButton("No") { _, _ ->
            }

            builder.setTitle("Delete order")
            builder.setMessage("Did you want to delete the order : ${OrdersManagerList[adapterPosition].getorderDeviceName()}?")
            builder.create().show()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_order_manager_list, parent, false))
    }

    override fun getItemCount(): Int {
        return OrdersManagerList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentOrder= OrdersManagerList[position]
        holder.itemView.findViewById<TextView>(R.id.nameDeviceOfOrderManager_tv).setText(currentOrder.getorderDeviceName())
        holder.itemView.findViewById<TextView>(R.id.personOrderedOfOrderManager_tv).setText(currentOrder.getorderDeviceOwner())
        holder.itemView.findViewById<TextView>(R.id.stateOfOrderManager_tv).setText(currentOrder.getorderDecision())

        if (currentOrder.getorderDecision().equals("Accepted"))
            holder.itemView.findViewById<TextView>(R.id.stateOfOrderManager_tv).setTextColor(R.color.white)

        /*

        // Change the color of the staate of the Order
        if(currentOrder.getorderDecision().equals("On Hold")) {
            holder.itemView.findViewById<TextView>(R.id.stateOfOrderManager_tv).setTextColor(R.color.design_default_color_error)
        }else if(currentOrder.getorderDecision().equals("Accepted")){
            holder.itemView.findViewById<TextView>(R.id.stateOfOrderManager_tv).setTextColor(R.color.white)
        }
        else{
            holder.itemView.findViewById<TextView>(R.id.stateOfOrderManager_tv).setTextColor(R.color.white)
        }

         */

    }

    fun setOrderData(order: List<Order>){
        this.OrdersManagerList = order as ArrayList<Order>
        notifyDataSetChanged()
    }
}