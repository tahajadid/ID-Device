package com.example.id_dev_fire.ui.mesa

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.ui.list.ListDeviceAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.firestore.FirebaseFirestore

class MesaFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var mesaViewModel: MesaViewModel
    lateinit var mProgressDialog: Dialog
    private var mFirestore = FirebaseFirestore.getInstance()
    private var SearchDevices: MutableList<Device> = arrayListOf()
    private var AllDevices: MutableList<Device> = arrayListOf()

    var adapter = ListDeviceAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mesaViewModel =
            ViewModelProvider(this).get(MesaViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mesa, container, false)

        // Recyclerview
        val adapter = ListDeviceAdapter()
        val recyclerView : RecyclerView = root.findViewById(R.id.recyclerview_mesa_device)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // DeviceViewModel
        mesaViewModel = ViewModelProvider(this).get(MesaViewModel::class.java)

        val noDevices = root.findViewById<TextView>(R.id.noDeviceFoundMesa_tv)
        val emtyAnim = root.findViewById<LottieAnimationView>(R.id.anim_empty_mesa)
        val sh = root.findViewById<ShimmerFrameLayout>(R.id.myshimmer_mesa)

        emtyAnim.visibility = View.INVISIBLE
        noDevices.visibility = View.INVISIBLE
        sh.startShimmerAnimation()

        // Observe all the LiveData returned from the ViewModel
        mesaViewModel.readAllData.observe(viewLifecycleOwner, Observer { device ->
            adapter.setDeviceData(device)
            if(mesaViewModel.readAllData.value == emptyList<Device>()){
                emtyAnim.visibility = View.VISIBLE
                noDevices.visibility = View.VISIBLE
            }else{
            }
            sh.stopShimmerAnimation()
            sh.visibility = View.GONE
        })

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu,inflater: MenuInflater) {
        inflater.inflate(R.menu.main,menu)

        val item = menu?.findItem(R.id.app_bar_search)
        val searchView : androidx.appcompat.widget.SearchView = item?.actionView as androidx.appcompat.widget.SearchView

        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        mFirestore.collection("devices").whereEqualTo("projectName","MESA").orderBy("devName").startAt(newText).endAt(newText + "\uf8ff").addSnapshotListener { value, error ->
            SearchDevices = arrayListOf()
            for(a in value!!.toObjects(Device::class.java)){
                SearchDevices.add(a)
            }
            AllDevices = SearchDevices
            adapter.setDeviceData(AllDevices)
        }
        return true
    }

}