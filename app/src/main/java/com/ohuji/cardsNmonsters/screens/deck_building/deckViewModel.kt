package com.ohuji.cardsNmonsters.screens.deck_building

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.ohuji.cardsNmonsters.database.CardNDeckCrossRef
import com.ohuji.cardsNmonsters.database.Deck
import com.ohuji.cardsNmonsters.database.FullDeck
import com.ohuji.cardsNmonsters.database.entities.Card
import com.ohuji.cardsNmonsters.repository.CardsNDeckRepository
import kotlinx.coroutines.launch

class DeckViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CardsNDeckRepository(application)

    /**
     * Returns all decks in a LiveData list
     */
    fun getAllDecks(): LiveData<List<Deck>> {
        return repository.allDecks
    }

    fun findDeckById(deckId: Long): LiveData<Deck> {
        return repository.findDeckById(deckId)
    }

    /**
     * Returns all cards in a LiveData list
     */
    fun getAllCards(): LiveData<List<Card>> {
        return repository.allCards
    }

    /**
     * Adds a new deck
     */
    fun addDeck(deckName: String) {
        val deck = Deck(0, deckName)
        viewModelScope.launch { repository.addDeck(deck) }
    }

    /**
     * Adds cards to deck that are on the selectedCards list
     */
    fun addCardsToDeck(deckId: Long, selectedCards: List<Card>) {
        viewModelScope.launch {

            val deck = repository.findDeckById(deckId).value?.deckId
            Log.d("DBG","tollanen deck $deck")

            val cardNDeckCrossRefs = selectedCards.map { CardNDeckCrossRef(deckId, it.cardId) }

            selectedCards.forEach { Log.d("DBG", "${it.cardName} - ${it.cardId}") }
            Log.d("DBG", "Card IDs:")
            cardNDeckCrossRefs.forEach { Log.d("DBG", "${it.cardId}") }
           repository.addCardNDeckCrossRefs(*cardNDeckCrossRefs.toTypedArray())
        }
    }

    /**
     * Returns deck based on id
     */
    fun getDeckWithCards(deckId: Long): LiveData<FullDeck> {
        val deck = Transformations.map(repository.getDeckWithCard(deckId)) {
            it
        }
        return deck
    }

    /**
     * Deletes a deck based on id
     */
    fun deleteFullDeck(deckId: Long) {
        repository.deleteFullDeck(deckId)
    }
}