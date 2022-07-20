package com.example.energybar

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "energy")
data class Energy(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "start")
    var start: Int,

    @ColumnInfo(name = "end")
    var end: Int,

    @ColumnInfo(name = "color") @ColorInt
    var color: Int
)
