package com.example.dishapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dishapp.model.entities.randomDish.Recipe
import com.example.dishapp.model.repository.DishRepository
import com.example.dishapp.utils.Response
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


class RandomDishViewModel @Inject constructor(private val dishRepository: DishRepository) : ViewModel() {


    private val disposable = CompositeDisposable()

    var mutableData = MutableLiveData<Response<Recipe>>()

    private var isFavorite = false




    fun getRandomDish() {

        mutableData.postValue(Response.Loading())

        disposable.add(
            getObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ recipe ->
                    getObserver(recipe)
                }, { t ->
                    onFailure(t)
                })
        )
    }

    private fun getObservable(): Observable<Recipe> {
        return dishRepository.getRandomDish()
    }

    private fun getObserver(response: Recipe) {
        if (response != null && response.recipes.isNotEmpty()) {
            mutableData.postValue(Response.Success(response))
        }
    }

    private fun onFailure(t: Throwable) {
        mutableData.postValue(Response.Error(t.toString()))
    }

    fun setFavoriteDish(favorite:Boolean)
    {
        this.isFavorite = favorite
    }

    fun getFavoriteDish() : Boolean
    {
        return isFavorite
    }



}