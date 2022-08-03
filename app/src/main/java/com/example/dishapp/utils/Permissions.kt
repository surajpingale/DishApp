package com.example.dishapp.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Permissions {

    private lateinit var permissions : Array<String>

    fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) {
        this.permissions = permissions

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {


            } catch (e: Exception) {

            }
        } else {
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        }

    }

//    fun checkPermissions(activity: Activity)
//    {
//        for (permission in permissions)
//        {
//            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
//        }
//    }


}