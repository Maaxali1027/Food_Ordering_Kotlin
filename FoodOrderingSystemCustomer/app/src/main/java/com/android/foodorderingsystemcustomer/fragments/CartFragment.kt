package com.android.foodorderingsystemcustomer.fragments



import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.foodorderingsystemcustomer.activities.PayOutActivity
import com.android.foodorderingsystemcustomer.databinding.FragmentCartBinding
import com.android.foodorderingsystemcustomer.utility.DataHolderForCart
import com.dialiax.sweeto.adapter.CartAdapter
import com.dialiax.sweeto.model.CartItems
import com.google.android.gms.common.data.DataHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.DatabaseMetaData

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodImagesUri: MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var userId: String
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)

        populateCart()


        binding.proceedbutton.setOnClickListener {

            if (cartAdapter.itemCount == 0)
            {
                Toast.makeText(requireContext(), "You have nothing in cart", Toast.LENGTH_SHORT).show()
            }
            else{
                startActivity(Intent(requireContext(), PayOutActivity::class.java))

            }
        }
        return binding.root
    }

    private fun populateCart() {
        cartAdapter = CartAdapter(DataHolderForCart.cartItems.toMutableList())

        binding.cartRecyclerView.adapter = cartAdapter
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }


}