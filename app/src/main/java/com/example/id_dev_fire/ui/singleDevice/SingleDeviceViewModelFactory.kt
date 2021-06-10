package com.example.id_dev_fire.ui.singleDevice

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SingleDeviceViewModelFactory(
    private val application: Application,
    private val idDev : String
): ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        SingleDeviceViewModel(idDev,application) as T

}