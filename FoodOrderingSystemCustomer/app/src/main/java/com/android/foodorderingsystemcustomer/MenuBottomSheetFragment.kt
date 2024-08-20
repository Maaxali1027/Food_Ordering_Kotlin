package com.android.foodorderingsystemcustomer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.foodorderingsystemcustomer.adapters.MenuAdapter
import com.android.foodorderingsystemcustomer.databinding.FragmentMenuBottomSheetBinding
import com.android.foodorderingsystemcustomer.model.MenuItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenuBottomSheetBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBottomSheetBinding.inflate(inflater,container,false)

        binding.buttonBack.setOnClickListener{
            dismiss()
        }
        retrieveMenuItem()
        return binding.root

    }

    private fun retrieveMenuItem() {

        val db = FirebaseFirestore.getInstance()
        val menuItemsRef = db.collection("mainMenu")
        menuItems = mutableListOf()
        menuItemsRef.get()
            .addOnSuccessListener { result ->
                val menuItemList = mutableListOf<MenuItem>()
                for (document in result.documents) {
                    val docId = document.id
                    val data = document.data ?: continue  // Skip empty documents

                    val foodName = data["name"].toString()
                    val foodPrice = data["price"].toString()
                    val foodDescription = data["description"].toString()
                    val foodImage = data["image"].toString()
                    val ingredients = data.get("foodIngredient") as? List<*>
                    val foodIngredientList: List<String>? = ingredients?.map { it as String }

                    val menuItem = MenuItem(docId, foodName, foodPrice, foodDescription, foodImage, foodIngredientList)
                    menuItems.add(menuItem)
                }

                binding.menuProgressBar.visibility = View.GONE
                setAdapter()
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting menu items:", exception)
                Toast.makeText(requireContext(), "FireStore data Fetch Error: ${exception}", Toast.LENGTH_SHORT).show()
            }



    }

    private fun setAdapter() {
        val adapter = MenuAdapter(menuItems,requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
    }


}