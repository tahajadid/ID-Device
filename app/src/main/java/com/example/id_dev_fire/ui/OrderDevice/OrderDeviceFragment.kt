package com.example.id_dev_fire.ui.OrderDevice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import com.example.id_dev_fire.R
import java.util.*

class OrderDeviceFragment : Fragment() {

    lateinit var btn : Button
    lateinit var datePickerFrom: DatePicker
    lateinit var datePickerTo: DatePicker


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_order_device, container, false)

        datePickerFrom = root.findViewById<DatePicker>(R.id.calendarFrom)
        datePickerTo = root.findViewById<DatePicker>(R.id.calendarTo)
        val today  = Calendar.getInstance()

        datePickerFrom.init(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH)){
            view, year, month, day ->
            val month = month + 1
            val actual = day.toString()+" - "+month.toString()+" - "+year.toString()
            val actualDate = root.findViewById<TextView>(R.id.fromDate_tv)
            actualDate.setText(actual)
        }

        datePickerTo.init(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH)){
            view, year, month, day ->
            val month = month + 1
            val actual = day.toString()+" - "+month.toString()+" - "+year.toString()
            val actualDate = root.findViewById<TextView>(R.id.toDate_tv)
            actualDate.setText(actual)
        }

        return root
    }


}