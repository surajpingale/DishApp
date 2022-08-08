package com.example.dishapp.di

import android.content.Context
import com.example.dishapp.views.activities.AddActivity
import com.example.dishapp.views.activities.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
interface ApplicationComponent {

    fun inject(addActivity: AddActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context) : ApplicationComponent
    }
}