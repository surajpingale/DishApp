package com.example.dishapp.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dishapp.model.entities.DishEntity

@Database(entities = [DishEntity::class], version = 1)
abstract class DishDatabase : RoomDatabase() {

    abstract fun dishDao(): DishDao

}