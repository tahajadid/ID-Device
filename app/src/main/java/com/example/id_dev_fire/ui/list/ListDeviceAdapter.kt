package com.example.id_dev_fire.ui.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.ui.evs.EvsFragmentDirections

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
    
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentDevice = DevList[position]
        holder.itemView.findViewById<TextView>(R.id.nameDevice_txt).setText(currentDevice.getdevName())
        holder.itemView.findViewById<TextView>(R.id.deviceOwner_txt).setText(currentDevice.getdevOwner())
        holder.itemView.findViewById<TextView>(R.id.version_tv).setText(currentDevice.getvdeVersion())
        this.Deviceid = currentDevice.getdevId().toString()

        /*
        * I need to add a tool who knows which is the actual fragment
        * */

        holder.itemView.findViewById<LinearLayoutCompat>(R.id.rowDeviceLayout).setOnClickListener {

            val action = EvsFragmentDirections.actionNavEvsToSingleDeviceFragment(currentDevice.getdevId().toString())
            // take the id of the selected device
            Log.d("to_see","you clicked on -- ${currentDevice.getdevId()}")
            holder.itemView.findNavController().navigate(action)
        }


    }

    fun setDeviceData(device: List<Device>){
        this.DevList = device
        notifyDataSetChanged()
    }

}