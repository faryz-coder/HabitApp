package com.bmh.habitapp.model

data class Rewards(
    val id: String = "",
    val name: String,
    val price: Number,
    val owned: Number = 0
)
