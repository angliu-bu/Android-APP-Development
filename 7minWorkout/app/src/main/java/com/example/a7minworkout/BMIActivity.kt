package com.example.a7minworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener
import android.widget.Toast
import com.example.a7minworkout.databinding.ActivityBmiactivityBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val US_UNIT_VIEW = "US_UNIT_VIEW"
    }
    private var currentVisibleView : String = METRIC_UNITS_VIEW

    private var binding : ActivityBmiactivityBinding? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiactivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarBmiActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Calculate BMI"
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener{_ , checkedId : Int -> // this is used for radio groups.
            if(checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUSUnitsView()
            }
        }
        binding?.btnCalculateUnits?.setOnClickListener{

            if(validateMetricUnits() && currentVisibleView == METRIC_UNITS_VIEW){
                val heightValue : Float = binding?.etMetricUnitHeight?.text.toString().toFloat()/100
                val weightValue : Float = binding?.etMetricUnitWeight?.text.toString().toFloat()
                val bmi = weightValue/(heightValue*heightValue)
                displayBMIResultValue(bmi)

            }else if (validateMetricUnits() && currentVisibleView == US_UNIT_VIEW){
                val inchPartOfHeight : Float
                if (binding?.etUSUnitHeightInch?.text.toString().isEmpty()){
                    inchPartOfHeight  = 0f
                }
                else{
                    inchPartOfHeight = binding?.etUSUnitHeightInch?.text.toString().toFloat()
                }
                val heightValue : Float = ((binding?.etUSUnitHeightFoot?.text.toString().toFloat() * 30.48f ) + (inchPartOfHeight * 2.54f)  )  / 100
                val weightValue : Float = binding?.etMetricUnitWeight?.text.toString().toFloat()
                val bmi = weightValue/(heightValue*heightValue)
                displayBMIResultValue(bmi)
            }
            else{
                Toast.makeText(this@BMIActivity ,
                    "Please enter valid values."
                , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?.tilUSUnitHeightFoot?.visibility = View.GONE
        binding?.tilUSUnitHeightInch?.visibility = View.GONE
        //u can also set til for ponds over here instead of kgs
        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE

    }
    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNIT_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?.tilUSUnitHeightFoot?.visibility = View.VISIBLE
        binding?.tilUSUnitHeightInch?.visibility = View.VISIBLE
        //u can also set til for ponds over here instead of kgs
        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()
        binding?.etUSUnitHeightFoot?.text!!.clear()
        binding?.etUSUnitHeightInch?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE

    }
    private fun displayBMIResultValue(bmi :Float){

        val bmiLabel : String
        val bmiDescription : String

        if(bmi.compareTo(15f) <= 0){
            bmiLabel = "Very Severely Underweight"
            bmiDescription = "oops! You Really need to take care of your diet and your routine. "

        }else if(bmi.compareTo(15f) > 0 && bmi.compareTo(16f )<= 0 ){
            bmiLabel = "Severely Underweight"
            bmiDescription = "oops! You Really need to take care of your diet and your routine. "
        }else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f )<= 0 ){
            bmiLabel = "Underweight"
            bmiDescription = "oops! You Really need to take care of your diet and your routine. "
        }else if(bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f )<= 0 ){
            bmiLabel = "Normal"
            bmiDescription = "You are in good shape , take care of yourself. "
        }else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f )<= 0){
            bmiLabel = "Overweight"
            bmiDescription = "Take care of yourself , stick to your workout have a controlled and planned diet. "
        }else if(bmi.compareTo(30f) > 0 && bmi.compareTo(35f )<= 0){
            bmiLabel = "obese class 1 (Moderately Obese) "
            bmiDescription = "oops! You really need to take care of yourself , stick to your workout have a strict and planned diet. "
        }else if(bmi.compareTo(35f) > 0 && bmi.compareTo(40f )<= 0){
            bmiLabel = "obese class 2 (Severely Obese)"
            bmiDescription = "oops! You really need to take care of yourself , stick to your workout have a strict and planned diet , you also need consultancy by physician and Diet and Workout planner or your trainer. "
        }else if(bmi.compareTo(35f) > 0 && bmi.compareTo(40f )<= 0){
            bmiLabel = "obese class 3 (Severely Obese)"
            bmiDescription = "oops! You really need to take care of yourself , stick to your workout have a strict and planned diet , you also need continuous consultancy by physician and Diet and Workout planner or your trainer. "
        }else if(bmi.compareTo(40f) > 0 && bmi.compareTo(55f )<= 0) {
            bmiLabel = "Kuch bhi"
            bmiDescription ="Are you a pig."
        }else{
            bmiLabel = "NO Result"
            bmiDescription = "NO Result"
        }

        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = BigDecimal(bmi.toDouble()).setScale(2 , RoundingMode.HALF_EVEN).toString()
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription
    }

    private fun validateMetricUnits():Boolean{
        var isValid = true
        if(binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }
        else if (binding?.etMetricUnitHeight?.text.toString().isEmpty() && binding?.etUSUnitHeightFoot?.text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }
}