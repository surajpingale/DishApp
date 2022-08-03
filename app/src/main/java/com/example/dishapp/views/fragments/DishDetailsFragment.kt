package com.example.dishapp.views.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.dishapp.R
import com.example.dishapp.databinding.FragmentDishDetailsBinding
import com.example.dishapp.interfaces.CustomToolbar
import com.example.dishapp.utils.Constants

class DishDetailsFragment : Fragment() {

    private val args: DishDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentDishDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDishDetailsBinding.inflate(inflater, container, false)

        setToolbarLoad()
        initViewsAndListeners()

        return binding.root
    }

    private fun setToolbarLoad() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.DISH_DETAILS_FRAGMENT,
                "Details"
            )
        }
    }

    private fun initViewsAndListeners() {
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )

        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.dishDetails.image)
                    .into(binding.ivDishPhoto)
            } catch (e: Exception) {
                e.localizedMessage
            }

            binding.ivDishPhoto.transitionName = "dish_big_image_${args.dishDetails.id}"
            binding.tvDishName.text = args.dishDetails.title
            binding.tvDishType.text = args.dishDetails.type
            binding.tvDishCategory.text = args.dishDetails.category
            binding.tvDishIngredients.text = args.dishDetails.ingredients
            binding.tvDirectionsToCook.text = args.dishDetails.directionToCook
            binding.tvTimeForCook.text = args.dishDetails.cookingTime

            val favorite = args.dishDetails.favoriteDish
            if (favorite == true) {
                binding.ivFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite
                    )
                )
            } else {
                binding.ivFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_with_line
                    )
                )
            }
        }
    }
}