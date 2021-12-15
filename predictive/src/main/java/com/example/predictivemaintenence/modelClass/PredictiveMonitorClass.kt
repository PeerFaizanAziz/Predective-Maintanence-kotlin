package com.example.predictivemaintenence.modelClass

data class PredictiveMonitorClass(val className: String,  val id: Long = 0)
//    val classDescription:String,
//    val workspace:WorkspaceClass,

{
    override fun toString(): String {
        return className
    }
}
