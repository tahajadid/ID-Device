package com.example.id_dev_fire.ui.ordersManager

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
import androidx.recyclerview.widget.RecyclerView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.ImpactOrder
import com.example.id_dev_fire.model.Order
import com.example.id_dev_fire.model.TokenDevice
import com.example.id_dev_fire.notificationClasses.NotificationData
import com.example.id_dev_fire.notificationClasses.PushNotification
import com.example.id_dev_fire.notificationClasses.RetrofitInstance
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListOrdersManagerAdapter(var OrdersManagerList : ArrayList<Order>) : RecyclerView.Adapter<ListOrdersManagerAdapter.MyViewHolder>() {

    private var mFirestore = FirebaseFirestore.getInstance()

    @SuppressLint("ResourceAsColor")
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

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

                val mImpactOrder = ImpactOrder()
                mImpactOrder.acceptOrder(OrdersManagerList[adapterPosition].id.toString(),
                    OrdersManagerList[adapterPosition].idDevice.toString())

                mFirestore.collection("orders")
                    .document(OrdersManagerList[adapterPosition].getorderId().toString())
                    .update(
                        mapOf("decision" to "Accepted","orderDecision" to "Accepted")
                    ).addOnCompleteListener {
                        // Update the list of orders
                        // set a message to the manager

                        Toast.makeText(
                            fragment.requireContext(),
                            "Order was Accepted",
                            Toast.LENGTH_SHORT
                        ).show()
                        OrdersManagerList[adapterPosition].setOrderDecision("Accepted")
                        notifyItemChanged(adapterPosition)

                        // We need to get the information of order refused
                        mFirestore.collection("orders")
                            .document(OrdersManagerList[adapterPosition].getorderId().toString())
                            .get().addOnCompleteListener {

                                if(it.isSuccessful) {
                                    val newOrder = it.result!!.toObject(Order::class.java)

                                    // Now we need to get the token of Developer device
                                    // And sending to him a notification

                                    mFirestore.collection("tokens")
                                        .document(newOrder!!.getorderCurrentOwner().toString())
                                        .get().addOnCompleteListener {
                                            val newToken = it.result!!.toObject(TokenDevice::class.java)

                                            val title = "Order Accepted"
                                            val message =  newOrder!!.getorderFullNameDeviceOwner() +
                                                    " accepted your order of : " + newOrder!!.getorderDeviceName()

                                            PushNotification(
                                                NotificationData(title,message),
                                                newToken!!.getTokDeviceToken().toString()
                                            ).also{
                                                sendNotification(it)
                                            }

                                        }.addOnFailureListener {
                                            //
                                        }

                                }else {
                                    //
                                }

                            }.addOnFailureListener {

                            }

                    }.addOnFailureListener {
                    }

            }

            builder.setNegativeButton("Refuse") { _, _ ->

                mFirestore.collection("orders")
                    .document(OrdersManagerList[adapterPosition].getorderId().toString())
                    .update(
                        mapOf("decision" to "Refused","orderDecision" to "Refused")
                    ).addOnCompleteListener {
                        // Update the list of orders
                        // set a message to the manager

                        Toast.makeText(
                            fragment.requireContext(),
                            "Order was Refused",
                            Toast.LENGTH_SHORT
                        ).show()
                        OrdersManagerList[adapterPosition].setOrderDecision("Refused")
                        notifyItemChanged(adapterPosition)

                        // We need to get the information of order refused
                        mFirestore.collection("orders")
                        .document(OrdersManagerList[adapterPosition].getorderId().toString())
                        .get().addOnCompleteListener {

                            if(it.isSuccessful) {
                                val newOrder = it.result!!.toObject(Order::class.java)

                                // Now we need to get the token of Developer device
                                // And sending to him a notification

                                mFirestore.collection("tokens")
                                    .document(newOrder!!.getorderCurrentOwner().toString())
                                    .get().addOnCompleteListener {
                                        val newToken = it.result!!.toObject(TokenDevice::class.java)

                                        val title = "Order Refused"
                                        val message =  newOrder!!.getorderFullNameDeviceOwner() +
                                                " refused your order of : " + newOrder!!.getorderDeviceName()

                                        PushNotification(
                                            NotificationData(title,message),
                                            newToken!!.getTokDeviceToken().toString()
                                        ).also{
                                            sendNotification(it)
                                        }

                                    }.addOnFailureListener {
                                     //
                                    }

                            }else {
                                //
                            }

                        }.addOnFailureListener {

                        }

                    }.addOnFailureListener {
                    }

            }

            builder.setNeutralButton("Cancel"
            ){  _, _ ->
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
        holder.itemView.findViewById<TextView>(R.id.nameDeviceOfOrderManager_tv)
            .setText(currentOrder.getorderDeviceName())
        holder.itemView.findViewById<TextView>(R.id.personOrderedOfOrderManager_tv)
                .setText(currentOrder.getorderfullNamecurrentOwner())
        holder.itemView.findViewById<TextView>(R.id.stateOfOrderManager_tv)
            .setText(currentOrder.getorderDecision())

        holder.itemView.findViewById<TextView>(R.id.reasonOfOrderManager_tv)
            .setText(currentOrder.getorderReason())

        if (currentOrder.getorderDecision().equals("Accepted"))
            holder.itemView.findViewById<TextView>(R.id.stateOfOrderManager_tv).setTextColor(R.color.white)

        if (currentOrder.getorderDecision().equals("Refused"))
            holder.itemView.findViewById<TextView>(R.id.stateOfOrderManager_tv).setTextColor(R.color.white)


    }


    fun setOrderData(order: List<Order>){
        this.OrdersManagerList = order as ArrayList<Order>
        notifyDataSetChanged()
    }

    private fun sendNotification(notification : PushNotification) = CoroutineScope(Dispatchers.IO).launch{

        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d("testNotif","#### Successful" )
            }else{
                Log.d("testNotif", response.errorBody().toString() )
            }

        }catch (e : Exception){
            Log.d("testNotif",e.toString() )
        }

    }
}