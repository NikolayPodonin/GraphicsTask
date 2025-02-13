package com.podonin.files_saver_api.domain

import com.podonin.files_saver_api.domain.entity.BitmapHolder

interface PhotoSaverInteractor {
    suspend fun <Bitmap> savePhoto(bitmapHolder: BitmapHolder<in Bitmap>): Result<String?>
}