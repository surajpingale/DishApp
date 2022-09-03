package com.example.dishapp.views.fragments

import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.dishapp.R
import com.example.dishapp.databinding.FragmentRandomDishBinding
import com.example.dishapp.interfaces.CustomToolbar
import com.example.dishapp.model.entities.DishEntity
import com.example.dishapp.model.entities.randomDish.Recipe
import com.example.dishapp.utils.Constants
import com.example.dishapp.utils.InternetAvailability
import com.example.dishapp.utils.Response
import com.example.dishapp.viewmodel.DishViewModel
import com.example.dishapp.viewmodel.DishViewModelFactory
import com.example.dishapp.viewmodel.RandomDishViewModel
import com.example.dishapp.viewmodel.RandomDishViewModelFactory
import com.example.dishapp.views.activities.MainActivity
import javax.inject.Inject


class RandomDishFragment : Fragment(), InternetAvailability.InternetListener {

    private lateinit var binding: FragmentRandomDishBinding

    private var favoriteDish = false
    private lateinit var internetReceiver: InternetAvailability
    private var isNetAvailable: Boolean? = null
    private var alreadyLoaded = false

    @Inject
    lateinit var dishViewModelFactory: DishViewModelFactory

    @Inject
    lateinit var randomDishViewModelFactory: RandomDishViewModelFactory

    private val dishViewModel: DishViewModel by viewModels {
        dishViewModelFactory
    }

    private val viewModel: RandomDishViewModel by viewModels {
        randomDishViewModelFactory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).activityComponent
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomDishBinding.inflate(inflater, container, false)

        setToolbarLoad()
        isInternetAvailable()
        initViewAndListeners()

        return binding.root
    }

    private fun isInternetAvailable() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        internetReceiver = InternetAvailability(this)
        requireActivity().registerReceiver(internetReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(internetReceiver)
    }

    private fun setToolbarLoad() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.RANDOM_DISH_FRAGMENT,
                "Random Dish"
            )
        }
    }

    override fun available() {
        if (isNetAvailable == null || isNetAvailable == false) {
            binding.tvOffline.visibility = View.GONE
            isNetAvailable = true
            if (!alreadyLoaded) {
                viewModel.getRandomDish()
            }
        }
    }

    override fun notAvailable() {
        if (isNetAvailable == null || isNetAvailable == true) {
            if (!alreadyLoaded) {
                binding.tvOffline.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.clDish.visibility = View.GONE
            }
            isNetAvailable = false
        }
    }

    private fun initViewAndListeners() {

        binding.swipeRefresh.setOnRefreshListener {

            if (isNetAvailable == true) {
                viewModel.getRandomDish()
                if (binding.swipeRefresh.isRefreshing) {
                    binding.swipeRefresh.isRefreshing = false
                    if (favoriteDish) {
                        favoriteDish = false
                        binding.ivFavorite.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireActivity(), R.drawable.ic_favorite_with_line
                            )
                        )
                    }
                }
                binding.tvOffline.visibility = View.GONE
            } else {
                if (binding.swipeRefresh.isRefreshing) {
                    binding.swipeRefresh.isRefreshing = false

                }
                binding.tvOffline.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.clDish.visibility = View.GONE
                alreadyLoaded = false
            }
        }

        viewModel.mutableData.observe(viewLifecycleOwner) { state ->

            when (state) {
                is Response.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.clDish.visibility = View.GONE
                }
                is Response.Error -> {
                    binding.clDish.visibility = View.GONE
                    if (state.errorMessage.equals(Constants.JAVA_SOCKET_TIMEOUT)) {
                        viewModel.getRandomDish()
                    }
                }
                is Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.clDish.visibility = View.VISIBLE
                    try {
                        val recipe = state.data!!
                        populateUI(recipe)
                        alreadyLoaded = true
                    } catch (e: Exception) {
                        e.localizedMessage
                    }
                }
            }
        }
    }

    private fun populateUI(recipe: Recipe) {

        var dishType = ""
        var ingredients = ""
        var directionToCook = ""
        var cookingTime = ""

        val getRecipe = recipe.recipes[0]

        Glide.with(requireActivity())
            .load(getRecipe.image)
            .into(binding.ivDishPhoto)
        binding.tvDishName.text = getRecipe.title

        if (getRecipe.dishTypes[0].isNotEmpty()) {
            binding.tvDishType.text = getRecipe.dishTypes[0]
            dishType = getRecipe.dishTypes[0]
        }

        binding.tvDishCategory.text = "Other"


        for (value in getRecipe.extendedIngredients) {
            if (ingredients.isEmpty()) {
                ingredients = value.original
            } else {
                ingredients = ingredients + ", \n" + value.original
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvDirectionsToCook.text = Html.fromHtml(
                getRecipe.instructions,
                Html.FROM_HTML_MODE_COMPACT
            )
            directionToCook = Html.fromHtml(
                getRecipe.instructions,
                Html.FROM_HTML_MODE_COMPACT
            ).toString()
        } else {
            binding.tvDirectionsToCook.text = Html.fromHtml(getRecipe.instructions)
            directionToCook = Html.fromHtml(
                getRecipe.instructions
            ).toString()
        }

        binding.tvDishIngredients.text = ingredients

        cookingTime = resources.getString(
            R.string.cooking_time,
            getRecipe.readyInMinutes.toString()
        )
        binding.tvTimeForCook.text = cookingTime


        binding.ivFavorite.setOnClickListener {

            if (!favoriteDish) {
                val dishEntity = DishEntity()
                dishEntity.image = getRecipe.image
                dishEntity.imageSource = Constants.IMAGE_SOURCE_ONLINE
                dishEntity.title = getRecipe.title
                dishEntity.type = dishType
                dishEntity.category = "Other"
                dishEntity.ingredients = ingredients
                dishEntity.cookingTime = cookingTime
                dishEntity.directionToCook = directionToCook
                dishEntity.favoriteDish = true
                favoriteDish = true
                viewModel.setFavoriteDish(favoriteDish)

                dishViewModel.insert(dishEntity)
                Toast.makeText(requireActivity(), "Downloaded", Toast.LENGTH_SHORT)
                    .show()
                binding.ivFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite
                    )
                )
            } else {
                Toast.makeText(
                    requireActivity(),
                    "You have already downloaded it.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }

        if (viewModel.getFavoriteDish()) {
            binding.ivFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(), R.drawable.ic_favorite
                )
            )
        } else {
            binding.ivFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(), R.drawable.ic_favorite_with_line
                )
            )
        }
    }

}