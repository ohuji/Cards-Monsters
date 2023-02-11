package com.ohuji.cardsNmonsters.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val deckName: String,
)