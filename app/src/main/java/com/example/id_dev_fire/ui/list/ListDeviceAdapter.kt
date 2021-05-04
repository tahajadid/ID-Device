package com.example.id_dev_fire.ui.list

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.ui.evs.EvsFragmentDirections
import com.example.id_dev_fire.ui.mesa.MesaFragmentDirections
import com.example.id_dev_fire.ui.mims.MimsFragmentDirections

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
    
    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentDevice = DevList[position]
        holder.itemView.findViewById<TextView>(R.id.nameDevice_txt).setText(currentDevice.getdevName())
        holder.itemView.findViewById<TextView>(R.id.deviceOwner_txt).setText(currentDevice.getdevOwner())
        holder.itemView.findViewById<TextView>(R.id.version_tv).setText(currentDevice.getvdeVersion())
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

            if(currentDevice.getdevOwner() == "Manager EVS"){
                val action = EvsFragmentDirections.actionNavEvsToSingleDeviceFragment(currentDevice.getdevId().toString())
                holder.itemView.findNavController().navigate(action)

            }else if (currentDevice.getdevOwner() == "Manager MiMs"){
                val action = MimsFragmentDirections.actionNavMimsToSingleDeviceFragment(currentDevice.getdevId().toString())
                holder.itemView.findNavController().navigate(action)

            }else if(currentDevice.getdevOwner() == "Manager MESA"){
                val action = MesaFragmentDirections.actionNavMesaToSingleDeviceFragment(currentDevice.getdevId().toString())
                holder.itemView.findNavController().navigate(action)
            }

        }

    }

    fun setDeviceData(device: List<Device>){
        this.DevList = device
        notifyDataSetChanged()
    }

}