package com.podonin.points_count_impl.domain.repository

import com.podonin.points_count_impl.data.entity.PointEntity

interface PointsCountRepository {
    suspend fun getPoints(count: Int): List<PointEntity>
}