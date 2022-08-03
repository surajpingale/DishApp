package com.example.dishapp.views.activities

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dishapp.databinding.ActivityAddBinding
import com.example.dishapp.databinding.AddPhotoDialogBinding
import com.example.dishapp.databinding.LayoutAddActivityBinding
import com.example.dishapp.viewmodel.DishViewModel
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.dishapp.R
import com.example.dishapp.application.DishApplication
import com.example.dishapp.model.entities.DishEntity
import com.example.dishapp.utils.Constants
import com.example.dishapp.viewmodel.DishViewModelFactory

class AddActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddBinding

    private lateinit var layout: LayoutAddActivityBinding

    private val REQUEST_CODE = 1

    private lateinit var imagePath: String
    private var favoriteDish = false
    private var dishDetails: DishEntity? = null

    private val dishViewModel: DishViewModel by viewModels {
        DishViewModelFactory((application as DishApplication).dishRepository)
    }

    private val getImageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            binding.includeAddLayout.ivDishPhoto.setImageURI(uri)
            imagePath = uri.toString()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewAndListeners()
        if (intent.hasExtra(Constants.INTENT_DISH_DETAILS)) {
            dishDetails = intent.getParcelableExtra(Constants.INTENT_DISH_DETAILS)
        }
        setUpToolBar()

    }

    private fun setUpToolBar() {
        val toolbar = binding.includeToolbar.toolbar
        val toolbarTitle = binding.includeToolbar.toolbarTitle


        if (dishDetails != null) {
            toolbarTitle.text = resources.getString(R.string.title_edit_dish)
            loadDishDetails()
        } else {
            toolbarTitle.text = resources.getString(R.string.title_add_dish)
        }

        setSupportActionBar(toolbar)
        toolbar.showOverflowMenu()
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun initViewAndListeners() {
        layout = binding.includeAddLayout
        layout.ivAddPhoto.setOnClickListener(this)
        layout.btnAddDish.setOnClickListener(this)
        binding.includeToolbar.btnBackPressed.setOnClickListener(this)
        layout.ivFavorite.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            layout.ivAddPhoto.id -> {
                if (checkPermission()) {
                    showDialog()
                } else {
                    requestPermission()
                }

            }

            layout.ivFavorite.id -> {
                if (!favoriteDish) {
                    favoriteDish = true
                    layout.ivFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            this, R.drawable.ic_favorite
                        )
                    )

                } else {
                    favoriteDish = false
                    layout.ivFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            this, R.drawable.ic_favorite_with_line
                        )
                    )
                }
            }
            layout.btnAddDish.id -> {
                addAndUpdateDish()
            }

            binding.includeToolbar.btnBackPressed.id -> {
                onBackPressed()
            }
        }
    }

    private fun addAndUpdateDish() {

        var dishId = 0

        dishDetails?.let {
            if (it.id != 0) {
                dishId = it.id!!
            }
        }

        val title = binding.includeAddLayout.etTitle.text.toString()
        val type = binding.includeAddLayout.etType.text.toString()
        val category = binding.includeAddLayout.etCategory.text.toString()
        val ingredients = binding.includeAddLayout.etIngredients.text.toString()
        val cookingTimeInMin =
            binding.includeAddLayout.etCookingTimeInMinutes.text.toString()
        val directionToCook = binding.includeAddLayout.etDirectionToCook.text.toString()


        val dishEntity = DishEntity()
        dishEntity.image = imagePath
        dishEntity.imageSource = Constants.IMAGE_SOURCE_LOCAL
        dishEntity.title = title
        dishEntity.type = type
        dishEntity.category = category
        dishEntity.ingredients = ingredients
        dishEntity.cookingTime = cookingTimeInMin
        dishEntity.directionToCook = directionToCook
        dishEntity.favoriteDish = favoriteDish

        if (dishId == 0) {
            dishViewModel.insert(dishEntity)
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
        } else {
            dishEntity.id = dishId
            dishViewModel.update(dishEntity)
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
        }
        finish()
    }


    private fun showDialog() {
        val dialog = Dialog(this)
        val addPhotoBinding = AddPhotoDialogBinding.inflate(layoutInflater)
        dialog.setContentView(addPhotoBinding.root)

        // Setting width of custom dialog box
        dialog.window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.show()

        addPhotoBinding.ivCamera.setOnClickListener {
            Toast.makeText(this, "Camera Clicked", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        addPhotoBinding.ivGallery.setOnClickListener {
            Toast.makeText(this, "Gallery Clicked", Toast.LENGTH_SHORT).show()

            getImageFromGallery.launch("image/*")
            dialog.dismiss()
        }
    }

    private fun checkPermission(): Boolean {
        val isGranted =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return isGranted == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE
        )
    }

    private fun loadDishDetails() {
        val layout = binding.includeAddLayout

        dishDetails?.let {
            if (it.id != 0) {
                imagePath = it.image!!
                Glide.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(layout.ivDishPhoto)

                favoriteOrNot(it.favoriteDish!!)
                favoriteDish = it.favoriteDish!!
                layout.etTitle.setText(it.title)
                layout.etType.setText(it.type)
                layout.etCategory.setText(it.category)
                layout.etIngredients.setText(it.ingredients)
                layout.etCookingTimeInMinutes.setText(it.cookingTime)
                layout.etDirectionToCook.setText(it.directionToCook)

                layout.btnTvAdd.text = resources.getString(R.string.update_dish)
            }
        }
    }

    private fun favoriteOrNot(favorite: Boolean) {
        if (favorite) {
            layout.ivFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.ic_favorite
                )
            )

        } else {
            layout.ivFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.ic_favorite_with_line
                )
            )
        }
    }

//    private fun pickImage()
//    {
//        val imagePickerIntent = Intent(Intent.ACTION_PICK,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//
//        getImageFromGallery.launch(imagePickerIntent)
//    }
}