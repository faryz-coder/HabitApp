package com.bmh.habitapp.utils

import com.bmh.habitapp.manager.FirestoreManager
import com.bmh.habitapp.model.BadHabits
import com.bmh.habitapp.screen.detail.DetailViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun timeDifference(begin: String): String {
    var begin = begin
    if (begin.isEmpty()) begin =  "12/12/2024"
    val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val targetDate = LocalDate.parse(begin, dateFormat)

    val currentDate = LocalDateTime.now().toLocalDate()

    val dayDiff = ChronoUnit.DAYS.between(targetDate, currentDate)

    return dayDiff.toString()
}

/**
 * Convert to achievement based on :
 * start = beginDate , current = currentDate
 */
fun getAchievement(start: String): String {
    val diff = timeDifference(start).toInt()

    return when {
        diff >= 365 -> "S"
        diff >= 120 -> "A+"
        diff >= 60 -> "A"
        diff >= 30 -> "A-"
        diff >= 15 -> "B"
        diff >= 7 -> "D"
        diff >= 3 -> "C"
        diff >= 1 -> "E"
        else -> "F"
    }
}

fun percentBeforeNextLevel(start: String): Float {
    val diff = timeDifference(start).toInt()

    return when (getAchievement(start)) {
        "F" -> (diff / 1f)
        "E" -> (diff / 3f)
        "C" -> (diff / 7f)
        "D" -> (diff / 15f)
        "B" -> (diff / 30f)
        "A-" -> (diff / 60f)
        "A" -> (diff / 120f)
        "A+" -> (diff / 365f)
        else -> {
            1f
        }
    }
}

fun currentDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return LocalDateTime.now().format(formatter)
}

fun Int.toAchievement(): String {
    return when {
        this >= 365 -> "S"
        this >= 120 -> "A+"
        this >= 60 -> "A"
        this >= 30 -> "A-"
        this >= 15 -> "B"
        this >= 7 -> "D"
        this >= 3 -> "C"
        this >= 1 -> "E"
        else -> "F"
    }
}

fun String.toRankDescription(): String {
    return when (this) {
        "E" -> "Bad"
        "D" -> "Below Average"
        "C" -> "Average"
        "B" -> "Above Average"
        "A-" -> "Good"
        "A" -> "Very Good"
        "A+" -> "Master"
        "S" -> "Legend"
        else -> {
            "Very Bad"
        }
    }
}


fun getCurrentTimestamp(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    return currentDateTime.format(formatter)
}

fun calculateEarned(badHabits: BadHabits, detailViewModel: DetailViewModel): String {
    val earned = timeDifference(badHabits.beginDate).toLong() * badHabits.cost

    if (earned != badHabits.moneyEarned) {
//        update db
        FirestoreManager().updateEarned(
            detailViewModel = detailViewModel,
            badHabits = badHabits,
            earned = earned
        )
    }
    return earned.toString()
}