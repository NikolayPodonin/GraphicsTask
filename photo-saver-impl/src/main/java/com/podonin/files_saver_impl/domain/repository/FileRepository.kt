package com.podonin.files_saver_impl.domain.repository

import android.graphics.Bitmap

interface FileRepository {
    suspend fun saveBitmapToFile(bitmap: Bitmap): Result<String?>
}