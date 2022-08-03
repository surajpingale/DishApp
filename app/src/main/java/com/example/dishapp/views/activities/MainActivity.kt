package com.example.dishapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.dishapp.R
import com.example.dishapp.databinding.ActivityMainBinding
import com.example.dishapp.interfaces.CustomToolbar
import com.example.dishapp.notification.NotificationWorker
import com.example.dishapp.utils.Constants
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), CustomToolbar {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingBottomNavBar()

        if (intent.hasExtra(Constants.NOTIFICATION_ID)) {
            intent.getIntExtra(Constants.NOTIFICATION_ID, 0)
            binding.bottomNavView.selectedItemId = R.id.random_dishes_fragment
        }

        startWork()
    }

    private fun createConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresCharging(false)
        .setRequiresBatteryNotLow(true)
        .build()

    private fun createWorkRequest() =
        PeriodicWorkRequestBuilder<NotificationWorker>(
            15,
            TimeUnit.MINUTES
        ).setConstraints(createConstraints()).build()

    private fun startWork() {
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "Dish Notification",
                ExistingPeriodicWorkPolicy.KEEP,
                createWorkRequest()
            )
    }

    fun showBottomNavigationBar() {
        binding.bottomNavView.clearAnimation()
        binding.bottomNavView.animate().translationY(0f).duration = 300
        binding.bottomNavView.visibility = View.VISIBLE

    }

    fun hideBottomNavigationBar() {
        binding.bottomNavView.clearAnimation()
        binding.bottomNavView.animate().translationY(binding.bottomNavView.height.toFloat())
            .duration = 300

        binding.bottomNavView.visibility = View.GONE

    }

    private fun settingBottomNavBar() {

        val navHost = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id)
                as NavHostFragment
        binding.bottomNavView.setupWithNavController(navHost.navController)

    }

    override fun onToolbarLoad(from: String, title: String) {
        val toolbar = binding.include.toolbar

        when (from) {
            Constants.ALL_DISHES_FRAGMENT -> {
                binding.include.btnBackPressed.visibility = View.GONE
                binding.include.toolbarTitle.text = title
            }
            Constants.DISH_DETAILS_FRAGMENT -> {
                binding.include.btnBackPressed.visibility = View.VISIBLE
                binding.include.toolbarTitle.text = title
            }
            Constants.FAVORITE_DISHES_FRAGMENT -> {
                binding.include.btnBackPressed.visibility = View.GONE
                binding.include.toolbarTitle.text = title
            }
            Constants.RANDOM_DISH_FRAGMENT -> {
                binding.include.btnBackPressed.visibility = View.GONE
                binding.include.toolbarTitle.text = title
            }
        }

        binding.include.btnBackPressed.setOnClickListener {
            onBackPressed()
        }

        setSupportActionBar(toolbar)
        toolbar.showOverflowMenu()
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.menu_add -> {
                val intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

