package com.example.id_dev_fire.ui.listDamagedDevices

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.DamagedDevice
import com.google.firebase.firestore.FirebaseFirestore


class ListDamagedAdapter (var DamagedList : ArrayList<DamagedDevice>) : RecyclerView.Adapter<ListDamagedAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var mFirestore = FirebaseFirestore.getInstance()

        init {
            // When you click on delete Image
            itemView.findViewById<LinearLayout>(R.id.deleteComplaint_linearLayout)
                .setOnClickListener {
                    deleteComplaint(it.findFragment())
                }
        }

        fun deleteComplaint(fragment: ListDamagedFragment){

            val builder = AlertDialog.Builder(fragment.requireContext())
            builder.setPositiveButton("Yes") { _, _ ->

                Log.d("tt","-----------  "+DamagedList[adapterPosition].getDecIdDamagedDevice().toString())
                mFirestore.collection("damagedDevice")
                    .document(DamagedList[adapterPosition].idDamagedDevice!!)
                    .delete().addOnCompleteListener {

                        Toast.makeText(
                            fragment.requireContext(),
                            "Complaint Deleted, Thank's.",
                            Toast.LENGTH_SHORT).show()

                        DamagedList.removeAt(adapterPosition)
                        notifyDataSetChanged()


                    }.addOnFailureListener {
                        Toast.makeText(
                            fragment.requireContext(),
                            "Please Try Again !",
                            Toast.LENGTH_SHORT).show()
                    }
            }
            builder.setNegativeButton("No") { _, _ -> }

            builder.setTitle("Delete Complaint User")
            builder.setMessage("Are you sure you want to delete this complaint ?")
            builder.create().show()

        }

        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDamagedAdapter.MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_damaged_device, parent, false))
    }

    override fun getItemCount(): Int {
        return DamagedList.size
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ListDamagedAdapter.MyViewHolder, position: Int){

        val currentDamagedDevice = DamagedList[position]

        holder.itemView.findViewById<TextView>(R.id.damaged_device_tv).setText(currentDamagedDevice.getDecNameDevice())
        holder.itemView.findViewById<TextView>(R.id.declaredBy_et).setText(currentDamagedDevice.getDecDeclaredByFullName())
        holder.itemView.findViewById<TextView>(R.id.titleDamaged_et).setText(currentDamagedDevice.getDecTitle())
        holder.itemView.findViewById<TextView>(R.id.descriptionDamaged_et).setText(currentDamagedDevice.getDecDescriptionDevices())

    }

    fun setDamagedData(damaged: ArrayList<DamagedDevice>){
        DamagedList = damaged
        notifyDataSetChanged()
    }
}