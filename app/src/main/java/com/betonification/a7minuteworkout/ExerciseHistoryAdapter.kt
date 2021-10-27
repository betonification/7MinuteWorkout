package com.betonification.a7minuteworkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_history.view.*

class ExerciseHistoryAdapter(val items: ArrayList<String>, val context: Context) : RecyclerView.Adapter<ExerciseHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val llItemHistoryRow = view.llItemHistory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_history,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date : String = items[position]
        holder.llItemHistoryRow.tvItemHistoryID.text = (position + 1).toString()
        holder.llItemHistoryRow.tvItemHistory.text = date
    }

    override fun getItemCount(): Int {
        return items.size
    }
}