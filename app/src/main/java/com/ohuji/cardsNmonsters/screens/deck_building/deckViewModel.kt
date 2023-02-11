package com.ohuji.cardsNmonsters.screens.deck_building

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.ohuji.cardsNmonsters.database.Card
import com.ohuji.cardsNmonsters.database.CardsNMonstersDatabase
import com.ohuji.cardsNmonsters.database.Deck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DeckViewModel(application: Application) : AndroidViewModel(application) {
    val db = CardsNMonstersDatabase.getInstance(application)

    fun getAllDecks(): LiveData<List<Deck>> {
        val decks = Transformations.map(db.cardsNMonstersDao.getAllDecks()) {
            it
        }
        return decks
    }

    fun findDeckById(deckId: Long): LiveData<Deck> {
        val deck = Transformations.map(db.cardsNMonstersDao.findDeckById(deckId)) {
            it
        }
        return deck
    }

    fun findCardsIn(deckId: Long): LiveData<List<Card>> {
        val cards = Transformations.map(db.cardsNMonstersDao.getCardsFromADeck(deckId)) {
            it
        }
        return cards
    }

    fun addCard(cardName: String, cardModel: String, cardElement: String, cardDamage: Int, decksIn: Long) {
        val card = Card(0, cardName, cardModel, cardElement, cardDamage, decksIn)
        viewModelScope.launch(Dispatchers.IO) { db.cardsNMonstersDao.addCard(card) }
    }

    fun addMovie(deckName: String) {
        val deck = Deck(0, deckName)
        viewModelScope.launch(Dispatchers.IO) { db.cardsNMonstersDao.addDeck(deck) }
    }
}