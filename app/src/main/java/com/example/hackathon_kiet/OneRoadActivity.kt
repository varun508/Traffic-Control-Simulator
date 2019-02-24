package com.example.hackathon_kiet

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_one_road.*
import kotlinx.coroutines.*

class OneRoadActivity : AppCompatActivity() {

    private val leftLane = TwoWayLane()
    private val rightLane = TwoWayLane()

    private var job = Job()

    private val colorInActive by lazy { ColorStateList.valueOf(resources.getColor(R.color.textLightGray)) }
    private val colorActive by lazy { ColorStateList.valueOf(resources.getColor(R.color.black)) }

    private val redLight by lazy {
        ContextCompat.getDrawable(
            this@OneRoadActivity,
            R.drawable.red_light
        )
    }
    private val greenLight by lazy {
        ContextCompat.getDrawable(
            this@OneRoadActivity,
            R.drawable.green_light
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_road)

        GlobalScope.launch(Dispatchers.Main) { while (true) startProcessing() }
    }

    private suspend fun startProcessing() {
        rightLane.trafficStatus = getTrafficStatus(rightLaneSpinner.selectedItem.toString())
        leftLane.trafficStatus = getTrafficStatus(leftLaneSpinner.selectedItem.toString())

        val rightTO = rightLane.trafficStatus.getTimeOut()
        val leftTO = leftLane.trafficStatus.getTimeOut()

        job.run {
            leftLane.start()
            rightLane.stop()
            leftLaneTimer.countDown(leftTO)
            leftLane.stop()
            rightLane.start()
            rightLaneTimer.countDown(rightTO)
        }
    }

    private suspend fun TextView.countDown(seconds: Int) {
        (seconds downTo 0).forEach {
            delay(1000)
            text = it.toString()
        }
        text = seconds.toString()
    }

    private fun TwoWayLane.stop() {
        isRunning = false
        straight = false
        uTurn = false
        if (this === rightLane) {
            rightLaneSignal.background = redLight
            rightLaneStraight.imageTintList = colorInActive
            rightLaneUTurn.imageTintList = colorInActive
        } else if (this === leftLane) {
            leftLaneSignal.background = redLight
            leftLaneStraight.imageTintList = colorInActive
            leftLaneUTurn.imageTintList = colorInActive
        }
    }

    private fun TwoWayLane.start() {
        isRunning = true
        straight = true
        uTurn = true
        if (this === rightLane) {
            rightLaneSignal.background = greenLight
            rightLaneStraight.imageTintList = colorActive
            rightLaneUTurn.imageTintList = colorActive
        } else if (this === leftLane) {
            leftLaneSignal.background = greenLight
            leftLaneStraight.imageTintList = colorActive
            leftLaneUTurn.imageTintList = colorActive
        }
    }

    private fun getTrafficStatus(status: String) =
        when (status) {
            "Heavy" -> TrafficStatus.HEAVY
            "Moderate" -> TrafficStatus.MODERATE
            "Light" -> TrafficStatus.LIGHT
            else -> TrafficStatus.HEAVY
        }

}
