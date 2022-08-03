package com.example.dishapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dishapp.model.entities.DishEntity

@Dao
interface DishDao {

    @Insert
    suspend fun insertDish(dishEntity: DishEntity)

    @Query("SELECT * FROM dish_table")
    fun getAllDishes() : LiveData<List<DishEntity>>

    @Update
    suspend fun updateDish(dish :DishEntity)

    @Delete
    suspend fun deleteDish(dish: DishEntity)

    // giving favorite dishes
    @Query("SELECT * FROM dish_table WHERE favorite_dish = 1")
    fun getFavoriteDishes() : LiveData<List<DishEntity>>


}