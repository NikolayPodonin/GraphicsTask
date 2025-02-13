package com.podonin.files_saver_impl.domain.repository

import android.graphics.Bitmap

interface MediaStorageRepository {
    suspend fun saveBitmapToMediaStore(bitmap: Bitmap): Result<String?>
}