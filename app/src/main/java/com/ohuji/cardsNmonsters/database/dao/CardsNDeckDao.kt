package com.ohuji.cardsNmonsters.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ohuji.cardsNmonsters.database.entities.Card
import com.ohuji.cardsNmonsters.database.CardNDeckCrossRef
import com.ohuji.cardsNmonsters.database.Deck
import com.ohuji.cardsNmonsters.database.FullDeck


@Dao
interface CardsNDeckDao {

    // Card related
    @Insert
    fun addCard(vararg card: Card)

    @Delete
    fun deleteCard(vararg card: Card)

    @Query("select * from Card")
    fun getAllCards(): LiveData<List<Card>>

    @Update
    fun updateCard(card: Card)

    // Deck related
    @Query("SELECT * FROM Deck WHERE deckId = :deckId")
    fun findDeckById(deckId: Long): LiveData<Deck>

    @Query("SELECT * FROM Card WHERE cardId = :cardId")
    fun findCardById(cardId: Long): LiveData<Card>

    @Insert
    fun addDeck(vararg deck: Deck)

    @Delete
    fun deleteDeck(deck: Deck)

    @Query("DELETE FROM Deck WHERE deckId = :deckId")
    fun deleteFullDeckById(deckId: Long)

    @Query("select * from Deck")
    fun getAllDecks(): LiveData<List<Deck>>

    // Full deck related

    @Insert
    fun addCardToDeck(cardNDeckCrossRef: CardNDeckCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCardNDeckCrossRefs(vararg cardNDeckCrossRef: CardNDeckCrossRef)
    @Transaction
    @Query("SELECT * FROM Deck")
    fun getFullDeck(): LiveData<List<FullDeck>>

    @Transaction
    @Query("SELECT * FROM Deck WHERE deckId = :deckId")
    fun getDeckWithCard(deckId: Long): LiveData<FullDeck>

}

/*
 //TESTING
    // Get all CardNDeckCrossRef objects associated with a specific deck
    @Transaction
    suspend fun updateCardNDeckCrossRefs(cardNDeckCrossRefs: List<CardNDeckCrossRef>) {
        for (cardNDeckCrossRef in cardNDeckCrossRefs) {
            val count = getCountInDeck(cardNDeckCrossRef.cardId, cardNDeckCrossRef.deckId) ?: 0
            cardNDeckCrossRef.count = count
        }
        updateCardNDeckCrossRefs(cardNDeckCrossRefs)
    }

    @Query("SELECT COUNT(*) FROM cardndeckcrossref WHERE cardId = :cardId AND deckId = :deckId")
    suspend fun getCountInDeck(cardId: Long, deckId: Long): Int?

    @Query("SELECT * FROM CardNDeckCrossRef WHERE deckId = :deckId")
    suspend fun getCardNDeckCrossRefsForDeck(deckId: Long): List<CardNDeckCrossRef>

    // Get all cards associated with a list of CardNDeckCrossRef objects
    @Query("SELECT * FROM Card WHERE cardId IN (:cardIds)")
    suspend fun getCardsByIds(cardIds: List<Long>): List<Card>
    @Insert
    fun addCardToDeck(cardNDeckCrossRef: CardNDeckCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCardNDeckCrossRefs(vararg cardNDeckCrossRef: CardNDeckCrossRef)

    @Query("SELECT * FROM CardNDeckCrossRef WHERE deckId = :deckId AND cardId = :cardId")
    fun getCardNDeckCrossRef(deckId: Long, cardId: Long): CardNDeckCrossRef

    @Update
    fun updateCardNDeckCrossRef(cardNDeckCrossRef: CardNDeckCrossRef)

    @Transaction
    @Query("SELECT * FROM Deck")
    fun getFullDeck(): LiveData<List<FullDeck>>

    @Transaction
    @Query("SELECT * FROM Deck WHERE deckId = :deckId")
    fun getDeckWithCard(deckId: Long): LiveData<FullDeck>
 */
