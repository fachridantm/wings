package com.example.wings.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wings.data.repository.MainRepository
import com.example.wings.domain.model.Login
import com.example.wings.ui.common.StateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: MainRepository) : ViewModel() {
    private val _stateLogin: MutableStateFlow<StateHolder<Login>> =
        MutableStateFlow(StateHolder.Loading)
    val stateLogin: StateFlow<StateHolder<Login>>
        get() = _stateLogin

    fun loginUser(email: String, password: String) {
        _stateLogin.value = StateHolder.Loading
        viewModelScope.launch {
            repository.loginUser(email, password).collect {
                if (it.isLogin) {
                    _stateLogin.value = StateHolder.Success(it)
                } else {
                    _stateLogin.value = StateHolder.Error(it.message)
                }
            }
        }
    }
}

