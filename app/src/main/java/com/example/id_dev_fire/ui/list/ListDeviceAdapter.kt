package com.example.id_dev_fire.ui.list

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.model.Employer
import com.example.id_dev_fire.ui.evs.EvsFragmentDirections
import com.example.id_dev_fire.ui.evs.EvsViewModel
import com.example.id_dev_fire.ui.mesa.MesaFragmentDirections
import com.example.id_dev_fire.ui.mims.MimsFragmentDirections
import com.google.firebase.firestore.FirebaseFirestore

class ListDeviceAdapter : RecyclerView.Adapter<ListDeviceAdapter.MyViewHolder>()  {

    private lateinit var Deviceid : String
    private var DevList = emptyList<Device>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_device_list, parent, false))
    }

    override fun getItemCount(): Int {
        return DevList.size
    }
    
    @SuppressLint("RestrictedApi", "ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentDevice = DevList[position]

        holder.itemView.findViewById<TextView>(R.id.nameDevice_txt)
            .setText(currentDevice.getdevName())
        holder.itemView.findViewById<TextView>(R.id.version_tv)
            .setText(currentDevice.getvdeVersion())
        holder.itemView.findViewById<TextView>(R.id.fullNameDeviceOwner_label_txt)
            .setText(currentDevice.getdevFullNameOwner())
        holder.itemView.findViewById<TextView>(R.id.projectDev_txt)
            .setText(currentDevice.getdevProjectName())

        if(currentDevice.getdevCurrentState().toString() == "Reserved"){
            holder.itemView.findViewById<TextView>(R.id.circle_currentState)
                .setBackgroundResource(R.drawable.mycercle_red)
            holder.itemView.findViewById<TextView>(R.id.deviceCurrentState_tv)
                .setText(currentDevice.getdevCurrentState())
            holder.itemView.findViewById<TextView>(R.id.deviceCurrentState_tv)
                .setTextColor(R.color.black)
        }

        if(currentDevice.getdevState().toString() == "Maintaining"){
            holder.itemView.findViewById<TextView>(R.id.circle_state)
                .setBackgroundResource(R.drawable.mycercle_red)
            holder.itemView.findViewById<TextView>(R.id.deviceState_tv)
                .setText(currentDevice.getdevState())
            holder.itemView.findViewById<TextView>(R.id.deviceState_tv)
                .setTextColor(R.color.black)

            holder.itemView.findViewById<TextView>(R.id.circle_currentState)
                .setBackgroundResource(R.drawable.mycercle_red)
            holder.itemView.findViewById<TextView>(R.id.deviceCurrentState_tv)
                .setText("Not available")
            holder.itemView.findViewById<TextView>(R.id.deviceCurrentState_tv)
                .setTextColor(R.color.black)
        }

        this.Deviceid = currentDevice.getdevId().toString()

        /*
        * Fragment Manager can't be implemented while using a NavCotroller
        * The BackStach returns always a null
        * */

        holder.itemView.findViewById<LinearLayoutCompat>(R.id.rowDeviceLayout).setOnClickListener {

            // We should know the device Owner of the device to know wich is the actual Fragment
            // The solution was to know the device owner of the item selected
            // Each Device belong to a trib which had an only Manager label
            // In this application case there is 3 tribs

            if(currentDevice.getdevProjectName() == "EVS"){
                val action = EvsFragmentDirections.actionNavEvsToSingleDeviceFragment(
                    currentDevice.getdevId().toString(),currentDevice)
                holder.itemView.findNavController().navigate(action)

            }else if (currentDevice.getdevProjectName() == "MiMs"){
                val action = MimsFragmentDirections.actionNavMimsToSingleDeviceFragment(
                    currentDevice.getdevId().toString(),currentDevice)
                holder.itemView.findNavController().navigate(action)

            }else if(currentDevice.getdevProjectName() == "MESA"){
                val action = MesaFragmentDirections.actionNavMesaToSingleDeviceFragment(
                    currentDevice.getdevId().toString(),currentDevice)
                holder.itemView.findNavController().navigate(action)
            }

        }

    }

    fun setDeviceData(device: List<Device>){
        this.DevList = device
        notifyDataSetChanged()
    }

}