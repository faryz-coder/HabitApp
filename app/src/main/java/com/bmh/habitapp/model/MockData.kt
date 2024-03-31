package com.bmh.habitapp.model

fun MockListHabit(size: Int = 5): MutableList<BadHabits> {
    val list = mutableListOf<BadHabits>()

    for (i in 0..size) {
        list.add(
            BadHabits(
                id = "$i",
                habits = "Habit $i",
                beginDate = "01/12/2024",
                score = "S",
                type = "Money",
                notes = "Notes"
            )
        )
    }
    return list
}