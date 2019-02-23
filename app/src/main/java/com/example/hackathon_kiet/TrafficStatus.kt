package com.example.hackathon_kiet

enum class TrafficStatus(
    private val timeOut: Int
) {
    HEAVY(6),
    MODERATE(4),
    LIGHT(2);

    fun getTimeOut() = timeOut
}