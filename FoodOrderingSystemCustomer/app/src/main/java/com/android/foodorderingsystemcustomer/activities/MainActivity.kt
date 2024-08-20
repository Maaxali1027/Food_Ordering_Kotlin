package com.android.foodorderingsystemcustomer.activities


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.foodorderingsystemcustomer.R
import com.android.foodorderingsystemcustomer.adapters.MenuAdapter
import com.android.foodorderingsystemcustomer.databinding.ActivityMainBinding
import com.android.foodorderingsystemcustomer.model.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var menuAdapter: MenuAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var NavController = findNavController(R.id.fragmentContainerView)
        var bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(NavController)


//        binding.notificationButton.setOnClickListener {
//            val bottomSheetDialog = NotificationBottomFragment()
//            bottomSheetDialog.show(supportFragmentManager,"Test")
//        }



    }
//



}