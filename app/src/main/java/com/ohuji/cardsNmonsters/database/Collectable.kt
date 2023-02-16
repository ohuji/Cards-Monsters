package com.ohuji.cardsNmonsters.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Collectable")
data class Collectable(
    @PrimaryKey(autoGenerate = true)
    val collectableId: Long,
    val collectableName: String,
    val collectableModel: String,
    val currentProgress: Int,
    val requirements: Int,
    val unlocked: Boolean,
)