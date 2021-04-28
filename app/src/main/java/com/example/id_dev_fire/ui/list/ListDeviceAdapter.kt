package com.example.id_dev_fire.ui.list

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Cupboard
import com.example.id_dev_fire.model.Device

class ListDeviceAdapter : RecyclerView.Adapter<ListDeviceAdapter.MyViewHolder>()  {


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

    }

    fun setDeviceData(device: List<Device>){
        this.DevList = device
        notifyDataSetChanged()
    }

}