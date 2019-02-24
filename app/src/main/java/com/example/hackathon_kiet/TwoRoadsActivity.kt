package com.example.hackathon_kiet

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_two_roads.*
import kotlinx.coroutines.*

class TwoRoadsActivity : AppCompatActivity() {


    private val leftLane = TwoWayLane()
    private val rightLane = TwoWayLane()
    private val sideLane = TwoWayLane()

    private var job = Job()

    private val colorInActive by lazy { ColorStateList.valueOf(resources.getColor(R.color.red)) }
    private val colorActive by lazy { ColorStateList.valueOf(resources.getColor(R.color.green)) }

    private var rightTO = 0
    private var leftTO = 0
    private var sideTO = 0

    private val redLight by lazy {
        ContextCompat.getDrawable(
            this@TwoRoadsActivity,
            R.drawable.red_light
        )
    }
    private val greenLight by lazy {
        ContextCompat.getDrawable(
            this@TwoRoadsActivity,
            R.drawable.green_light
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two_roads)

        GlobalScope.launch(Dispatchers.Main) { while (true) startProcessing() }
    }

    private suspend fun startProcessing() {
        rightLane.trafficStatus = getTrafficStatus(rightLaneSpinner.selectedItem.toString())
        leftLane.trafficStatus = getTrafficStatus(leftLaneSpinner.selectedItem.toString())
        sideLane.trafficStatus = getTrafficStatus(sideLaneSpinner.selectedItem.toString())

        rightTO = rightLane.trafficStatus.getTimeOut()
        leftTO = leftLane.trafficStatus.getTimeOut()
        sideTO = sideLane.trafficStatus.getTimeOut()

        job.run {
            leftLane.start()
            rightLane.stop()
            sideLane.stop()
            leftLaneTimer.countDown(leftTO)
            rightLane.start()
            leftLane.stop()
            rightLaneTimer.countDown(rightTO)
            sideLane.start()
            rightLane.stop()
            sideLaneTimer.countDown(sideTO)
        }
    }

    private suspend fun TextView.countDown(seconds: Int) {
        (seconds downTo 0).forEach {
            delay(1000)
            text = it.toString()
        }
        text = when {
            this === leftLaneTimer -> (sideTO + rightTO).toString()
            this === rightLaneTimer -> (sideTO + leftTO).toString()
            else -> (leftTO + rightTO).toString()
        }
        GlobalScope.launch(Dispatchers.Main) {
            (text.toString().toInt() + 1 downTo 0).forEach {
                delay(1000)
                text = it.toString()
            }
        }
    }

    private fun TwoWayLane.stop() {
        isRunning = false
        straight = false
        uTurn = false
        when {
            this === rightLane -> {
                rightLaneSignal.background = redLight
                rightLaneToSideLane.imageTintList = colorInActive
                rightLaneStraight.imageTintList = colorInActive
                rightLaneUTurn.imageTintList = colorInActive
            }
            this === leftLane -> {
                leftLaneSignal.background = redLight
                leftLaneToSideLane.imageTintList = colorInActive
                leftLaneStraight.imageTintList = colorInActive
                leftLaneUTurn.imageTintList = colorInActive
            }
            else -> {
                sideLaneSignal.background = redLight
                sideLaneToLeftLane.imageTintList = colorInActive
                sideLaneToRightLane.imageTintList = colorInActive
                sideLaneUTurn.imageTintList = colorInActive
            }
        }
    }

    private fun TwoWayLane.start() {
        isRunning = true
        straight = true
        uTurn = true
        when {
            this === rightLane -> {
                rightLaneSignal.background = greenLight
                rightLaneToSideLane.imageTintList = colorActive
                rightLaneStraight.imageTintList = colorActive
                rightLaneUTurn.imageTintList = colorActive
            }
            this === leftLane -> {
                leftLaneSignal.background = greenLight
                leftLaneToSideLane.imageTintList = colorActive
                leftLaneStraight.imageTintList = colorActive
                leftLaneUTurn.imageTintList = colorActive
            }
            else -> {
                sideLaneSignal.background = greenLight
                sideLaneToLeftLane.imageTintList = colorActive
                sideLaneToRightLane.imageTintList = colorActive
                sideLaneUTurn.imageTintList = colorActive
            }
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
