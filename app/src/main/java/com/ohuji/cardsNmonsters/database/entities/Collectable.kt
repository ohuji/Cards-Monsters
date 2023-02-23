package com.ohuji.cardsNmonsters.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Collectable")
data class Collectable(
    @PrimaryKey(autoGenerate = true)
    val collectableId: Long,
    val collectableName: String,
    val collectableModel: String,
    var currentProgress: Int,
    val requirements: Int,
    var unlocked: Boolean,
    val type: String
)