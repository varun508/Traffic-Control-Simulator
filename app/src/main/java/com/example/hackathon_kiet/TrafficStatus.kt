package com.example.hackathon_kiet

enum class TrafficStatus(
    private val timeOut: Int
) {
    HEAVY(60),
    MODERATE(40),
    LIGHT(20);

    fun getTimeOut() = timeOut
}