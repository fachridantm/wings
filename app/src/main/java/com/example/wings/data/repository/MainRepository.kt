package com.example.wings.data.repository

import com.example.wings.data.source.local.DummyData
import com.example.wings.domain.model.Login
import com.example.wings.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MainRepository {

    private val listProduct = mutableListOf<Product>()

    init {
        if (listProduct.isEmpty()) {
            DummyData.dummyList.forEach {
                listProduct.add(Product(it, 0))
            }
        }
    }

    fun getAllProduct(): Flow<List<Product>> {
        return flowOf(listProduct)
    }

    fun getProductById(productId: Long): Product {
        return listProduct.first {
            it.product.id == productId
        }
    }

    fun updateProduct(productId: Long, newCountValue: Int): Flow<Boolean> {
        val index = listProduct.indexOfFirst { it.product.id == productId }
        val result = if (index >= 0) {
            val products = listProduct[index]
            listProduct[index] = products.copy(product = products.product, count = newCountValue)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    fun getAddedProduct(): Flow<List<Product>> {
        return getAllProduct()
            .map { list ->
                list.filter { products ->
                    products.count != 0
                }
            }
    }

    fun findProduct(query: String): Flow<List<Product>> {
        return getAllProduct()
            .map { list ->
                list.filter {
                    it.product.title.contains(query, ignoreCase = true)
                }
            }
    }

    fun loginUser(email: String, password: String): Flow<Login> {
        val result = if (email == "admin@gmail.com" && password == "admin") {
            Login(email, password, true, "Login success")
        } else {
            Login(email, password,false, "Email or password is wrong")
        }
        return flowOf(result)
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance(): MainRepository =
            instance ?: synchronized(this) {
                MainRepository().apply {
                    instance = this
                }
            }
    }

}