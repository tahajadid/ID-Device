package com.example.id_dev_fire

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.id_dev_fire.model.Employer
import com.example.id_dev_fire.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var mFirestoreAuth = FirebaseAuth.getInstance()
    private var mFirestore = FirebaseFirestore.getInstance()
    private lateinit var mFirestoreUser : FirebaseUser

    private lateinit var userName : TextView
    private lateinit var userMail : TextView
    lateinit var button_logout : Button

    private lateinit var actualUid : String
    lateinit var longNameEmployer : String
    lateinit var roleEmployer : String

    lateinit var employer: Employer

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val header = navView.getHeaderView(0)
        val navController = findNavController(R.id.nav_host_fragment)

        mFirestoreUser= mFirestoreAuth.currentUser

        userMail = header?.findViewById(R.id.actualMailUser)!!
        userName = header?.findViewById(R.id.actualNameUser)!!
        button_logout =  header?.findViewById(R.id.buttonLogout)!!

        userMail.setText(mFirestoreUser.email)
        actualUid = mFirestoreUser.uid

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_mims,R.id.nav_mesa, R.id.nav_evs,
        R.id.nav_settings,R.id.nav_orders,R.id.nav_support,R.id.nav_addEmployer,R.id.nav_addDevice,
        R.id.nav_addCupboard,R.id.nav_singleDeviceFragment,R.id.nav_orderDeviceFragment,
        R.id.nav_ordersManager,R.id.nav_changePassword,R.id.nav_bug,R.id.nav_bug,R.id.nav_listEmployers,
        R.id.nav_information)
            , drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setInfo(navView)

        button_logout.setOnClickListener{
            mFirestoreAuth.signOut()

            val intentLogin = Intent(this, LoginActivity::class.java)
            intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentLogin)
        }


    }


    fun setInfo(navView : NavigationView) {

        mFirestore.collection("employers")
                .whereEqualTo("id",actualUid)
                .get().addOnCompleteListener {
                    if (it.isSuccessful){
                        for (res in it.result!!) {
                            employer = res.toObject(Employer::class.java)
                            longNameEmployer = employer.getEmployerFirstName() + " " + employer.getEmployerLastName()
                            roleEmployer = employer.getEmployerRole()
                            userName.setText(longNameEmployer)
                        }
                        redrawNavView(navView,roleEmployer)
                    }else {
                        userName.setText("User")
                        redrawNavView(navView,"User")
                    }
                }.addOnFailureListener {
                    userName.setText("User")
                    redrawNavView(navView,"User")
                }

    }

    fun redrawNavView(navView : NavigationView, roleEmployer : String) {

        // We have 3 case : Manager, Administrator and a Developer
        // we pass a last case as a "User" when we will fail to connect to firebase
        // and we give to this user the access as a developer


        when(roleEmployer) {
            "Manager" -> {
                navView.menu.findItem(R.id.nav_addCupboard).setVisible(false)
                navView.menu.findItem(R.id.nav_addDevice).setVisible(false)
                navView.menu.findItem(R.id.nav_addEmployer).setVisible(false)
                navView.menu.findItem(R.id.nav_orders).setVisible(false)
                navView.menu.findItem(R.id.nav_listEmployers).setVisible(false)
            }
            "Administrator" -> {
                navView.menu.findItem(R.id.nav_orders).setVisible(false)
                navView.menu.findItem(R.id.nav_ordersManager).setVisible(false)
            }
            else -> {
                navView.menu.findItem(R.id.nav_addCupboard).setVisible(false)
                navView.menu.findItem(R.id.nav_addDevice).setVisible(false)
                navView.menu.findItem(R.id.nav_addEmployer).setVisible(false)
                navView.menu.findItem(R.id.nav_ordersManager).setVisible(false)
                navView.menu.findItem(R.id.nav_listEmployers).setVisible(false)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}