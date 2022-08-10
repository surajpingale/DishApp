package com.example.dishapp.application

import android.app.Application
import com.example.dishapp.di.ApplicationComponent
import com.example.dishapp.di.DaggerApplicationComponent
import com.example.dishapp.model.database.DishDatabase
import com.example.dishapp.model.repository.DishRepository

class DishApplication : Application() {

    lateinit var applicationComponent : ApplicationComponent

    // This called when only it requires by using lazy
//    private val dishDao by lazy {
//        val database = DishDatabase.getInstance(this)
//        database.dishDao()
//    }
//
//    private val apiInterface by lazy {
//        Network.getRetrofit()
//    }

//    val dishRepository by lazy {
//        DishRepository(dishDao, apiInterface)
//    }

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}