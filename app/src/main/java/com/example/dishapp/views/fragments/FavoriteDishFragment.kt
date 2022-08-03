package com.example.dishapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dishapp.application.DishApplication
import com.example.dishapp.databinding.FragmentFavoriteDishBinding
import com.example.dishapp.interfaces.CustomToolbar
import com.example.dishapp.model.entities.DishEntity
import com.example.dishapp.utils.Constants
import com.example.dishapp.viewmodel.DishViewModel
import com.example.dishapp.viewmodel.DishViewModelFactory
import com.example.dishapp.views.activities.MainActivity
import com.example.dishapp.views.adapters.DishAdapter
import java.util.concurrent.TimeUnit


class FavoriteDishFragment : Fragment(), DishAdapter.DishClickListener {

    private lateinit var binding: FragmentFavoriteDishBinding
    private lateinit var dishesList: List<DishEntity>
    private lateinit var dishAdapter: DishAdapter

    private val viewModel: DishViewModel by viewModels {
        DishViewModelFactory(
            (requireActivity().application as
                    DishApplication).dishRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteDishBinding.inflate(inflater, container, false)

        setToolbarLoad()
        initViews()
        updateRecyclerView()

        return binding.root
    }

    private fun setToolbarLoad() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FAVORITE_DISHES_FRAGMENT,
                "Favorites"
            )
        }
    }

    private fun initViews() {
        dishesList = ArrayList()

        viewModel.favoriteDishes().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.tvNoFavoriteDishesAdded.visibility = View.GONE
                dishAdapter.updateList(it)
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.tvNoFavoriteDishesAdded.visibility = View.VISIBLE
            }
        }
    }

    /**
     * showing list of all favorites dishes
     */
    private fun updateRecyclerView() {
        val recyclerView = binding.recyclerView

        dishAdapter = DishAdapter(this, dishesList, this)

        postponeEnterTransition(300, TimeUnit.MILLISECONDS)
        recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = dishAdapter
        }
    }

    override fun dishClicked(dish: DishEntity, view: View) {
        val extras = FragmentNavigatorExtras( view to "dish_big_image_${dish.id}")

        findNavController().navigate(
            FavoriteDishFragmentDirections.favoriteDishToDishDetails(dish),
            extras
        )
        if (activity is MainActivity) {
            (activity as MainActivity).hideBottomNavigationBar()
        }
    }

    override fun popUpEditClicked(dish: DishEntity) {

    }

    override fun popUpDeleteClicked(dish: DishEntity) {

    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            (activity as MainActivity).showBottomNavigationBar()
        }
    }
}