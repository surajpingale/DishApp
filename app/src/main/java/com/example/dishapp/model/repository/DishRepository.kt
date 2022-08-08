package com.example.dishapp.model.repository

import androidx.lifecycle.LiveData
import com.example.dishapp.network.ApiInterface
import com.example.dishapp.model.entities.randomDish.Recipe
import com.example.dishapp.model.database.DishDao
import com.example.dishapp.model.entities.DishEntity
import com.example.dishapp.utils.Constants
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import javax.inject.Inject

class DishRepository @Inject constructor(
    private val dishDao: DishDao,
    private val apiInterface: ApiInterface
) {

    suspend fun insertDish(dishEntity: DishEntity) {
        dishDao.insertDish(dishEntity)
    }

    fun getAllDishes(): LiveData<List<DishEntity>> {
        return dishDao.getAllDishes()
    }

    fun favorites(): LiveData<List<DishEntity>> {
        return dishDao.getFavoriteDishes()
    }

    suspend fun deleteDish(dish: DishEntity) {
        return dishDao.deleteDish(dish)
    }

    suspend fun updateDish(dish: DishEntity) {
        return dishDao.updateDish(dish)
    }

    fun getRandomDish(): Observable<Recipe> {

        return apiInterface.getRandomRecipe(
            Constants.API_KEY_VALUE,
            Constants.LIMIT_LICENSE_VALUE,
            Constants.TAGS_VALUE,
            Constants.NUMBER_VALUE
        )
    }


}