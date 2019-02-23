package com.example.hackathon_kiet

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class OneRoadActivity : AppCompatActivity() {

    val leftLane = TwoWayLane()
    val rightLane = TwoWayLane()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_road)
    }


}
