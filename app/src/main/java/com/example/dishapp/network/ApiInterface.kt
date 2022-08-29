package com.example.dishapp.network

import com.example.dishapp.model.entities.randomDish.Recipe
import com.example.dishapp.utils.Constants
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET(Constants.API_ENDPOINTS)
    fun getRandomRecipe(
        @Query(Constants.API_KEY) apiKey :String,
        @Query(Constants.LIMIT_LICENSE) limitLicense :Boolean,
        @Query(Constants.TAGS) tags :String,
        @Query(Constants.NUMBER) number :Int
    ) : Observable<Recipe>

}