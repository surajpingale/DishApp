package com.example.dishapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dishapp.model.repository.DishRepository


class RandomDishViewModelFactory(private val dishRepository: DishRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RandomDishViewModel(dishRepository) as T
    }
}