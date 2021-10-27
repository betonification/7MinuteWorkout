package com.betonification.a7minuteworkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_select_exercise.view.*

class SelectExercisesAdapter (val items: ArrayList<ExerciseModel>, val context: Context) : RecyclerView.Adapter<SelectExercisesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val llItemSelectExercise = view.llItemSelectExercise
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_select_exercise,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: ExerciseModel = items[position]

        holder.llItemSelectExercise.tvExerciseNumber.text = item.getId().toString() + "."
        holder.llItemSelectExercise.tvExerciseNameSelect.text = item.getName()
        holder.llItemSelectExercise.cbSelectExercise.isChecked = item.getIsSelected()

        holder.llItemSelectExercise.cbSelectExercise.setOnCheckedChangeListener { _, isChecked ->
            when{
                isChecked -> {
                    item.setIsSelected(true)
                }else -> {
                    item.setIsSelected(false)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}