package com.example.wings.domain.model

data class ProductItem(
    val id: Long,
    val image: Int,
    val title: String,
    val desc: String,
    val price: Int,
    val disc: Double,
)
