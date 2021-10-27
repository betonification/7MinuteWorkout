package com.betonification.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_bmiactivity.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    //promenljive koje sluze da znam u kom trenutku je koja grupa view-ova aktivna
    val METRIC_VIEW = "METRIC_VIEW"
    val IMPERIAL_VIEW = "IMPERIAL_VIEW"
    var currentVisibleView = METRIC_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiactivity)

        setSupportActionBar(toolbar_bmi_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "CALCULATE BMI"

        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }
        makeMetricViewVisible()

        //postavljanje view-ova u zavisnosti od toga sta je cekirano na radio button-u
        rgSelectUnits.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbMetric){
                makeMetricViewVisible()
            }else{
                makeImperialViewVisible()
            }
        }

        btnCalculate.setOnClickListener {

            if(currentVisibleView == METRIC_VIEW){
                if (areMetricFieldsFilled()){
                    calculateMetricBMI()
                }else{
                    Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show()
                }
            }else{
                if (areImperialFieldsFilled()){
                    calculateImperialBMI()
                }else
                    Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // funkcija za ispitivanje da li su uneti potrebni podaci, metric
    private fun areMetricFieldsFilled(): Boolean{
        var filled = true

        if(etMetricUnitWeight.text.toString().isEmpty()){
            filled = false
        }else if (etMetricUnitHeight.text.toString().isEmpty()){
            filled = false
        }
        return filled
    }

    // funkcija za ispitivanje da li su uneti potrebni podaci, imperial
    private fun areImperialFieldsFilled(): Boolean{
        var filled = true

        when {
            etImperialUnitWeight.text!!.isEmpty() -> {
                filled = false
            }
            etImperialUnitHeightFeet.text!!.isEmpty() -> {
                filled = false
            }
            etImperialUnitHeightInches.text!!.isEmpty() -> {
                filled = false
            }
        }
        return filled
    }

    //postavljanje view-ova za metric bmi
    private fun makeMetricViewVisible(){
        currentVisibleView = METRIC_VIEW
        tilMetricUnitHeight.visibility = View.VISIBLE
        tilMetricUnitWeight.visibility = View.VISIBLE
        llImperialHeight.visibility = View.GONE
        tilImperialUnitWeight.visibility = View.GONE
        llDisplayBMIResult.visibility = View.GONE
        etImperialUnitHeightFeet.text?.clear()
        etImperialUnitHeightInches.text?.clear()
        etImperialUnitWeight.text?.clear()
    }

    //postavljanje view-ova za imperial bmi
    private fun makeImperialViewVisible(){
        currentVisibleView = IMPERIAL_VIEW
        tilMetricUnitHeight.visibility = View.GONE
        tilMetricUnitWeight.visibility = View.GONE
        llImperialHeight.visibility = View.VISIBLE
        tilImperialUnitWeight.visibility = View.VISIBLE
        llDisplayBMIResult.visibility = View.GONE
        etMetricUnitHeight.text?.clear()
        etMetricUnitWeight.text?.clear()
    }


    private fun calculateMetricBMI(){
        val heightValue = etMetricUnitHeight.text.toString().toFloat() / 100
        val weightValue = etMetricUnitWeight.text.toString().toFloat()

        val bmi = weightValue/(heightValue*heightValue)

        displayBMIResult(bmi)
    }

    private fun calculateImperialBMI(){
        val heightValueFeet = etImperialUnitHeightFeet.text.toString().toFloat()
        val heightValueInches = etImperialUnitHeightInches.text.toString().toFloat()
        val weightValuePounds = etImperialUnitWeight.text.toString().toFloat()

        val heightValue = (heightValueFeet * 12) + heightValueInches

        val bmi = (weightValuePounds / (heightValue*heightValue)) * 703

        displayBMIResult(bmi)
    }

    //funkcija za ispisivanje rezultata BMI, kao i propratnih komentara u njihove pripadajuce textview-ove
    private fun displayBMIResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Extremely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        llDisplayBMIResult.visibility = View.VISIBLE

        //zaokruzavanje vrednosti BMI na dve decimale
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()

        tvBMIValue.text = bmiValue
        tvBMIType.text = bmiLabel
        tvBMIDescription.text = bmiDescription
    }
}