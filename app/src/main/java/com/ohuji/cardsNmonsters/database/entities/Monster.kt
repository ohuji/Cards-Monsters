package com.ohuji.cardsNmonsters.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Monster")
data class Monster(
    @PrimaryKey(autoGenerate = true)
    val monsterId: Long,
    val monsterName: String,
    val monsterModel: String,
    val monsterElement: String,
    val monsterHealth: Int,
)