package com.example.wings.ui.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wings.data.repository.MainRepository
import com.example.wings.domain.model.CartState
import com.example.wings.ui.common.StateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val repository: MainRepository) : ViewModel() {
    private val _stateCart: MutableStateFlow<StateHolder<CartState>> =
        MutableStateFlow(StateHolder.Loading)
    val stateCart: StateFlow<StateHolder<CartState>>
        get() = _stateCart

    fun getAddedProduct() {
        viewModelScope.launch {
            _stateCart.value = StateHolder.Loading
            repository.getAddedProduct()
                .collect { products ->
                    val totalPrice =
                        products.sumOf { it.product.price * it.count }
                    _stateCart.value =
                        StateHolder.Success(CartState(products, totalPrice))
                }
        }
    }

    fun updateProduct(productId: Long, count: Int) {
        viewModelScope.launch {
            repository.updateProduct(productId, count)
                .collect { isUpdated ->
                    if (isUpdated) {
                        getAddedProduct()
                    }
                }
        }
    }
}