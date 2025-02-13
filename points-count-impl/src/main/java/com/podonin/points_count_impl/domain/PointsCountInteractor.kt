package com.podonin.points_count_impl.domain

import com.podonin.points_count_api.domain.entity.XYPoint

interface PointsCountInteractor {
    suspend fun getPoints(count: Int): Result<List<XYPoint>>
}