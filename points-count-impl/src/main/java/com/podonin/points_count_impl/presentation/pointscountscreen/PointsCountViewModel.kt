package com.podonin.points_count_impl.presentation.pointscountscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.podonin.points_count_impl.R
import com.podonin.points_count_impl.domain.PointsCountInteractor
import com.podonin.xygraph_api.navigation.GraphScreenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PointsCountViewModel @Inject constructor(
    private val router: Router,
    private val graphScreenProvider: GraphScreenProvider,
    private val pointsCountInteractor: PointsCountInteractor
) : ViewModel() {

    private val _showToast = MutableSharedFlow<Int>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val showToast = _showToast.asSharedFlow()

    private val _showLoader = MutableSharedFlow<Boolean>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val showLoader = _showLoader.asSharedFlow()

    fun requestPoints(count: Int) {
        _showLoader.tryEmit(true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = pointsCountInteractor.getPoints(count)
            val xyPoints = result.getOrNull()
            if (result.isSuccess && xyPoints?.isNotEmpty() == true) {
                router.navigateTo(
                    graphScreenProvider.getXYGraphScreen(xyPoints)
                )
            } else {
                _showToast.emit(R.string.points_unexpected_error)
            }
            _showLoader.emit(false)
        }
    }
}
