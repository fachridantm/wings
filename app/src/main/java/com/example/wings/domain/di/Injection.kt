package com.example.wings.domain.di

import com.example.wings.data.repository.MainRepository

object Injection {
    fun provideRepository(): MainRepository {
        return MainRepository.getInstance()
    }
}