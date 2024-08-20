package com.android.foodorderingsystemcustomer.activities

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.foodorderingsystemcustomer.databinding.ActivityDetailsBinding
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore


class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodDescription: String? = null
    private var foodImage: String? = null
    private var foodIngredient: String? = null
    private lateinit var auth: Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemId = intent.getStringExtra("menuItemId")
        
        getMenuFromCloud(itemId)

        binding.imageButton2.setOnClickListener {
            finish()
        }
        binding.addtocartbutton.setOnClickListener {
//            addItemToCart()
        }

    }

    private fun getMenuFromCloud(itemId: String?) {
        if (itemId == null) {
            // Handle the case where itemId is null
            return
        }

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("mainMenu").document(itemId)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    foodDescription = document.getString("description")
                    foodImage = document.getString("image")
                    foodIngredient = (document.get("ingredients") as? List<*>).toString()
                    foodName = document.getString("name")
                    foodPrice = document.getString("price")

                    populateData()
                } else {
                    println("No such document")
                }
            }
            .addOnFailureListener { exception ->
                println("get failed with $exception")
            }
    }

    private fun populateData() {
        binding.detailFoodName.text = foodName
        binding.detailDescription.text = foodDescription
        Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(binding.detailFoodImage)
    }


}
