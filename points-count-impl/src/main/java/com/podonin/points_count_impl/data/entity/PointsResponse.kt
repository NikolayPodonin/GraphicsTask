package com.podonin.points_count_impl.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class PointsResponse(
    val points: List<PointEntity>
)