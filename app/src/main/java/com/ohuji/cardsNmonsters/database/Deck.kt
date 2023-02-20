package com.ohuji.cardsNmonsters.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.ohuji.cardsNmonsters.database.entities.Card

@Entity
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val deckId: Long,
    val deckName: String,
)

data class FullDeck(
    @Embedded val deck: Deck,
    @Relation(
        parentColumn = "deckId",
        entityColumn = "cardId",
        associateBy = Junction(CardNDeckCrossRef::class)
    )
    val cards: List<Card>
)

@Entity(primaryKeys = ["deckId", "cardId"])
data class CardNDeckCrossRef(
    val deckId: Long,
    val cardId: Long

)

@Entity(primaryKeys = ["bookId","authorId"])
data class BookAndAuthorEntity (
    val bookId: Long,
    val authorId: Long
)
