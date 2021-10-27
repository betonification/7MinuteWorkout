package com.betonification.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.betonification.a7minuteworkout.Constants.Companion.defaultExerciseList
import com.betonification.a7minuteworkout.MainActivity.Companion.exerciseList
import com.betonification.a7minuteworkout.MainActivity.Companion.selectedExercisesList
import kotlinx.android.synthetic.main.activity_exercise_acivity.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer : CountDownTimer? = null
    private var restProgressTimer : CountDownTimer? = null
    private var exerciseTimer : CountDownTimer? = null
    private var exerciseProgressTimer: CountDownTimer? = null
    private var restTimeInMillis : Long = 10000
    private var exerciseTimeInMillis : Long = 30000
    //private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var exerciseStatusAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_acivity)

        tts = TextToSpeech(this, this)

        //postavljanje i podesavanje actionbar-a
        setSupportActionBar(toolbar_exercise_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //postavljanje funkcionalnosti back dugmeta u actionbaru. otkazivanje svih tajmera
        toolbar_exercise_activity.setNavigationOnClickListener {
            setupCustomDialogForBackButton()
        }

        exerciseList = setupExerciseList()

        setupExerciseStatusRecyclerView()

        setupRestView()
    }

    override fun onBackPressed() {
        setupCustomDialogForBackButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        restTimer?.cancel()
        restProgressTimer?.cancel()
        exerciseTimer?.cancel()
        tts?.stop()
        tts?.shutdown()
        player?.stop()
    }

    //funkcija koja je zaduzena za odbrojavanje tajmera za odmor, kao i za progressbar (napravljena su dva countdown tajmera da bi progress bar imao smooth animaciju)
    private fun restCountDown(){
        restTimer = object : CountDownTimer(restTimeInMillis + 1000,1000){
            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsOngoing(true)
                exerciseStatusAdapter!!.notifyItemChanged(currentExercisePosition)
                setupExerciseView()
                setExerciseModel()
                this.cancel()
            }
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.text = (millisUntilFinished / 1000).toString()
                when(tvTimer.text){
                    "9" -> hearTheText("Get ready for " + exerciseList!![currentExercisePosition+1].getName())
                    "3" -> hearTheText("Three")
                    "2" -> hearTheText("Two")
                    "1" -> hearTheText("One")
                }
            }
        }.start()
        restProgressTimer = object : CountDownTimer (restTimeInMillis, 10){
            override fun onFinish() {
                this.cancel()
            }

            override fun onTick(millisUntilFinished: Long) {
                progressBar.progress = (millisUntilFinished / 10).toInt()
            }
        }.start()
    }

    //funkcija koja je zaduzena za odbrojavanje tajmera za vezbanje, kao i za progressbar (napravljena su dva countdown tajmera da bi progress bar imao smooth animaciju)
    private fun exerciseCountDown(){
        exerciseTimer = object : CountDownTimer(exerciseTimeInMillis + 1000,1000){
            override fun onFinish() {
                if(currentExercisePosition < exerciseList!!.size - 1){
                    exerciseList!![currentExercisePosition].setIsOngoing(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseStatusAdapter!!.notifyItemChanged(currentExercisePosition)
                    setupRestView()
                }else{
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                    this.cancel()
                }
                this.cancel()
            }

            override fun onTick(millisUntilFinished: Long) {
                tvExerciseTimer.text = (millisUntilFinished / 1000).toString()
                when(tvExerciseTimer.text){
                    "20" -> hearTheText("Twenty seconds left")
                    "10" -> hearTheText("Ten seconds left")
                    "3" -> hearTheText("Three")
                    "2" -> hearTheText("Two")
                    "1" -> hearTheText("One")
                    "0" -> {
                        try{
                            player = MediaPlayer.create(applicationContext,R.raw.exercise_finished)
                            player!!.isLooping = false
                            player!!.start()
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }
        }.start()
        exerciseProgressTimer = object : CountDownTimer (exerciseTimeInMillis, 10){
            override fun onFinish() {
                this.cancel()
            }

            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress.progress = (millisUntilFinished / 10).toInt()
            }
        }.start()
    }

    //Postavljanje layouta za odmaranje
    private fun setupRestView(){
        llExerciseView.visibility = View.GONE
        llRestView.visibility = View.VISIBLE
        exerciseProgress.progress = exerciseProgress.max
        tvNextExercise.text = exerciseList!![currentExercisePosition+1].getName()
        restCountDown()
    }

    //postavljanje layouta za vezbanje
    private fun setupExerciseView(){
        llRestView.visibility = View.GONE
        llExerciseView.visibility = View.VISIBLE
        progressBar.progress = progressBar.max
        exerciseCountDown()
    }

    //funkcija za postavljanje vezbe, tj za postavljanje komponenata iz exerciseModel-a u view-ove
    private fun setExerciseModel(){
        val exercise = exerciseList!![currentExercisePosition]
        tvExerciseName.text = exercise.getName()
        hearTheText(exercise.getName())
        ivExerciseImage.setImageResource(exercise.getImage())
    }
    //funkcija koja mora obavezno da se implementira zbog nasledjivanja od texttospeech i koja je zaduzena za postavljanje jezika govora i handlovanje greski ukoliko dodje do nje
    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","Language missing or not supported")
            }else{
                Log.e("TTS","Speech initialization failed")
            }
        }
    }

    //funkcija koja sluzi da prolsednjeni tekst procita naglas
    private fun hearTheText(text: CharSequence){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null, "")
    }

    //Postavljanje recyclerview-a za status vezbi u donjem delu ekrana
    private fun setupExerciseStatusRecyclerView(){
        rvExerciseStatus.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseStatusAdapter = ExerciseStatusAdapter(exerciseList!!, this)
        rvExerciseStatus.adapter = exerciseStatusAdapter
    }

    // postavljanje custom dialoga za back dugme, klikom na Yes se desava isto sto bi se inace desavalo u onBackPressed, kada ne bi bio implementiran dialog
    private fun setupCustomDialogForBackButton(){
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)

        customDialog.btnYes.setOnClickListener {
            restTimer?.cancel()
            restProgressTimer?.cancel()
            exerciseTimer?.cancel()
            tts?.stop()
            tts?.shutdown()
            player?.stop()
            finish()
            customDialog.dismiss()
        }
        customDialog.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    // funkcija koja vraca novu listu vezbanja, koja se sastoji samo od vezbanja koja su selektovana u recyclerView-u
    private fun setupExerciseList():ArrayList<ExerciseModel>{
        val newList = ArrayList<ExerciseModel>()
        val defaultList = selectedExercisesList
        for(item in defaultList!!){
            if(item.getIsSelected()){
                newList.add(item)
            }
        }
        return newList
    }
}