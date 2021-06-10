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

class ListOrdersAdapter(var OrderList : ArrayList<Order>) : RecyclerView.Adapter<ListOrdersAdapter.MyViewHolder>()  {


    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var mFirestore = FirebaseFirestore.getInstance()

        init {
            // When you click on delete Image
            itemView.findViewById<LinearLayoutCompat>(R.id.rowOrderLayout)
                .findViewById<ImageView>(R.id.imageView_device).setOnClickListener {
                    deletOrder(it.findFragment())
                }
        }

        fun deletOrder(fragment: OrdersFragment) {

            val builder = AlertDialog.Builder(fragment.requireContext())
            builder.setPositiveButton("Yes") { _, _ ->

                val fRef = mFirestore.collection("orders").document(OrderList[adapterPosition].id.toString())
                fRef.delete().addOnSuccessListener {
                    Toast.makeText(
                        fragment.requireContext(),
                        "Successfully removed",
                        Toast.LENGTH_SHORT).show()

                    OrderList.removeAt(adapterPosition)
                    notifyDataSetChanged()

                }.addOnFailureListener {
                }

            }
            builder.setNegativeButton("No") { _, _ -> }

            builder.setTitle("Delete Order")
            builder.setMessage("Are you sure you want to delete order of ?")
            builder.create().show()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_order_list, parent, false))
    }

    override fun getItemCount(): Int {
        return OrderList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int){

        val currentOrder= OrderList[position]
        holder.itemView.findViewById<TextView>(R.id.nameDeviceOfOrder_tv).setText(currentOrder.getorderDeviceName())
        holder.itemView.findViewById<TextView>(R.id.ownerDeviceOfOrder_tv).setText(currentOrder.getorderFullNameDeviceOwner())
        holder.itemView.findViewById<TextView>(R.id.stateOrder_tv).setText(currentOrder.getorderDecision())

    }

    fun setOrderData(order: ArrayList<Order>){
        OrderList = order
        notifyDataSetChanged()
    }

}