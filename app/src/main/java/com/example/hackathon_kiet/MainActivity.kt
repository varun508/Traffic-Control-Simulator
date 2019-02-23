package com.example.hackathon_kiet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toOne(view: View) {
        startActivity(Intent(this, OneRoadActivity::class.java))
    }
    fun toTwo(view: View) {
        startActivity(Intent(this, OneRoadActivity::class.java))
    }
    fun toThree(view: View) {
        startActivity(Intent(this, OneRoadActivity::class.java))
    }
}
