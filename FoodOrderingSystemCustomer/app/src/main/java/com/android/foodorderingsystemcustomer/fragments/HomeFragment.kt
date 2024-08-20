package com.android.foodorderingsystemcustomer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.foodorderingsystemcustomer.adapters.MenuAdapter
import com.android.foodorderingsystemcustomer.databinding.FragmentHomeBinding
import com.android.foodorderingsystemcustomer.model.MenuItem
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems:MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewAllMenu.setOnClickListener {
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }

        //retrieve and display popular item
        retrieveAndDisplayPopularItems()

        return binding.root
    }

    private fun retrieveAndDisplayPopularItems() {
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

                Log.d("Firestorebjkbhjhgbjhhgjk", "Data From FireBase: ${menuItems}" )

//                randomPopularItems()
                binding.menuProgressBar.visibility = View.GONE
                setPopularItemsAdapter(menuItems)
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting menu items:", exception)
                Toast.makeText(requireContext(), "FireStore data Fetch Error: ${exception}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun randomPopularItems() {
        //create as shuffled list of menu items
        val index = menuItems.indices.toList().shuffled()
        val numItemToShow = 6
        val subsetMenuItems = index.take(numItemToShow).map { menuItems[it] }

        setPopularItemsAdapter(subsetMenuItems)
    }

    private fun setPopularItemsAdapter(subsetMenuItems: List<MenuItem>) {
        val adapter = MenuAdapter(subsetMenuItems,requireContext())
        binding.PopularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.PopularRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}
