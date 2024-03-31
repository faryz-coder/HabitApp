package com.bmh.habitapp.viewModel

import androidx.lifecycle.ViewModel
import com.bmh.habitapp.manager.BiometricManager

class MainViewModel: ViewModel() {
    lateinit var biometricManager: BiometricManager
}