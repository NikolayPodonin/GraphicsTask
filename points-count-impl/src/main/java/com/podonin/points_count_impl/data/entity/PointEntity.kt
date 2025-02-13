package com.podonin.points_count_impl.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class PointEntity(
    val x: Float,
    val y: Float
)