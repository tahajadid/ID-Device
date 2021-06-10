package com.example.id_dev_fire.ui.singleDevice

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.inflate
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.id_dev_fire.MainActivity
import com.example.id_dev_fire.R
import com.example.id_dev_fire.firestoreClass.FirestoreClass
import com.example.id_dev_fire.firestoreClass.ImpactOrder
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.model.Order
import com.example.id_dev_fire.model.QueueDevices
import com.example.id_dev_fire.ui.orders.ListOrdersAdapter
import com.example.id_dev_fire.ui.orders.OrdersViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList


class SingleDeviceFragment : Fragment() {

    private lateinit var orderList:ArrayList<Order>
    private var mFirestoreClass = FirebaseFirestore.getInstance()
    lateinit var thisRecyclerView : RecyclerView

    private val args by navArgs<SingleDeviceFragmentArgs>()
    private var listOrder = mutableListOf<String>()
    lateinit var adapter: ListOrderedAdapter

    val calendar = Calendar.getInstance()
    val today = Date(calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val idDev = args.deviceSelected.getdevId()

        val singleDeviceViewModel : SingleDeviceViewModel by viewModels {
            SingleDeviceViewModelFactory(requireActivity().application,idDev.toString())
        }

        val root = inflater.inflate(R.layout.fragment_single_device, container, false)

        // Recyclerview
        orderList = ArrayList()
        adapter = ListOrderedAdapter(orderList)

        val noOrders = root.findViewById<TextView>(R.id.noDeviceFound_tv)
        val emtyAnim = root.findViewById<LottieAnimationView>(R.id.anim_empty_singleDev)
        val recyclerView : RecyclerView = root.findViewById(R.id.recyclerview_orderedby)

        thisRecyclerView = recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        emtyAnim.visibility = View.INVISIBLE
        noOrders.visibility = View.INVISIBLE

        // Observe all the LiveData returned from the ViewModel
        singleDeviceViewModel.readAllData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.setOrderData(it as ArrayList<Order>)
            if(singleDeviceViewModel.readAllData.value == emptyList<Device>()){
                emtyAnim.visibility = View.VISIBLE
                noOrders.visibility = View.VISIBLE
            }else{

            }
        })


        setHasOptionsMenu(true)

        root.findViewById<TextView>(R.id.nameSingleDevice_tv).setText(args.deviceSelected.getdevName())
        root.findViewById<TextView>(R.id.supportedOsSingleDevice_tv).setText(args.deviceSelected.getdevSupportedOS())
        root.findViewById<TextView>(R.id.featuresSingleDevice_tv).setText(args.deviceSelected.getdevFeatures())
        root.findViewById<TextView>(R.id.serviceName_tv).setText(args.deviceSelected.getdevServiceName())
        root.findViewById<TextView>(R.id.placeDevice_tv).setText(args.deviceSelected.getdevCupboard())

        if(args.deviceSelected.getdevCurrentState() == "Reserved"){
            // If we want to get Data from the FirestoreClass we will not be able
            // To set the irght value, cause we didn't use coroutines before
            var date = Date()
            var newList = mutableListOf<String>()
            mFirestoreClass.collection("queueDevices").document(args.deviceSelected.getdevId().toString())
                .get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val newQueue = it.result!!.toObject(QueueDevices::class.java)
                        if (newQueue != null) {
                            listOrder = newQueue!!.getQueueDevicesListOrders()
                            newList = newQueue.getQueueDevicesListOrders().toMutableList()
                            if(newList.size > 0){
                                mFirestoreClass.collection("orders").document(newList[newList.size-1])
                                    .get().addOnCompleteListener {

                                        val newOrder = it.result!!.toObject(Order::class.java)
                                        date = newOrder!!.getorderDateEnd()!!

                                        if(date.after(today) || date.equals(today)){
                                            // We need to put the day after the EndDay of Order.. because that day is included
                                            // Into Order Duration
                                            // Mounth in Java are from 0 to 11 .. so we need to always inc the value to have the real date
                                            root.findViewById<TextView>(R.id.dateAvailable_tv)
                                                .setText((date.date+1).toString() + " - " +
                                                        (date.month+1).toString() + " - " +
                                                        date.year.toString())

                                        }else{
                                            root.findViewById<TextView>(R.id.dateAvailable_tv)
                                                .setText(" Now ")
                                        }
                                    }
                            }else{
                                root.findViewById<TextView>(R.id.dateAvailable_tv)
                                    .setText(" Now ")
                            }
                        }
                    }else{
                        root.findViewById<TextView>(R.id.dateAvailable_tv)
                            .setText(" Now ")
                    }
                }.addOnFailureListener {

                }
        }else{
            root.findViewById<TextView>(R.id.dateAvailable_tv)
                .setText(" Now ")
        }


        // Checking the state of State and make updates on the Queue
        val impactOrder = ImpactOrder()
        impactOrder.checkDeviceState(args.deviceSelected.getdevId().toString())

        root.findViewById<Button>(R.id.orderDevice_button).setOnClickListener {
            val action = SingleDeviceFragmentDirections
                    .actionNavSingleDeviceFragmentToOrderDeviceFragment(
                            args.deviceSelected.getdevName().toString(),
                            args.deviceSelected.getdevId().toString())
            it.findNavController().navigate(action)
        }

        return root

    }


}