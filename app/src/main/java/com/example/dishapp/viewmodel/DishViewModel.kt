package com.example.dishapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.dishapp.model.entities.DishEntity
import com.example.dishapp.model.repository.DishRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DishViewModel @Inject constructor(
    private val dishRepository: DishRepository
) : ViewModel() {

    fun insert(dishEntity: DishEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dishRepository.insertDish(dishEntity)
        }
    }

    fun getAllDishes(): LiveData<List<DishEntity>> {
        return dishRepository.getAllDishes()
    }

    fun favoriteDishes(): LiveData<List<DishEntity>> {
        return dishRepository.favorites()
    }

    fun delete(dishEntity: DishEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dishRepository.deleteDish(dishEntity)
        }
    }

    fun update(dishEntity: DishEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dishRepository.updateDish(dishEntity)
        }
    }

}