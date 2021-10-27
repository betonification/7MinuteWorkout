package com.betonification.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bmiactivity.*
import kotlinx.android.synthetic.main.activity_bmiactivity.toolbar_bmi_activity
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_select_exercises.*

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setSupportActionBar(toolbar_history_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "EXERCISE HISTORY"

        toolbar_history_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        getAllCompletedDates()
    }

    // funkcija za dobijanje liste datuma iz baze i postavljanje recyclerView-a
    private fun getAllCompletedDates(){
        val dbHandler = SqliteOpenHelper(this,null)
        val allCompletedDatesList = dbHandler.getAllCompletedDatesList()

        //ukoliko jos uvek nema upisa u bazu, ne pojavljuje se recycler view, vec textview sa obavestenjem
        if(allCompletedDatesList.isNotEmpty()){
            rvExerciseHistory.visibility = View.VISIBLE
            tvNoEntriesYet.visibility = View.GONE
            rvExerciseHistory.layoutManager = LinearLayoutManager(this)
            val exerciseHistoryAdapter = ExerciseHistoryAdapter(allCompletedDatesList,this)
            rvExerciseHistory.adapter = exerciseHistoryAdapter
        }else{
            tvNoEntriesYet.visibility = View.VISIBLE
            rvExerciseHistory.visibility = View.GONE
        }
    }
}