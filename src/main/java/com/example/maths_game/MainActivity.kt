package com.example.maths_game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newGameBtn = findViewById<Button>(R.id.new_game)
        val aboutBtn = findViewById<Button>(R.id.about)

        newGameBtn.setOnClickListener{

            val gameLobbyPageIntent = Intent(this,GameLobby:: class.java)
            startActivity(gameLobbyPageIntent)
        }
        aboutBtn.setOnClickListener{

            val aboutPageIntent = Intent(this,About:: class.java)
            startActivity(aboutPageIntent)
        }
    }
}