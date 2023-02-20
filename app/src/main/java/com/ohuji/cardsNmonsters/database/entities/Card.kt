package com.ohuji.cardsNmonsters.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Card")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val cardId: Long,
    val cardName: String,
    val cardModel: String,
    val cardElement: String,
    val cardDamage: Int,
    )
