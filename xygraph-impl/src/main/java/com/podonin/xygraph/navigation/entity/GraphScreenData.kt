package com.podonin.xygraph.navigation.entity

import android.os.Parcelable
import com.podonin.points_count_api.domain.entity.XYPoint
import kotlinx.parcelize.Parcelize

@Parcelize
data class GraphScreenData(
    val points: List<XYPoint>
): Parcelable