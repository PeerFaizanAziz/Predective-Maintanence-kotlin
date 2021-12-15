package com.example.predictivemaintenence

import com.example.predictivemaintenence.modelClass.PredictiveMonitorClass
import com.example.predictivemaintenence.modelClass.SearchEquipmentClass
import com.example.predictivemaintenence.modelClass.ThrushHoldValueClass
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    //search equipment
    @GET("equip/search")
    fun getEquipment(
        @Header("workspace") workspace: String,
        @Header("Authorization") token: String,
        @Query("query") string: String?
    ): Call<List<SearchEquipmentClass>>


    //get monitor list
    @GET("predictive/monitorList")
    fun getMonitorList(
        @Header("workspace") workspace: String,
        @Header("Authorization") token: String
    ): Call<List<PredictiveMonitorClass>>

    //check equipment is created
    @GET("predictive/view/{id}")
    fun checkEquipmentCreated(
        @Header("workspace") workspace: String,
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<ThrushHoldValueClass>

    //create equipment
    @POST("predictive/save")
    fun getCreate(
        @Header("workspace") workspace: String,
        @Header("Authorization") token: String,
        @Body thrushHoldValueClass: ThrushHoldValueClass
    ): Call<Void>

    //update equipment
    @POST("predictive/update")
    fun getupdate(
        @Header("workspace") workspace: String,
        @Header("Authorization") token: String,
        @Body thrushHoldValueClass: ThrushHoldValueClass
    ): Call<Void>

    //delete monitor
    @POST("predictive/deletemonitor/{id}/{pmId}")
    fun getDeleteMonitor(
        @Header("workspace") workspace: String,
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Path("pmId") pmId: Long
    ): Call<Void>

    //delete param
    @POST("predictive/deleteParam/{id}/{pmId}")
    fun getDeleteParam(
        @Header("workspace") workspace: String,
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Path("pmId") pmId: Long
    ): Call<Void>

}

