package com.example.id_dev_fire

import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.id_dev_fire.model.Device
import com.example.id_dev_fire.model.Employer
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val header = navView.getHeaderView(0)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_evs,R.id.nav_mesa,R.id.nav_mims,
        R.id.nav_settings,R.id.nav_orders,R.id.nav_support,R.id.nav_addEmployer,R.id.nav_addDevice,
        R.id.nav_addCupboard)
            , drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        mFirestoreUser= mFirestoreAuth.currentUser

        userMail = header?.findViewById(R.id.actualMailUser)!!
        userName = header?.findViewById(R.id.actualNameUser)!!
        button_logout =  header?.findViewById(R.id.buttonLogout)!!

        userMail.setText(mFirestoreUser.email)
        actualUid = mFirestoreUser.uid

        mFirestore.collection("employers")
                .whereEqualTo("id",actualUid)
                .get().addOnCompleteListener {
                    if (it.isSuccessful){
                        for (result in it.result!!) {
                            val userInfo = result.toObject(Employer::class.java)
                            longNameEmployer = userInfo.getEmployerFirstName() + userInfo.getEmployerLastName()
                            userName.setText(longNameEmployer)
                        }

                    }else {
                        userName.setText("User")
                    }

                }.addOnFailureListener {
                    userName.setText("User")
                }

        button_logout.setOnClickListener{
            mFirestoreAuth.signOut()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}