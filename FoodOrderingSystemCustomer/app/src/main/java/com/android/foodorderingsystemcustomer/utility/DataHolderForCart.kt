package com.android.foodorderingsystemcustomer.utility

import com.android.foodorderingsystemcustomer.model.MenuItem

class DataHolderForCart {
    companion object {
        var cartItems: MutableSet<MenuItem> = mutableSetOf()

        // confirmed
        var selectedMenuItems: List<MenuItem> = listOf()
        var itemQuantities: List<Int> = listOf()

        // Function to calculate total price
        fun calculateTotalPrice(): Double {
            var totalPrice = 0.0
            for (i in selectedMenuItems.indices) {
                val price = selectedMenuItems[i].foodPrice?.toDoubleOrNull() ?: 0.0
                val quantity = itemQuantities.getOrNull(i) ?: 0
                totalPrice += price * quantity
            }
            return totalPrice
        }

        fun getMenuIds(): List<String> {
            return selectedMenuItems.map { it.docId }
        }

    }
}
