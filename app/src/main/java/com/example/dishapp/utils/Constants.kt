package com.example.dishapp.utils

object Constants {

    // Fragments
    const val ALL_DISHES_FRAGMENT = "AllDishesFragment"
    const val DISH_DETAILS_FRAGMENT = "DishDetailsFragment"
    const val FAVORITE_DISHES_FRAGMENT = "FavoriteDishesFragment"
    const val RANDOM_DISH_FRAGMENT = "RandomDishFragment"


    const val IMAGE_SOURCE_LOCAL = "Local"
    const val IMAGE_SOURCE_ONLINE = "Online"

    // parcelable dish details
    const val INTENT_DISH_DETAILS = "IntentDishDetails"


    // Api
    const val BASE_URL = "https://api.spoonacular.com/"
    const val API_KEY = "apiKey"
    const val API_ENDPOINTS = "recipes/random"
    const val LIMIT_LICENSE = "limitLicense"
    const val TAGS = "tags"
    const val NUMBER = "number"

    const val API_KEY_VALUE = ""
    const val LIMIT_LICENSE_VALUE = true
    const val TAGS_VALUE = "vegetarian"
    const val NUMBER_VALUE = 1

    // Exception
    const val JAVA_SOCKET_TIMEOUT = "java.net.SocketTimeoutException: timeout"

    // Notification
    const val NOTIFICATION_ID = "DishNotificationId"
    const val NOTIFICATION_NAME = "Dish"
    const val NOTIFICATION_CHANNEL = "DishNotificationChannel"

}