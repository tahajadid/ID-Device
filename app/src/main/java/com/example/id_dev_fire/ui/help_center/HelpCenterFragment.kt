package com.example.id_dev_fire.ui.help_center

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.Lottie
import com.example.id_dev_fire.R

class HelpCenterFragment : Fragment() {

    private lateinit var helpCenterViewModel: HelpCenterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        helpCenterViewModel =
            ViewModelProvider(this).get(HelpCenterViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_help_center, container, false)

        val textView: TextView = root.findViewById(R.id.text_help_center)
        helpCenterViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }
}