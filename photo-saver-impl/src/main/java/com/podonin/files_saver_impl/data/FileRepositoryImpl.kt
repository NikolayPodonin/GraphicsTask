package com.podonin.files_saver_impl.data

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import com.podonin.files_saver_impl.domain.repository.FileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
): FileRepository {
    override suspend fun saveBitmapToFile(bitmap: Bitmap): Result<String?> {
        val filename = "graph_${System.currentTimeMillis()}.png"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename)

        return try {
            withContext(Dispatchers.IO) {
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
            }
            MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), arrayOf("image/png"), null)
            Result.success(Uri.fromFile(file).path)
        } catch (e: IOException) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}