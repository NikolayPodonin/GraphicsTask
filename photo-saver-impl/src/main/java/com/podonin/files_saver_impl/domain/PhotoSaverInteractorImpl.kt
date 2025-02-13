package com.podonin.files_saver_impl.domain

import android.os.Build
import com.podonin.files_saver_api.domain.PhotoSaverInteractor
import com.podonin.files_saver_api.domain.entity.BitmapHolder
import com.podonin.files_saver_impl.domain.repository.FileRepository
import com.podonin.files_saver_impl.domain.repository.MediaStorageRepository
import javax.inject.Inject

class PhotoSaverInteractorImpl @Inject constructor(
    private val fileRepository: FileRepository,
    private val mediaStorageRepository: MediaStorageRepository
): PhotoSaverInteractor {
    override suspend fun <Bitmap> savePhoto(bitmapHolder: BitmapHolder<in Bitmap>): Result<String?> {
        val bitmap = bitmapHolder.bitmap as? android.graphics.Bitmap
        if (bitmap == null) return Result.failure(IllegalArgumentException("No bitmap provided"))

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mediaStorageRepository.saveBitmapToMediaStore(bitmap) // API 29+
        } else {
            fileRepository.saveBitmapToFile(bitmap) // API 28 и ниже
        }
    }
}