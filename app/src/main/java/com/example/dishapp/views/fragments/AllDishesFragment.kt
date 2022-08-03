package com.example.dishapp.views.fragments

import android.app.AlertDialog
import android.content.Intent
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
import com.example.dishapp.R
import com.example.dishapp.application.DishApplication
import com.example.dishapp.databinding.FragmentAllDishesBinding
import com.example.dishapp.interfaces.CustomToolbar
import com.example.dishapp.model.entities.DishEntity
import com.example.dishapp.utils.Constants
import com.example.dishapp.viewmodel.DishViewModel
import com.example.dishapp.viewmodel.DishViewModelFactory
import com.example.dishapp.views.activities.AddActivity
import com.example.dishapp.views.activities.MainActivity
import com.example.dishapp.views.adapters.DishAdapter
import java.util.concurrent.TimeUnit


class AllDishesFragment : Fragment(), DishAdapter.DishClickListener {

    private lateinit var binding: FragmentAllDishesBinding

    private lateinit var dishesList: List<DishEntity>
    private lateinit var dishAdapter: DishAdapter

    private val viewModel: DishViewModel by viewModels {
        DishViewModelFactory((requireActivity().application as DishApplication).dishRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAllDishesBinding.inflate(inflater, container, false)

        setToolbarLoad()
        initViews()
        updateRecyclerView()

        return binding.root
    }

    private fun initViews() {
        dishesList = ArrayList()

        viewModel.getAllDishes().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.tvNoDishesAdded.visibility = View.GONE
                dishAdapter.updateList(it)
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.tvNoDishesAdded.visibility = View.VISIBLE
            }
        }
    }


    private fun setToolbarLoad() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(Constants.ALL_DISHES_FRAGMENT, "All Dishes")
        }
    }

    /**
     * showing list of all dishes
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
        val extras = FragmentNavigatorExtras(view to "dish_big_image_${dish.id}")

        findNavController().navigate(
            AllDishesFragmentDirections.allDishesToDishDetails(dish),
            extras
        )
        if (activity is MainActivity) {
            (activity as MainActivity).hideBottomNavigationBar()
        }
    }

    override fun popUpEditClicked(dish: DishEntity) {
        val intent = Intent(requireActivity(), AddActivity::class.java)
        intent.putExtra(Constants.INTENT_DISH_DETAILS, dish)
        startActivity(intent)
    }

    override fun popUpDeleteClicked(dish: DishEntity) {

        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setMessage(resources.getString(R.string.alert_dialog_message, dish.title))
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
        alertDialog.setPositiveButton(R.string.alert_yes) { dialogInterface, _ ->
            viewModel.delete(dish)
            dialogInterface.dismiss()
        }
        alertDialog.setNegativeButton(R.string.alert_no) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        alertDialog.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            (activity as MainActivity).showBottomNavigationBar()
        }
    }
}