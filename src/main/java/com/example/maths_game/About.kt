package com.example.maths_game

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class About: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about)

        val aboutBackBtn = findViewById<Button>(R.id.aboutBackBtn)

        aboutBackBtn.setOnClickListener{
            val backToHomePage = Intent(this,MainActivity:: class.java)
            startActivity(backToHomePage)
        }
    }
}