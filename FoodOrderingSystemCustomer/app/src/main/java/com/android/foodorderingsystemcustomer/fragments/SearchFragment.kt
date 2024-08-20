package com.android.foodorderingsystemcustomer.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.foodorderingsystemcustomer.adapters.MenuAdapter
import com.android.foodorderingsystemcustomer.databinding.FragmentSearchBinding
import com.android.foodorderingsystemcustomer.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private lateinit var database: FirebaseDatabase
    private val originalMenuItems = mutableListOf<MenuItem>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        //retrieve menu item from database
        retrieveMenuItem()

        //SETUP FOR SEARCHVIEW
        setupSearchView()

        return binding.root
    }

    private fun retrieveMenuItem() {
        val db = FirebaseFirestore.getInstance()
        val menuItemsRef = db.collection("mainMenu")


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
                    originalMenuItems.add(menuItem)
                }
                showAllMenu()
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting menu items:", exception)
                Toast.makeText(requireContext(), "FireStore data Fetch Error: ${exception}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showAllMenu() {
        val filteredMenuItem = ArrayList(originalMenuItems)
        setAdapter(filteredMenuItem)
    }

    private fun setAdapter(filteredMenuItem: List<MenuItem>) {
        adapter = MenuAdapter(filteredMenuItem, requireContext())
        binding.rvSearchMenuItem.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchMenuItem.adapter = adapter
    }


    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true // Add return statement here
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true // Add return statement here
            }
        })
    }


    private fun filterMenuItems(query: String) {
        val filteredMenuItems = originalMenuItems.filter {
            it.foodName?.contains(query, ignoreCase = true) == true
        }
        setAdapter(filteredMenuItems)
        adapter.notifyDataSetChanged()
    }

    companion object {

    }
}