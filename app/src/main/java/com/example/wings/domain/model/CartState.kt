package com.example.wings.domain.model

data class CartState(
    val product: List<Product>,
    val totalPrice: Int,
)