package com.bmh.habitapp.manager

import android.util.Log
import com.bmh.habitapp.model.BadHabits
import com.bmh.habitapp.model.Chats
import com.bmh.habitapp.model.Constant.BEGIN_DATE
import com.bmh.habitapp.model.Constant.CHAT
import com.bmh.habitapp.model.Constant.CONSUMPTION
import com.bmh.habitapp.model.Constant.COST
import com.bmh.habitapp.model.Constant.DAILY
import com.bmh.habitapp.model.Constant.EMAIL
import com.bmh.habitapp.model.Constant.HABIT
import com.bmh.habitapp.model.Constant.HABITS
import com.bmh.habitapp.model.Constant.MESSAGE
import com.bmh.habitapp.model.Constant.MONEY_EARNED
import com.bmh.habitapp.model.Constant.MONTHLY
import com.bmh.habitapp.model.Constant.NAME
import com.bmh.habitapp.model.Constant.NOTES
import com.bmh.habitapp.model.Constant.OWNED
import com.bmh.habitapp.model.Constant.PRICE
import com.bmh.habitapp.model.Constant.RANK
import com.bmh.habitapp.model.Constant.REWARDS
import com.bmh.habitapp.model.Constant.SCORE
import com.bmh.habitapp.model.Constant.SPEND
import com.bmh.habitapp.model.Constant.TIMESTAMP
import com.bmh.habitapp.model.Constant.TOTAL_NOT_USED
import com.bmh.habitapp.model.Constant.TYPE
import com.bmh.habitapp.model.Constant.USER
import com.bmh.habitapp.model.Constant.WEEKLY
import com.bmh.habitapp.model.Rewards
import com.bmh.habitapp.model.UserInfo
import com.bmh.habitapp.screen.detail.DetailViewModel
import com.bmh.habitapp.utils.currentDate
import com.bmh.habitapp.utils.getCurrentTimestamp
import com.bmh.habitapp.utils.timeDifference
import com.bmh.habitapp.utils.toAchievement
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.getField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreManager {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun getUserInfo(onSuccess: (UserInfo) -> Unit = {}, onFailed: () -> Unit = {}) {
        val userEmail = auth.currentUser?.email

        db.collection(USER).document(userEmail.toString())
            .get()
            .addOnSuccessListener {
                val userInfo = UserInfo(
                    email = it.id,
                    rank = it.getField<String>(RANK) ?: ""
                )
                onSuccess.invoke(userInfo)
            }
            .addOnFailureListener {
                onFailed.invoke()
            }
    }

    fun getChat(onSuccess: (MutableList<Chats>) -> Unit, onFailed: () -> Unit) {
        db.collection(CHAT).orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onFailed.invoke()
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val list = mutableListOf<Chats>()

                    snapshot.map {
                        list.add(
                            Chats(
                                email = it.getField<String>(EMAIL) ?: "",
                                rank = it.getField<String>(RANK) ?: "",
                                message = it.getField<String>(MESSAGE) ?: "",
                                timestamp = it.getField<String>(TIMESTAMP) ?: "",
                            )
                        )
                    }
                    onSuccess.invoke(list)
                }
            }
    }

    fun getListHabit(onSuccess: (MutableList<BadHabits>) -> Unit, onFailed: () -> Unit) {
        val list = mutableListOf<BadHabits>()
        val userEmail = auth.currentUser?.email
        var ranked = 0

        db.collection(USER).document(userEmail.toString())
            .collection(HABIT).get()
            .addOnSuccessListener { document ->
                if (document.size() > 0) {
                    document.map { item ->
                        list.add(
                            BadHabits(
                                item.id,
                                item.getField<String>(HABITS) ?: "",
                                item.getField<Long>(MONEY_EARNED) ?: 0L,
                                item.getField<String>(BEGIN_DATE) ?: "",
                                item.getField<Long>(DAILY) ?: 0L,
                                item.getField<Long>(WEEKLY) ?: 0L,
                                item.getField<Long>(MONTHLY) ?: 0L,
                                item.getField<Long>(TOTAL_NOT_USED) ?: 0L,
                                item.getField<String>(SCORE) ?: "",
                                item.getField<String>(TYPE) ?: "",
                                item.getField<Long>(CONSUMPTION) ?: 0L,
                                item.getField<Long>(COST) ?: 0L,
                                item.getField<String>(NOTES) ?: "",
                                item.getField<Long>(SPEND) ?: 0L,
                            )
                        )
                        // record highest ranked
                        val diff =
                            timeDifference(item.getField<String>(BEGIN_DATE).toString()).toInt()
                        if (diff > ranked) {
                            ranked = diff
                        }
                    }
                    submitHighestRank(ranked)
                    onSuccess.invoke(list)
                    Log.d("getListHabit", "list: ${list.size}")
                } else {
                    emptyList<BadHabits>()
                    Log.d("getListHabit", "list: emptyList")
                }
            }
            .addOnFailureListener {
                Log.d("getListHabit", "list: onFailed")
                onFailed.invoke()
            }
    }

    private fun submitHighestRank(rank: Int) {
        rank.toAchievement()
        val userEmail = auth.currentUser?.email

        val data = hashMapOf(
            "rank" to rank.toAchievement()
        )

        db.collection(USER).document(userEmail.toString())
            .set(data, SetOptions.merge())
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    fun getReward(
        badHabits: BadHabits,
        onSuccess: (MutableList<Rewards>) -> Unit,
        onFailed: () -> Unit
    ) {
        val list = mutableListOf<Rewards>()
        val userEmail = auth.currentUser?.email

        val docRef = db.collection(USER).document(userEmail.toString())
            .collection(HABIT).document(badHabits.id)
            .collection(REWARDS)
        docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                list.clear()
                list.add(Rewards(name = "", price = 0))
                if (snapshot.size() > 0) {
                    snapshot.map { item ->
                        list.add(
                            Rewards(
                                item.id,
                                item.getField<String>(NAME) ?: "",
                                item.getField<Long>(PRICE) ?: 0,
                                item.getField<Long>(OWNED) ?: 0
                            )
                        )
                        onSuccess.invoke(list)
                    }
                }
                onSuccess.invoke(list)
            }
        }
    }

    fun addReward(
        badHabits: BadHabits,
        reward: Rewards,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    ) {
        val userEmail = auth.currentUser?.email

        val data = hashMapOf(
            "name" to reward.name,
            "price" to reward.price
        )
        db.collection(USER).document(userEmail.toString())
            .collection(HABIT).document(badHabits.id)
            .collection(REWARDS).add(data)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnSuccessListener {
                onFailed.invoke()
            }

    }

    fun deleteReward(
        badHabits: BadHabits,
        reward: Rewards,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    ) {
        val userEmail = auth.currentUser?.email
        val data = hashMapOf(
            SPEND to badHabits.spend - (reward.owned.toLong() * reward.price.toLong())
        )
        db.collection(USER).document(userEmail.toString())
            .collection(HABIT).document(badHabits.id)
            .set(data, SetOptions.merge())

        db.collection(USER).document(userEmail.toString())
            .collection(HABIT).document(badHabits.id)
            .collection(REWARDS).document(reward.id)
            .delete()
            .addOnFailureListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailed.invoke()
            }
    }

    fun updateNotes(badHabits: BadHabits, onSuccess: () -> Unit) {
        val notes = hashMapOf(
            "notes" to badHabits.notes
        )

        db.collection(USER).document(auth.currentUser!!.email!!)
            .collection(HABIT).document(badHabits.id)
            .set(notes, SetOptions.merge())
            .addOnSuccessListener {
                onSuccess.invoke()
            }
    }

    fun addHabits(badHabits: BadHabits, onSuccess: () -> Unit, onFailed: () -> Unit) {
        val userEmail = auth.currentUser?.email
        val dailyConsumptionCost = badHabits.cost * badHabits.consumption
        //
        badHabits.daily = dailyConsumptionCost
        badHabits.weekly = dailyConsumptionCost * 7
        badHabits.monthly = dailyConsumptionCost * 29
        badHabits.moneyEarned = dailyConsumptionCost
        badHabits.beginDate = currentDate()
        //
        if (badHabits.consumption <= 0) {
            badHabits.consumption = 1
        }
        db.collection(USER).document(userEmail.toString())
            .collection(HABIT)
            .add(badHabits)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailed.invoke()
            }
    }

    fun deleteHabits(id: String) {
        val userEmail = auth.currentUser?.email
        db.collection(USER).document(userEmail.toString())
            .collection(HABIT)
            .document(id)
            .delete()
    }

    fun sendChat(detailViewModel: DetailViewModel, message: String) {
        val timestamp = getCurrentTimestamp()
        val data = hashMapOf(
            "email" to detailViewModel.loginInfo.email,
            "rank" to detailViewModel.loginInfo.rank,
            "message" to message,
            "timestamp" to timestamp
        )

        db.collection(CHAT)
            .add(data)
    }

    fun updateEarned(detailViewModel: DetailViewModel, badHabits: BadHabits, earned: Long) {
        val data = hashMapOf(
            MONEY_EARNED to earned
        )
        db.collection(USER).document(detailViewModel.loginInfo.email)
            .collection(HABIT).document(badHabits.id)
            .set(data, SetOptions.merge())
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    fun buyItem(detailViewModel: DetailViewModel, reward: Rewards) {
        val spend = reward.price.toLong() + detailViewModel.badHabits.spend
        val owned = reward.owned.toLong() + 1
        val dataHabit = hashMapOf(
            SPEND to spend
        )
        val dataReward = hashMapOf(
            OWNED to owned
        )
        db.collection(USER).document(detailViewModel.loginInfo.email)
            .collection(HABIT).document(detailViewModel.badHabits.id)
            .set(dataHabit, SetOptions.merge())
        db.collection(USER).document(detailViewModel.loginInfo.email)
            .collection(HABIT).document(detailViewModel.badHabits.id)
            .collection(REWARDS).document(reward.id)
            .set(dataReward, SetOptions.merge())
    }

    fun getMoneyAvailableAndEarned(
        detailViewModel: DetailViewModel,
        onSuccess: (BadHabits) -> Unit
    ) {
        val docRef = db.collection(USER).document(detailViewModel.loginInfo.email)
            .collection(HABIT).document(detailViewModel.badHabits.id)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val badHabits = BadHabits(
                    snapshot.id,
                    snapshot.getField<String>(HABITS) ?: "",
                    snapshot.getField<Long>(MONEY_EARNED) ?: 0L,
                    snapshot.getField<String>(BEGIN_DATE) ?: "",
                    snapshot.getField<Long>(DAILY) ?: 0L,
                    snapshot.getField<Long>(WEEKLY) ?: 0L,
                    snapshot.getField<Long>(MONTHLY) ?: 0L,
                    snapshot.getField<Long>(TOTAL_NOT_USED) ?: 0L,
                    snapshot.getField<String>(SCORE) ?: "",
                    snapshot.getField<String>(TYPE) ?: "",
                    snapshot.getField<Long>(CONSUMPTION) ?: 0L,
                    snapshot.getField<Long>(COST) ?: 0L,
                    snapshot.getField<String>(NOTES) ?: "",
                    snapshot.getField<Long>(SPEND) ?: 0L,
                )
                onSuccess.invoke(badHabits)
            }
        }

    }

}