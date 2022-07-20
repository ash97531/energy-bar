package com.example.energybar

import androidx.room.*


@Dao
interface EnergyDao {

    @Query("SELECT * FROM energy")
    suspend fun getAll(): List<Energy>

    @Insert
    suspend fun insert(energy: Energy)

    @Query("DELETE FROM energy WHERE start LIKE :start")
    suspend fun delete(start: Int)

    @Query ("DELETE FROM energy")
    suspend fun deleteAll()

    @Query("UPDATE energy SET `end` =:end, color =:color WHERE start =:start")
    suspend fun update(start: Int, end: Int, color: Int)

}