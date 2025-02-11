package com.podonin.xygraph.presentation.table

import com.podonin.xygraph_api.entity.XYPoint

sealed class TableItem {
    abstract val title: String
    abstract val xLabel: String
    abstract val yLabel: String

    data class TitleItem(
        override val title: String,
        override val xLabel: String,
        override val yLabel: String
    ) : TableItem()

    data class PointItem(
        private val point: XYPoint
    ) : TableItem() {
        override val title: String = point.index.toString()
        override val yLabel: String = point.y.toString()
        override val xLabel: String = point.x.toString()
    }
}