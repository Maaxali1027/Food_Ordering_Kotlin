package com.android.foodorderingsystemcustomer.activities


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.foodorderingsystemcustomer.CongratsBottomSheet
import com.android.foodorderingsystemcustomer.databinding.ActivityPayOutBinding
import com.android.foodorderingsystemcustomer.model.Orders
import com.android.foodorderingsystemcustomer.utility.DataHolderForCart
import com.google.firebase.firestore.FirebaseFirestore

class PayOutActivity : AppCompatActivity() {

     lateinit var binding: ActivityPayOutBinding
     private lateinit var listMenuItemsIds: List<String>
     private lateinit var itemQuantities: List<Int>
     val TAG =  "Order Insertion Testing"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemQuantities = DataHolderForCart.itemQuantities
        Log.d(TAG, "Quantities: ${itemQuantities}")


        DataHolderForCart.apply {
            listMenuItemsIds = getMenuIds()
            Log.d(TAG, "Quantities: ${itemQuantities}")

        }


        val totalPrice = DataHolderForCart.calculateTotalPrice()
        binding.tvTotalAmount.text = "€"+totalPrice.toString()

        binding.placeMyOrder.setOnClickListener {
            val name = binding.name.text.toString()
            val tableNo = binding.etTableNo.text.toString()

            if (name.isNotEmpty() && tableNo.isNotEmpty())
            {
                val order = Orders(name,tableNo.toInt(),listMenuItemsIds,itemQuantities,totalPrice,"pending")
                placeOrder(order)

                Toast.makeText(this, "Placing Order, Please wait", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Name and Table No cannot be empty", Toast.LENGTH_SHORT).show()
            }


        }



        }

    fun placeOrder(order: Orders) {
        val db = FirebaseFirestore.getInstance()
        val ordersCollection = db.collection("orders")

        // Add a new document with a generated ID
        ordersCollection
            .add(order)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Order placed with ID: ${documentReference.id}")
                DataHolderForCart.cartItems = mutableSetOf()
                CongratsBottomSheet().show(supportFragmentManager, CongratsBottomSheet().tag)
                // Handle success (optional)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding order", e)
                // Handle failures (optional)
            }
    }

}





