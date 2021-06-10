package com.example.id_dev_fire.ui.evs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.ui.list.ListDeviceAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.firestore.FirebaseFirestore

class EvsFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var evsViewModel: EvsViewModel
    lateinit var mProgressDialog: Dialog
    private var AllDevices: MutableList<Device> = arrayListOf()
    private var SearchDevices: MutableList<Device> = arrayListOf()
    private var mFirestore = FirebaseFirestore.getInstance()

    // Recyclerview
    var adapter = ListDeviceAdapter()

    @SuppressLint("WrongConstant")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        evsViewModel =
                ViewModelProvider(this).get(EvsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_evs, container, false)

        view?.setBackgroundResource(R.drawable.inside_layers)

        val recyclerView : RecyclerView = root.findViewById(R.id.recyclerview_evs_device)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val noDevices = root.findViewById<TextView>(R.id.noDeviceFoundEvs_tv)
        val emtyAnim = root.findViewById<LottieAnimationView>(R.id.anim_empty_evs)
        val shimmer = root.findViewById<ShimmerFrameLayout>(R.id.myshimmer_evs)

        // We start the Shimmer when data is loading
        shimmer.startShimmerAnimation()

        emtyAnim.visibility = View.INVISIBLE
        noDevices.visibility = View.INVISIBLE

        // Observe all the LiveData returned from the ViewModel
        evsViewModel.readAllData.observe(viewLifecycleOwner, { device ->
            adapter.setDeviceData(device)
            if(evsViewModel.readAllData.value == emptyList<Device>()){
                // The case when we didn't have any Device in list we set the Animation and Quote visible
                emtyAnim.visibility = View.VISIBLE
                noDevices.visibility = View.VISIBLE
            }else{
            }

            // We stop the Shimmer when we get a Response from Firebase
            shimmer.stopShimmerAnimation()
            shimmer.visibility = View.GONE
        })

        // Add menu
        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu,inflater: MenuInflater) {
        inflater.inflate(R.menu.main,menu)

        // We add the widget of Searching to the AppBar
        val item = menu?.findItem(R.id.app_bar_search)
        val searchView : androidx.appcompat.widget.SearchView = item?.actionView as androidx.appcompat.widget.SearchView

        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

    }

    /*
        Searching Part made by a queries in our Database "Firebase"
     */

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        mFirestore.collection("devices").whereEqualTo("projectName","EVS")
            .orderBy("devName").startAt(newText).endAt(newText + "\uf8ff")
            .addSnapshotListener { value, error ->
            SearchDevices = arrayListOf()
            for(a in value!!.toObjects(Device::class.java)){

                // If the name of the device enter on the SerachBar match with our Device
                    // We add it to the new list that will be displayed
                SearchDevices.add(a)
            }
            AllDevices = SearchDevices
            adapter.setDeviceData(AllDevices)
        }

        return true
    }

}