package com.ohuji.cardsNmonsters.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Player")
data class Player(
    @PrimaryKey(autoGenerate = true)
    val playerId: Long,
    var playerLevel: Int,
    var allTimeExp: Int,
    var currentLvlExp: Int,
    var expRequirement: Int
)