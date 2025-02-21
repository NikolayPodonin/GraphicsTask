package com.podonin.points_count_impl.domain.repository

import com.podonin.points_count_api.domain.entity.XYPoint

interface PointsCountRepository {
    suspend fun getPoints(count: Int): List<XYPoint>
}