package com.example.maths_game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Results : AppCompatActivity(){

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.final_results)

        val correctAnsText = findViewById<TextView>(R.id.correct_ans)
        val allQues = findViewById<TextView>(R.id.allQues)
        val backBtn = findViewById<Button>(R.id.gameResultsBackBtn)

        val bundle:Bundle? = intent.extras
        val allQuesCount = bundle!!.getString("allQuesCount")
        val correctAns = bundle.getString("correctAns")

        correctAnsText.text =correctAns
        allQues.text = allQuesCount



        backBtn.setOnClickListener{
            val backToHomePage = Intent(this,MainActivity:: class.java)
            startActivity(backToHomePage)
        }

    }
}