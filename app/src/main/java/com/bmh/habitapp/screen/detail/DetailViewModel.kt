package com.bmh.habitapp.screen.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bmh.habitapp.manager.FirestoreManager
import com.bmh.habitapp.model.BadHabits
import com.bmh.habitapp.model.UserInfo
import java.lang.Exception

class DetailViewModel : ViewModel() {

    init {
        try {
            getUser()
        } catch (e: Exception) {

        }
    }

    private val _email = MutableLiveData<String>()

    val email = _email

    var badHabits: BadHabits = BadHabits()
    var loginInfo: UserInfo = UserInfo()

    fun getUser() {
        FirestoreManager().getUserInfo(onSuccess = {
            loginInfo = it
            _email.value = it.email
        })
    }
}