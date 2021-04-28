package com.example.id_dev_fire.ui.help_center

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HelpCenterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is MESA Fragment"
    }
    val text: LiveData<String> = _text
}