package com.podonin.points_count_impl.data

import com.podonin.points_count_impl.data.entity.PointsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/test/points")
    suspend fun getPoints(@Query("count") count: Int): PointsResponse
}