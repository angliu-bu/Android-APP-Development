package com.example.a7minworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.a7minworkout.databinding.ActivityExcerciseBinding
import com.example.a7minworkout.databinding.ActivityMainBinding
import com.example.a7minworkout.databinding.DialogCustomBackConfirmationBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ExcerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding : ActivityExcerciseBinding? = null
    private var restTimer : CountDownTimer? = null
    private var restProgress = 0
    private var exerciseTimer : CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercise : Int = -1
    private var tts : TextToSpeech? = null
    private var player : MediaPlayer? = null
    private var exerciseAdapter : ExerciseStatusAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityExcerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExcercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)//When you call setDisplayHomeAsUpEnabled(true), it enables the display of the "Up" button in the action bar or toolbar. This button typically appears as a left-facing arrow or a custom icon representing navigation or going back.
            supportActionBar?.title = "Your Workout"
        }

        exerciseList = Constants.defaultExcerciseList()

        tts = TextToSpeech(this , this)
        binding?.toolbarExcercise?.setNavigationOnClickListener{
            customDialogForBackButton()
        }
        // binding?.flTimer?.visibility = View.GONE will make the fl gone from the ui.
        //.INVISIBLE will make it invisible but thr fl would still have the space in ui.

        setUpRestView()
        setUpExerciseRcyclrView()

    }

    private fun setUpExerciseRcyclrView(){
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL ,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setUpRestView(){// done so that if we go back to home then come gain timer will be reset.
        try {
            val soundURI = Uri.parse("android.resource://com.example.a7minworkout/" + R.raw.kshmr)
            player = MediaPlayer.create(applicationContext , soundURI)
            player?.isLooping = false
            player?.start()
        }catch (e : Exception){
            e.printStackTrace()
        }
        binding?.flTimer?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExercise?.visibility = View.INVISIBLE
        binding?.flTimerExcrcise?.visibility = View.INVISIBLE
        binding?.gifExercise?.visibility = View.INVISIBLE
        binding?.upcomingLable?.visibility = View.VISIBLE
        binding?.tvUpcomingExercise?.visibility  = View.VISIBLE
        speakout("Please Get Some Rest. ")
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        binding?.tvUpcomingExercise?.text = exerciseList!![currentExercise + 1].getName()
        setRestProgressBar()
    }
    private fun setUpExerciseView(){

        binding?.flTimer?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExercise?.visibility = View.VISIBLE
        binding?.flTimerExcrcise?.visibility = View.VISIBLE
        binding?.gifExercise?.visibility = View.VISIBLE
        binding?.tvUpcomingExercise?.visibility = View.INVISIBLE
        binding?.upcomingLable?.visibility = View.INVISIBLE
        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakout(exerciseList!![currentExercise].getName())

        binding?.gifExercise?.setImageResource(exerciseList!![currentExercise].getImage())
        binding?.tvExercise?.text = exerciseList!![currentExercise].getName()
        setExerciseProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress
        restTimer = object : CountDownTimer(1000 , 1000){
            override fun onTick(p0 : Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()

            }

            override fun onFinish() {

                player?.stop()
                currentExercise++
                exerciseList!![currentExercise].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setUpExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(1000 , 1000){
            override fun onTick(p0 : Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {

                Toast.makeText(this@ExcerciseActivity ,
                    "take some rest now"
                    ,Toast.LENGTH_SHORT).show()
                if(currentExercise < exerciseList?.size!! -1){

                    exerciseList!![currentExercise].setIsSelected(false)
                    exerciseList!![currentExercise].setIsCompleted(true)

                    exerciseAdapter!!.notifyDataSetChanged()
                    setUpRestView()
                }else{

                    finish()
                    val intent = Intent(this@ExcerciseActivity , FinalACtivity::class.java)
                    startActivity(intent)
                }

            }
        }.start()
    }

    override fun onBackPressed() {
        customDialogForBackButton()

    }//overriding the onBackPressed functionality to modify it .
    private fun customDialogForBackButton(){
        val customDialog = Dialog(this@ExcerciseActivity)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        // inflating binding for custom xml resource file.
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener{
            this@ExcerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNahh.setOnClickListener{
            customDialog.dismiss()
        }
        customDialog.show()

    }

    override fun onDestroy() {
        super.onDestroy()
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        if(tts != null){
            tts?.stop()
            tts?.shutdown()
        }
        if(player!=null){
            player?.stop()
        }
        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)//setting n checking language

            if(result == TextToSpeech.LANG_MISSING_DATA ||//dhecking if language is right
                result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS" , "the language specified is not supported!")
            }
        }else{
            Log.e("TTS" , "initialization failed!")
        }
    }

    private fun speakout(text : String){
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH , null , "")
    }


}