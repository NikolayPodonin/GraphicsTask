package com.podonin.graphicstask.presentation.main

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.podonin.points_count_api.navigation.PointsCountScreenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val router: Router,
    private val pointsCountScreenProvider: PointsCountScreenProvider
) : ViewModel() {

    fun navigateToFeatureScreen() {
        router.replaceScreen(
            pointsCountScreenProvider.getPointsCountScreen()
        )
    }
}
