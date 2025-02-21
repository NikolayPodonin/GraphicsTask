package com.podonin.xygraph.presentation.graphscreen

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.podonin.common_ui.resources_provider.ResourcesProvider
import com.podonin.files_saver_api.domain.PhotoSaverInteractor
import com.podonin.files_saver_api.domain.entity.BitmapHolder
import com.podonin.points_count_api.domain.entity.XYPoint
import com.podonin.xygraph.R
import com.podonin.xygraph.navigation.entity.GraphScreenData
import com.podonin.xygraph.presentation.graphscreen.table.TableItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor(
    private val photoSaverInteractor: PhotoSaverInteractor,
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {

    private val _items = MutableStateFlow<List<TableItem>>(emptyList())
    val items = _items.asStateFlow()

    private val _points = MutableStateFlow<List<XYPoint>>(emptyList())
    val points = _points.asStateFlow()

    private val _showToast = MutableSharedFlow<Int>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val showToast = _showToast.asSharedFlow()

    fun setScreenData(data: GraphScreenData?) {
        _points.value = data?.points.orEmpty().sortedBy {
            it.x
        }
        _items.value = listOf(
            TableItem.TitleItem(
                title = resourcesProvider.getString(R.string.table_index),
                xLabel = resourcesProvider.getString(R.string.table_x_label),
                yLabel = resourcesProvider.getString(R.string.table_y_label)
            )
        ) + _points.value.map {
            TableItem.PointItem(it)
        }
    }

    fun saveGraphToGallery(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = photoSaverInteractor.savePhoto(BitmapHolder(bitmap))
            if (result.isSuccess) {
                _showToast.emit(R.string.toast_saved_graph)
            } else {
                _showToast.emit(R.string.toast_failed_to_save_graph)
            }
        }
    }
}
