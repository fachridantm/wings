package com.example.wings.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wings.data.repository.MainRepository
import com.example.wings.domain.model.Product
import com.example.wings.ui.common.StateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MainRepository) : ViewModel() {
    private val _stateHome: MutableStateFlow<StateHolder<List<Product>>> =
        MutableStateFlow(StateHolder.Loading)
    val stateHome: StateFlow<StateHolder<List<Product>>>
        get() = _stateHome

    fun getAllProduct() {
        viewModelScope.launch {
            repository.getAllProduct()
                .catch {
                    _stateHome.value = StateHolder.Error(it.message.toString())
                }
                .collect { product ->
                    _stateHome.value = StateHolder.Success(product)
                }
        }
    }

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun findProduct(newQuery: String) {
        viewModelScope.launch {
            _query.value = newQuery
            repository.findProduct(_query.value)
                .catch {
                    _stateHome.value = StateHolder.Error(it.message.toString())
                }
                .collect { product ->
                    _stateHome.value = StateHolder.Success(product)
                }
        }
    }
}