package com.podonin.files_saver_impl.data

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import com.podonin.files_saver_impl.domain.repository.MediaStorageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaStorageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): MediaStorageRepository {
    override suspend fun saveBitmapToMediaStore(bitmap: Bitmap): Result<String?> {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "graph_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Graphs")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val contentResolver = context.contentResolver
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        return try {
            uri?.let {
                withContext(Dispatchers.IO) {
                    contentResolver.openOutputStream(it)?.use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    }
                }
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            }
            Result.success(uri?.path)
        } catch (e: IOException) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}