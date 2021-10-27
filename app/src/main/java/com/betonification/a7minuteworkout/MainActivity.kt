package com.betonification.a7minuteworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        var exerciseList: ArrayList<ExerciseModel>? = null
        var selectedExercisesList: ArrayList<ExerciseModel>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exerciseList = Constants.defaultExerciseList()

        selectedExercisesList = exerciseList

        llStart.setOnClickListener {
            if (checkIfAnyExerciseIsSelected()){
                val exerciseIntent = Intent(this, ExerciseActivity::class.java )
                startActivity(exerciseIntent)
            }else{
                Toast.makeText(this, "Please select at least one exercise", Toast.LENGTH_SHORT).show()
            }
        }

        llSelectExercises.setOnClickListener {
            val intent = Intent(this,SelectExercisesActivity::class.java)
            startActivity(intent)
        }

        llBMI.setOnClickListener {
            val intent = Intent(this,BMIActivity::class.java)
            startActivity(intent)
        }

        llHistory.setOnClickListener {
            val intent = Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    //funkcija koja proverava da li je neko vezbanje izabrano
    private fun checkIfAnyExerciseIsSelected() : Boolean{
        var isSomethingSelected = false
        for(item in selectedExercisesList!!){
            if (item.getIsSelected()){
                isSomethingSelected = true
            }
        }
        return isSomethingSelected
    }

}