package com.example.wings.ui.screen.detail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wings.data.repository.MainRepository
import com.example.wings.domain.model.Product
import com.example.wings.domain.model.ProductItem
import com.example.wings.ui.common.StateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailProductViewModel(private val repository: MainRepository) : ViewModel() {
    private val _stateDetail: MutableStateFlow<StateHolder<Product>> =
        MutableStateFlow(StateHolder.Loading)
    val stateDetail: StateFlow<StateHolder<Product>>
        get() = _stateDetail

    fun getProductById(productId: Long) {
        viewModelScope.launch {
            _stateDetail.value = StateHolder.Loading
            _stateDetail.value = StateHolder.Success(repository.getProductById(productId))
        }
    }

    fun addToCart(product: ProductItem, count: Int) {
        viewModelScope.launch {
            repository.updateProduct(product.id, count)
        }
    }
}