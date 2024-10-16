package com.example.a7minworkout

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.FrameLayout
import android.widget.Toast
import com.example.a7minworkout.databinding.ActivityMainBinding
import com.example.a7minworkout.databinding.DialogCustomBackConfirmationBinding
import com.example.a7minworkout.databinding.DialogCustomBackConfirmationForMainActivityBinding

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)//R.layout.activity_main isn't used here

        //val flStartButton : FrameLayout = findViewById(R.id.flStart)
        binding?.flStart?.setOnClickListener{//now we don't need to create any variables for layout items using their ids.


            val intent = Intent(this , ExcerciseActivity::class.java)
            startActivity(intent)

        }
        binding?.flBmi?.setOnClickListener{
            val intent = Intent(this , BMIActivity::class.java)
            startActivity(intent)
        }
        binding?.flHistory?.setOnClickListener{
            val intent = Intent(this , HistoryActivity::class.java)
            startActivity(intent)
        }


    }
    override fun onBackPressed() {
        customDialogForBackButton()

    }//overriding the onBackPressed functionality to modify it .
    private fun customDialogForBackButton(){
        val customDialog = Dialog(this@MainActivity)
        val dialogBinding = DialogCustomBackConfirmationForMainActivityBinding.inflate(layoutInflater)
        // inflating binding for custom xml resource file.
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener{
            this@MainActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNahh.setOnClickListener{
            customDialog.dismiss()
        }
        customDialog.show()

    }

    override fun onDestroy() {//used to unassign viewbinding

        super.onDestroy()
        binding = null
    }


}