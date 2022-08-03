package com.example.dishapp.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "dish_table")
data class DishEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "image")
    var image: String? = null,
    @ColumnInfo(name = "image_source")
    var imageSource: String? = null,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "type")
    var type: String? = null,
    @ColumnInfo(name = "category")
    var category: String? = null,
    @ColumnInfo(name = "ingredients")
    var ingredients: String? = null,
    @ColumnInfo(name = "cookingTime")
    var cookingTime: String? = null,
    @ColumnInfo(name = "instructions")
    var directionToCook: String? = null,
    @ColumnInfo(name = "favorite_dish")
    var favoriteDish: Boolean? = null,
) : Parcelable
