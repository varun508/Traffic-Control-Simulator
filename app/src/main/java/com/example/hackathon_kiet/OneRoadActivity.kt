package com.example.hackathon_kiet

import android.content.Context
import android.content.res.ColorStateList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_one_road.*
import kotlinx.coroutines.*

class OneRoadActivity : AppCompatActivity() {

    private val TAG = "OneRoadActivity"

    private val leftLane = TwoWayLane()
    private val rightLane = TwoWayLane()

    private var job = Job()

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
            leftLane.run {
                isRunning = true
                straight = true
                uTurn = true
                leftLaneSignal.background = ContextCompat.getDrawable(
                    this@OneRoadActivity,
                    R.drawable.green_light
                )
                leftLaneStraight.imageTintList = ColorStateList.valueOf(R.color.black)
                leftLaneUturn.imageTintList = ColorStateList.valueOf(R.color.black)
            }

            rightLane.run {
                isRunning = false
                straight = true
                uTurn = true
                leftLaneSignal.background = ContextCompat.getDrawable(
                    this@OneRoadActivity,
                    R.drawable.green_light
                )
                leftLaneStraight.imageTintList = ColorStateList.valueOf(R.color.black)
                leftLaneUturn.imageTintList = ColorStateList.valueOf(R.color.black)
            }

            job = GlobalScope.launch(Dispatchers.Main) {
                repeat(60) {
                    delay(1000)
                    leftLaneTimer.text = (--leftTO).toString()
                }
            }
            job.join()

        } else if (rightTO > leftTO) {

        } else {

        }

        Log.d(
            TAG, "rightLaneOrdinal: ${rightLane.trafficStatus.getTimeOut()} " +
                    "leftLaneOrdinal: ${leftLane.trafficStatus.getTimeOut()}"
        )


    }

    private fun getTrafficStatus(status: String) =
        when (status) {
            "Heavy" -> TrafficStatus.HEAVY
            "Moderate" -> TrafficStatus.MODERATE
            "Light" -> TrafficStatus.LIGHT
            else -> TrafficStatus.HEAVY
        }

}
