package com.bmh.habitapp.model

data class BadHabits(
    var id: String = "",
    var habits: String = "",
    // Money Earned = (cost * consumption) * day passed
    var moneyEarned: Long = 0L,
    var beginDate: String = "",
    var daily: Long = 0L,
    var weekly: Long = 0L,
    var monthly: Long = 0L,
    var totalNotUsed: Long = 0L,
    // Score = Achievement
    var score: String = "",
    var type: String = "",
    // Consumption - Daily
    var consumption: Long = 0L,
    // Cost - Each
    var cost: Long = 0L,
    var notes: String = "",
    var spend: Long = 0L,
    var rewards: List<Rewards> = emptyList()
)