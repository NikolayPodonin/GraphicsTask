package com.podonin.xygraph.navigation.entity

import android.os.Parcelable
import com.podonin.xygraph_api.entity.XYPoint
import kotlinx.parcelize.Parcelize

@Parcelize
data class GraphScreenData(
    val points: List<XYPoint>
): Parcelable