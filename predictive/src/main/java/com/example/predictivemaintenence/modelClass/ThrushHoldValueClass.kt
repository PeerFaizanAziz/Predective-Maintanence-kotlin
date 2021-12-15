package com.example.predictivemaintenence.modelClass

import java.io.Serializable

data class ThrushHoldValueClass(
    val id :Long?,
    val createdDate:String?,
    val equipment:EquipmentClass,
    val comments:String,
    val taskDescription:String,
    val fileName:String?,
    val user:User,
    val parameterList: ArrayList<ParameterModelClass>,
    val monitorPointList: ArrayList<MonitorModelClass>

):Serializable
