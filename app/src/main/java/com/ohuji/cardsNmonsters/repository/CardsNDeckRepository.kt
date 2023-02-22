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