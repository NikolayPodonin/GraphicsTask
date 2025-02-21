package com.podonin.points_count_impl.data

import com.podonin.points_count_api.domain.entity.XYPoint
import com.podonin.points_count_impl.data.entity.PointEntity
import com.podonin.points_count_impl.domain.repository.PointsCountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointsCountRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): PointsCountRepository {
    override suspend fun getPoints(count: Int): List<XYPoint> {
        return apiService.getPoints(count).points.mapIndexed { index, pointEntity ->
            XYPoint(index, pointEntity.x, pointEntity.y)
        }
    }
}