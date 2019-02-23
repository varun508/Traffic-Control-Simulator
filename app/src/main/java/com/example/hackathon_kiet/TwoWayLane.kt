package com.example.hackathon_kiet

data class TwoWayLane(
    var uTurn: Boolean = false,
    var straight: Boolean = false,
    var trafficStatus: TrafficStatus = TrafficStatus.HEAVY,
    var isRunning: Boolean = false
)