package com.example.dishapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dishapp.model.database.DishDatabase
import com.example.dishapp.utils.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun getDatabaseInstance(context : Context) : DishDatabase {

        return Room.databaseBuilder(
            context,
            DishDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration().build()

    }

}