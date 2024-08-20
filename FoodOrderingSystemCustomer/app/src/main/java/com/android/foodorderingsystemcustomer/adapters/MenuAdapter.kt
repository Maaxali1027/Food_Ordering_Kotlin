package com.android.foodorderingsystemcustomer.adapters


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.foodorderingsystemcustomer.R
import com.android.foodorderingsystemcustomer.activities.DetailsActivity
import com.android.foodorderingsystemcustomer.model.MenuItem
import com.android.foodorderingsystemcustomer.utility.DataHolderForCart
import com.bumptech.glide.Glide


class MenuAdapter(
    private var menuItems:List<MenuItem>,
    private val requiredContext : Context
): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItems.size


    fun updateMenu(menuItems : List<MenuItem>){
        this.menuItems = menuItems
        notifyDataSetChanged()
    }

    inner class MenuViewHolder(view : View): RecyclerView.ViewHolder(view) {

        val menuItemName = view.findViewById<TextView>(R.id.menuFoodName)
        val menuItemPrice = view.findViewById<TextView>(R.id.menuPrice)
        val menuItemImage = view.findViewById<ImageView>(R.id.menuImage)
        val btnAddToCart = view.findViewById<Button>(R.id.btnAddtocart)

        init {
           view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    openDetailActivity(menuItems[adapterPosition].docId)
                }
            }

            btnAddToCart.setOnClickListener {
                Toast.makeText(view.context, "Added to Cart", Toast.LENGTH_SHORT).show()
                DataHolderForCart.cartItems.add(menuItems[adapterPosition])
            }
        }

        // set data into recyclerview item name, price, image
        fun bind(position: Int) {
            val menuItem = menuItems[position]

                    menuItemName.text = menuItem.foodName
                    menuItemPrice.text = "€"+menuItem.foodPrice
                    val uri = Uri.parse(menuItem.foodImage)
                    Glide.with(requiredContext).load(menuItem.foodImage)
                        .error(R.drawable.menu1)
                        .into(menuItemImage)
                }
        }
    private fun openDetailActivity(id : String) {

        // intent to open detail activity and pass data
        val intent = Intent(requiredContext, DetailsActivity::class.java).apply {
            putExtra("menuItemId",id)
        }
        //start the detail activityf
        requiredContext.startActivity(intent)
    }

    }


