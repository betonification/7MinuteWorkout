package com.betonification.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.betonification.a7minuteworkout.MainActivity.Companion.selectedExercisesList
import kotlinx.android.synthetic.main.activity_select_exercises.*

class SelectExercisesActivity : AppCompatActivity() {

    private var selectExercisesAdapter: SelectExercisesAdapter? = null
    private var selectedAll = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_exercises)

        setSupportActionBar(toolbar_select_exercises_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "SELECT EXERCISES"

        setupSelectExercisesRecyclerView()

        toolbar_select_exercises_activity.setNavigationOnClickListener {
            onBackPressed()
        }
        btnOk.setOnClickListener {
            finish()
        }
        btnSelectAll.setOnClickListener {
            if (!selectedAll){
                for(item in selectedExercisesList!!){
                    item.setIsSelected(true)
                }
                selectedAll = true
            }else{
                for(item in selectedExercisesList!!){
                    item.setIsSelected(false)
                }
                selectedAll = false
            }
            selectExercisesAdapter?.notifyDataSetChanged()
        }
    }

    private fun setupSelectExercisesRecyclerView(){
        rvSelectExercises.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        selectExercisesAdapter = SelectExercisesAdapter(selectedExercisesList!!,this)

        rvSelectExercises.adapter = selectExercisesAdapter
    }
}