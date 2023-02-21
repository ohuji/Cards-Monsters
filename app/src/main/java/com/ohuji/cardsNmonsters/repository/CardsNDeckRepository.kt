package com.ohuji.cardsNmonsters.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ohuji.cardsNmonsters.database.CardNDeckCrossRef
import com.ohuji.cardsNmonsters.database.CardsNMonstersDatabase
import com.ohuji.cardsNmonsters.database.Deck
import com.ohuji.cardsNmonsters.database.FullDeck
import com.ohuji.cardsNmonsters.database.entities.Card

class CardsNDeckRepository( application: Application) {
    private val db = CardsNMonstersDatabase.getInstance(application)
    private val cardsNDeckDao = db.cardsNDecksDao

    val allCards: LiveData<List<Card>> = cardsNDeckDao.getAllCards()

    val allDecks: LiveData<List<Deck>> = cardsNDeckDao.getAllDecks()

    fun findDeckById(deckId: Long): LiveData<Deck> {
        return cardsNDeckDao.findDeckById(deckId)
    }

    fun findCardById(cardId: Long): LiveData<Card> {
        return cardsNDeckDao.findCardById(cardId)
    }

    fun addDeck(vararg deck: Deck) {
        db.databaseWriteExecutor.execute { cardsNDeckDao.addDeck(*deck) }
    }

    fun deleteDeck(deck: Deck) {
        db.databaseWriteExecutor.execute { cardsNDeckDao.deleteDeck(deck) }
    }

    fun deleteFullDeck(deckId: Long) {
        db.databaseWriteExecutor.execute { cardsNDeckDao.deleteFullDeckById(deckId) }
    }
    fun addCardNDeckCrossRefs(vararg cardNDeckCrossRef: CardNDeckCrossRef) {
        db.databaseWriteExecutor.execute { cardsNDeckDao.addCardNDeckCrossRefs(*cardNDeckCrossRef) }
    }

    fun getFullDeck(): LiveData<List<FullDeck>> {
        return cardsNDeckDao.getFullDeck()
    }

    fun getDeckWithCard(deckId: Long): LiveData<FullDeck> {
        return cardsNDeckDao.getDeckWithCard(deckId)
    }
}


/*
//TESTING
suspend fun getCardCountsInDeck(deckId: Long): Map<Long, Int> {
        val cardsInDeck = cardsNDeckDao.getDeckWithCard(deckId)
        return cardsInDeck. groupBy { it.cardId }.mapValues { it.value.size }
    }
    suspend fun getCardsInDeck(deckId: Long): List<Card> {
        val cardNDeckCrossRefs = cardsNDeckDao.getCardNDeckCrossRefsForDeck(deckId)
        val cardIds = cardNDeckCrossRefs.map { it.cardId }
        return cardsNDeckDao.getCardsByIds(cardIds)
    }

    suspend fun updateCardNDeckCrossRefs(vararg cardNDeckCrossRefs: CardNDeckCrossRef) {
        withContext(Dispatchers.IO) {
            cardNDeckCrossRefs.forEach { cardNDeckCrossRef ->
                val existingRef = cardsNDeckDao.getCardNDeckCrossRef(cardNDeckCrossRef.deckId, cardNDeckCrossRef.cardId)
                // Update the existing reference with the new count
                existingRef.count += cardNDeckCrossRef.count
                cardsNDeckDao.updateCardNDeckCrossRef(existingRef)
            }
        }
    }
 */