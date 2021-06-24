package com.example.id_dev_fire.ui.listEmployers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.id_dev_fire.R
import com.example.id_dev_fire.model.Employer
import com.example.id_dev_fire.model.Order
import com.example.id_dev_fire.model.TokenDevice
import com.example.id_dev_fire.notificationClasses.NotificationData
import com.example.id_dev_fire.notificationClasses.PushNotification
import com.example.id_dev_fire.notificationClasses.RetrofitInstance
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmployersAdapter(var EmployerList : ArrayList<Employer>) : RecyclerView.Adapter<EmployersAdapter.MyViewHolder>()  {


        inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            private var mFirestore = FirebaseFirestore.getInstance()

            init {
                // When you click on delete Image
                itemView.findViewById<LinearLayoutCompat>(R.id.rowEmployersLayout)
                    .findViewById<ImageView>(R.id.imageView_acceptEmployer).setOnClickListener {
                        acceptEmployer(it.findFragment())
                    }
            }

            fun acceptEmployer(fragment: EmployersFragment){

                val builder = AlertDialog.Builder(fragment.requireContext())
                builder.setPositiveButton("Yes") { _, _ ->

                    mFirestore.collection("employers").document(EmployerList[adapterPosition].id)
                        .update(
                            hashMapOf("employerFlagAccess" to true,
                                "flagAccess" to true) as Map<String, Any>
                        ).addOnSuccessListener {
                        Toast.makeText(
                            fragment.requireContext(),
                            "Flag Raised",
                            Toast.LENGTH_SHORT).show()

                            mFirestore.collection("tokens").document(EmployerList[adapterPosition].id.toString())
                                .get().addOnCompleteListener {

                                    val newToken = it.result!!.toObject(TokenDevice::class.java)

                                    val title = "Good News"
                                    val message =
                                            "Your Requet was accepted by the admin. Go and take the action" +
                                            "to discover ID-Device"

                                    PushNotification(
                                        NotificationData(title,message),
                                        newToken!!.getTokDeviceToken().toString()
                                    ).also{
                                        sendNotification(it)
                                    }

                                }.addOnFailureListener {
                                    // Noth..
                                }



                    }.addOnFailureListener {
                            Toast.makeText(
                                fragment.requireContext(),
                                "Please Try Again",
                                Toast.LENGTH_SHORT).show()
                    }

                }
                builder.setNegativeButton("No") { _, _ -> }

                builder.setTitle("Accept User")
                builder.setMessage("Are you sure you want to give this user the access ?")
                builder.create().show()
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_employer_list, parent, false))
        }

        override fun getItemCount(): Int {
            return EmployerList.size
        }

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        override fun onBindViewHolder(holder: MyViewHolder, position: Int){

            val currentEmployer = EmployerList[position]
            holder.itemView.findViewById<TextView>(R.id.fullNameListEmployers_tv).setText(
                currentEmployer.getEmployerFirstName()+" "+currentEmployer.getEmployerLastName())
            holder.itemView.findViewById<TextView>(R.id.emailListEmployers_tv).setText(currentEmployer.getEmployerEmail())
            holder.itemView.findViewById<TextView>(R.id.roleListEmployers_tv).setText(currentEmployer.getEmployerRole())

        }

        fun setEmployerData(employer: ArrayList<Employer>){
            EmployerList = employer
            notifyDataSetChanged()
        }

    private fun sendNotification(notification : PushNotification) = CoroutineScope(Dispatchers.IO).launch{

        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d("testNotif","#### Successful" )
            }else{
                Log.d("testNotif", response.errorBody().toString() )
            }

        }catch (e : Exception){
            Log.d("testNotif",e.toString() )
        }

    }

    }