package com.podonin.xygraph.presentation.graphscreen

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.podonin.files_saver_api.domain.PhotoSaverInteractor
import com.podonin.files_saver_api.domain.entity.BitmapHolder
import com.podonin.xygraph.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor(
    private val photoSaverInteractor: PhotoSaverInteractor
) : ViewModel() {

    private val _showToast = MutableSharedFlow<Int>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val showToast = _showToast.asSharedFlow()

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
