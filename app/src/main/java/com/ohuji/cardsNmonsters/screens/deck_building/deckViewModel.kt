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

    fun getAllDecks(): LiveData<List<Deck>> {
        return repository.allDecks
    }

    fun findDeckById(deckId: Long): LiveData<Deck> {
        return repository.findDeckById(deckId)
    }

    fun getAllCards(): LiveData<List<Card>> {
        return repository.allCards
    }

    fun addDeck(deckName: String) {
        val deck = Deck(0, deckName)
        viewModelScope.launch { repository.addDeck(deck) }
    }

    fun addCardsToDeck(deckId: Long, selectedCards: List<Card>) {
        Log.d("DBG", "Tultiin addcardstodec3")
        viewModelScope.launch {
            // Get a reference to the deck
            val deck = repository.findDeckById(deckId).value?.deckId   //?: return@launch
            Log.d("DBG","tollanen deck $deck")
            // Create CardNDeckCrossRef objects to associate the selected cards with the deck
            val cardNDeckCrossRefs = selectedCards.map { CardNDeckCrossRef(deckId, it.cardId) }
            Log.d("DBG", "tollanen refse $cardNDeckCrossRefs")
            // Print out the selected cards and card IDs
            Log.d("DBG", "Selected Cards:")
            selectedCards.forEach { Log.d("DBG", "${it.cardName} - ${it.cardId}") }
            Log.d("DBG", "Card IDs:")
            cardNDeckCrossRefs.forEach { Log.d("DBG", "${it.cardId}") }

           repository.addCardNDeckCrossRefs(*cardNDeckCrossRefs.toTypedArray())
        }
    }

    fun getDeckWithCards(deckId: Long): LiveData<FullDeck> {
        val deck = Transformations.map(repository.getDeckWithCard(deckId)) {
            it
        }
        return deck
    }

    fun deleteFullDeck(deckId: Long) {
        repository.deleteFullDeck(deckId)
    }
}