package com.example.id_dev_fire.ui.mims

import android.app.Dialog
import android.os.Bundle
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


class MimsFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var mimsViewModel: MimsViewModel
    lateinit var mProgressDialog: Dialog
    private var SearchDevices: MutableList<Device> = arrayListOf()
    private var AllDevices: MutableList<Device> = arrayListOf()
    private var mFirestore = FirebaseFirestore.getInstance()
    lateinit var thisRecyclerView : RecyclerView

    var adapter = ListDeviceAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        mimsViewModel =
                ViewModelProvider(this).get(MimsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mims, container, false)

        // Recyclerview
        val adapter = ListDeviceAdapter()

        view?.setBackgroundResource(R.drawable.inside_layers)

        val recyclerView : RecyclerView = root.findViewById(R.id.recyclerview_mims_device)
        thisRecyclerView = recyclerView

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val noDevices = root.findViewById<TextView>(R.id.noDeviceFoundMims_tv)
        val emtyAnim = root.findViewById<LottieAnimationView>(R.id.anim_empty_mims)
        val shimmer = root.findViewById<ShimmerFrameLayout>(R.id.myshimmer_mims)

        // We start the Shimmer when data is loading
        shimmer.startShimmerAnimation()

        emtyAnim.visibility = View.INVISIBLE
        noDevices.visibility = View.INVISIBLE

        // Observe all the LiveData returned from the ViewModel
        mimsViewModel.readAllData.observe(viewLifecycleOwner, Observer { device ->
            adapter.setDeviceData(device)
            if(mimsViewModel.readAllData.value == emptyList<Device>()){

                // The case when we didn't have any Device in list we set the Animation and Quote visible
                emtyAnim.visibility = View.VISIBLE
                noDevices.visibility = View.VISIBLE
            }else {
            }

            // We stop the Shimmer when we get a Response from Firebase
            shimmer.startShimmerAnimation()
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

        mFirestore.collection("devices").whereEqualTo("projectName","MiMs")
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