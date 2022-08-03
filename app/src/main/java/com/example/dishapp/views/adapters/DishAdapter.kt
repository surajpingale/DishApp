package com.example.dishapp.views.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dishapp.R
import com.example.dishapp.databinding.DishItemBinding
import com.example.dishapp.model.entities.DishEntity
import com.example.dishapp.utils.DishDiffUtils
import com.example.dishapp.views.fragments.AllDishesFragment

class DishAdapter(
    private val fragment: Fragment,
    private var dishesList: List<DishEntity>,
    private val dishClickListener: DishClickListener
) :
    RecyclerView.Adapter<DishAdapter.DishViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DishItemBinding.inflate(layoutInflater, parent, false)
        return DishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishesList[position]

        holder.binding.apply {
            rvIvDishPhoto.transitionName = "small_dish_image_${dish.id}"
            rvTvDishName.text = dish.title
            ivPopUp.setOnClickListener {
                popUpInflater(
                    fragment.context!!, holder.binding.ivPopUp,
                    dish
                )
            }
        }

        Glide.with(holder.itemView.context)
            .load(dish.image)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.binding.rvIvDishPhoto)

        holder.itemView.setOnClickListener {
            dishClickListener.dishClicked(dish, holder.binding.rvIvDishPhoto)
        }

        if (fragment is AllDishesFragment) {
            holder.binding.ivPopUp.visibility = View.VISIBLE
        } else {
            holder.binding.ivPopUp.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return dishesList.size
    }

    fun updateList(newDishesList: List<DishEntity>) {
        val diffUtil = DishDiffUtils(dishesList, newDishesList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        dishesList = newDishesList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class DishViewHolder(itemView: DishItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: DishItemBinding

        init {
            binding = itemView
        }
    }

    interface DishClickListener {
        fun dishClicked(dish: DishEntity, view: View)
        fun popUpEditClicked(dish: DishEntity)
        fun popUpDeleteClicked(dish: DishEntity)
    }

    private fun popUpInflater(
        context: Context, ivPopUp: View,
        dish: DishEntity
    ) {
        val popupMenu = PopupMenu(context, ivPopUp)
        popupMenu.menuInflater.inflate(R.menu.pop_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->

            if (menuItem.itemId == R.id.menu_edit) {
                dishClickListener.popUpEditClicked(dish)
            } else {
                dishClickListener.popUpDeleteClicked(dish)
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }
}