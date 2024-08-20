package com.android.foodorderingsystemcustomer.model

import com.google.firebase.Timestamp


data class Orders(
    val customerName: String? = null,
    val tableNo: Int = 0,
    val items: List<String> = emptyList(),
    val itemQuantities : List<Int> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: String = "",
    val orderDate: Timestamp = Timestamp.now()
)
