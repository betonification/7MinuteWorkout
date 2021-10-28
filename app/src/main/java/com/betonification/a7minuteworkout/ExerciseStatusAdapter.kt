package com.betonification.a7minuteworkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_status_of_exercise.view.*

class ExerciseStatusAdapter(val items: ArrayList<ExerciseModel>, val context: Context) : RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>(){

    class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val tvItem = view.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_status_of_exercise,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: ExerciseModel = items[position]

        holder.tvItem.text = item.getId().toString()

        // u zavisnosti od toga da li je vezbanje zavrseno, traje ili jos uvek nije doslo na red menja se izgled tvItem view-a
        // status vezbanja se menja u onFinish metodi exerciseTimer objekta kreiranog u exerciseCountDown funkciji
        when{
            item.getIsOngoing() -> {
                holder.tvItem.background = ContextCompat.getDrawable(context,R.drawable.item_circular_color_accent_border_thin)
                holder.tvItem.setTextColor(ContextCompat.getColor(context,R.color.black))
            }
            item.getIsCompleted() -> {
                holder.tvItem.background = ContextCompat.getDrawable(context,R.drawable.item_circular_color_accent_background)
                holder.tvItem.setTextColor(ContextCompat.getColor(context,R.color.white))
            }else -> {
            holder.tvItem.background = ContextCompat.getDrawable(context,R.drawable.item_circular_background_grey)
            holder.tvItem.setTextColor(ContextCompat.getColor(context,R.color.black))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}