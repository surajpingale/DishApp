package com.example.dishapp.di

import com.example.dishapp.views.activities.AddActivity
import com.example.dishapp.views.activities.MainActivity
import com.example.dishapp.views.fragments.AllDishesFragment
import com.example.dishapp.views.fragments.FavoriteDishFragment
import com.example.dishapp.views.fragments.RandomDishFragment
import dagger.Component
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create():ActivityComponent
    }

    fun inject(addActivity: AddActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(randomDishFragment: RandomDishFragment)
    fun inject(favoriteDishFragment: FavoriteDishFragment)
    fun inject(allDishesFragment: AllDishesFragment)



}