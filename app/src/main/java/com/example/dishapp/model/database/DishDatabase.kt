package com.example.dishapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dishapp.model.entities.DishEntity
import com.example.dishapp.utils.Constants

@Database(entities = [DishEntity::class], version = 1)
abstract class DishDatabase : RoomDatabase() {

    abstract fun dishDao(): DishDao

//    companion object {
//
//        private var DATABASE_INSTANCE: DishDatabase? = null
//
//        fun getInstance(context: Context): DishDatabase {
//            if (DATABASE_INSTANCE == null) {
//                DATABASE_INSTANCE = Room.databaseBuilder(
//                    context.applicationContext,
//                    DishDatabase::class.java,
//                    Constants.DATABASE_NAME
//                ).fallbackToDestructiveMigration().build()
//            }
//            return DATABASE_INSTANCE!!
//        }
//    }


}