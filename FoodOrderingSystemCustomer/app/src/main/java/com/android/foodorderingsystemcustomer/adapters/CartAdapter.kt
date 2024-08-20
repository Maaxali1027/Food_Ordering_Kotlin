package com.dialiax.sweeto.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.foodorderingsystemcustomer.R
import com.android.foodorderingsystemcustomer.utility.DataHolderForCart
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference


class CartAdapter(
    private val selectedMenuItems: MutableList<com.android.foodorderingsystemcustomer.model.MenuItem>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    companion object {
        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemsReference: DatabaseReference
    }

    init {
        itemQuantities = IntArray(selectedMenuItems.size) { 1 } // Initialize with 1 for each item
        DataHolderForCart.selectedMenuItems = selectedMenuItems
        DataHolderForCart.itemQuantities = itemQuantities.toList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = selectedMenuItems.size

    inner class CartViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val itemName = view.findViewById<TextView>(R.id.cartFoodName)
        private val itemPrice = view.findViewById<TextView>(R.id.cartitemPrice)
        private val itemImage = view.findViewById<ImageView>(R.id.cartImage)
        private val itemQuantity = view.findViewById<TextView>(R.id.cartItemQuantity)
        private val btnPlus = view.findViewById<ImageButton>(R.id.plusbutton)
        private val btnMinus = view.findViewById<ImageButton>(R.id.minusButton)
        private val btnDelete = view.findViewById<ImageButton>(R.id.deleteButton)

        fun bind(position: Int) {
            val selectedMenuItem = selectedMenuItems[position]

            itemName.text = selectedMenuItem.foodName
            itemPrice.text = "€"+selectedMenuItem.foodPrice
            val imageUriString = selectedMenuItem.foodImage
            val uri = Uri.parse(imageUriString)

            Glide.with(view.context).load(uri).into(itemImage)

            itemQuantity.text = itemQuantities[position].toString()

            btnMinus.setOnClickListener {
                if (itemQuantities[position] > 1) {
                    itemQuantities[position]--
                    itemQuantity.text = itemQuantities[position].toString()
                    DataHolderForCart.itemQuantities = itemQuantities.toList()
                }
            }

            btnPlus.setOnClickListener {
                if (itemQuantities[position] < 10) {
                    itemQuantities[position]++
                    itemQuantity.text = itemQuantities[position].toString()
                    DataHolderForCart.itemQuantities = itemQuantities.toList()
                }
            }

            btnDelete.setOnClickListener {
                val itemPosition = adapterPosition
                if (itemPosition != RecyclerView.NO_POSITION) {
                    deleteItem(position)
                }
            }
        }
    }

    private fun deleteItem(position: Int) {
        if (position < 0 || position >= selectedMenuItems.size) {
            return
        }
        DataHolderForCart.cartItems.remove(selectedMenuItems[position])
        selectedMenuItems.removeAt(position)
        itemQuantities = itemQuantities.filterIndexed { index, _ -> index != position }.toIntArray()
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, selectedMenuItems.size)

        DataHolderForCart.apply {
            selectedMenuItems = selectedMenuItems
            itemQuantities = itemQuantities.toList()
        }
    }
}
