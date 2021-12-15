package com.example.predictivemaintenence.modelClass

import java.time.LocalDate

data class MonitorModelClass (
    val id:Long?,
    val monitorType: String,
    val unit: String,
    val date:String,
    val lowValue:Double,
    val highValue:Double,
    val actualValue:Double,
    val readingDelta:Double,
    val predictiveMonClass:PredictiveMonitorClass
)