package com.example.hackathon_kiet

import android.content.res.ColorStateList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_one_road.*
import kotlinx.coroutines.*

class OneRoadActivity : AppCompatActivity() {

    private val TAG = "OneRoadActivity"

    private val leftLane = TwoWayLane()
    private val rightLane = TwoWayLane()

    private var job = Job()

    private val colorInActive = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.textLightGray))
    private val colorActive = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))
    private val redLight = ContextCompat.getDrawable(
        this@OneRoadActivity,
        R.drawable.red_light
    )

    private val greenLight = ContextCompat.getDrawable(
        this@OneRoadActivity,
        R.drawable.green_light
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_road)

        rightLaneSpinner.onItemSelectedListener = listener()
        leftLaneSpinner.onItemSelectedListener = listener()
    }

    private fun listener() = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            job.cancel()
            GlobalScope.launch { startProcessing() }
        }
    }


    private suspend fun startProcessing() {
        rightLane.trafficStatus = getTrafficStatus(rightLaneSpinner.selectedItem.toString())
        leftLane.trafficStatus = getTrafficStatus(leftLaneSpinner.selectedItem.toString())

        var rightTO = rightLane.trafficStatus.getTimeOut()
        var leftTO = leftLane.trafficStatus.getTimeOut()

        if (rightTO == leftTO) {
            leftLane.start()
            rightLane.stop()

            leftLaneTimer.countDown(leftTO)
            job.join()

            leftLane.stop()
            rightLane.start()

        } else if (rightTO > leftTO) {

        } else {

        }
    }

    private fun TextView.countDown(seconds: Int) {
        job = GlobalScope.launch(Dispatchers.Main) {
            (seconds until 0).forEach {
                delay(1000)
                leftLaneTimer.text = it.toString()
            }
        }
    }

    private fun TwoWayLane.stop() {
        isRunning = false
        straight = false
        uTurn = false
        if (this == rightLane) {
            rightLaneSignal.background = redLight
            rightLaneStraight.imageTintList = colorInActive
            rightLaneUturn.imageTintList = colorInActive
        } else {
            leftLaneSignal.background = redLight
            leftLaneStraight.imageTintList = colorInActive
            leftLaneUturn.imageTintList = colorInActive
        }
    }

    private fun TwoWayLane.start() {
        isRunning = true
        straight = true
        uTurn = true
        if (this == rightLane) {
            rightLaneSignal.background = greenLight
            rightLaneStraight.imageTintList = colorActive
            rightLaneUturn.imageTintList = colorActive
        } else {
            leftLaneSignal.background = greenLight
            leftLaneStraight.imageTintList = colorActive
            leftLaneUturn.imageTintList = colorActive
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
