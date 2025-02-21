package com.podonin.points_count_impl.domain

import com.podonin.points_count_api.domain.entity.XYPoint
import com.podonin.points_count_impl.domain.repository.PointsCountRepository
import javax.inject.Inject

class PointsCountInteractorImpl @Inject constructor(
    private val pointsCountRepository: PointsCountRepository
) : PointsCountInteractor {

    override suspend fun getPoints(count: Int): Result<List<XYPoint>> {
        return runCatching {
            pointsCountRepository.getPoints(count)
        }
    }
}