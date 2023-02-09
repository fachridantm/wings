package com.example.wings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wings.data.repository.MainRepository
import com.example.wings.ui.screen.cart.CartViewModel
import com.example.wings.ui.screen.detail.DetailProductViewModel
import com.example.wings.ui.screen.home.HomeViewModel
import com.example.wings.ui.screen.login.LoginViewModel

class ViewModelFactory(private val repository: MainRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailProductViewModel::class.java)) {
            return DetailProductViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}