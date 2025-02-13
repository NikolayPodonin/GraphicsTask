package com.podonin.points_count_impl.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.podonin.points_count_api.navigation.PointsCountScreenProvider
import com.podonin.points_count_impl.presentation.pointscountscreen.PointsCountFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointsCountScreenProviderImpl @Inject constructor() : PointsCountScreenProvider {
    override fun getPointsCountScreen() = object : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment {
            return PointsCountFragment.newInstance()
        }
    }
}