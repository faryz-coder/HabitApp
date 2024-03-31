package com.bmh.habitapp.screen.home

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import androidx.navigation.NavHostController
import com.bmh.habitapp.manager.SharePreferencesManager
import com.bmh.habitapp.model.BadHabits
import com.bmh.habitapp.screen.detail.DetailViewModel
import com.bmh.habitapp.viewModel.MainViewModel

class TestHabitItem(
    context: Context,
    val navHostController: NavHostController,
    val mainViewModel: MainViewModel,
    val sharePref: SharePreferencesManager,
    val item: BadHabits,
    val detailViewModel: DetailViewModel,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    @Composable
    override fun Content() {
        HabitsItem(
            navHostController = navHostController,
            mainViewModel = mainViewModel,
            sharePref = sharePref,
            badHabits = item,
            detailViewModel = detailViewModel
        )
    }

}