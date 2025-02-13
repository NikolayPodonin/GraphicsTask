package com.podonin.files_saver_api.domain.entity

// Поскольку модуль не андроидовский, про Bitmap и Parcelable он ничего не знает,
// так что создадим обертку для Bitmap и будем ей оперировать
data class BitmapHolder<Bitmap>(
    val bitmap: Bitmap
)