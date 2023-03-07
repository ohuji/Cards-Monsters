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
     * Retrieves all decks from the repository as a LiveData object.
     *
     * @return A LiveData object containing a list of all decks.
     */
    fun getAllDecks(): LiveData<List<Deck>> {
        return repository.allDecks
    }

    /**
     * Finds a deck in the repository by its unique ID and returns it as a LiveData object.
     *
     * @param deckId The unique ID of the deck to find.
     * @return A LiveData object containing the deck with the specified ID.
     */
    fun findDeckById(deckId: Long): LiveData<Deck> {
        return repository.findDeckById(deckId)
    }

    /**
     * Returns the [LiveData] object that holds a list of all the [Deck] objects
     * currently stored in the repository.
     *
     * @return The [LiveData] object containing a list of all [Deck]s.
     */
    fun getAllCards(): LiveData<List<Card>> {
        return repository.allCards
    }

    /**
     * Adds a new [Deck] object with the specified [deckName] to the repository.
     *
     * @param deckName The name of the new [Deck] object to be added.
     */
    fun addDeck(deckName: String) {
        val deck = Deck(0, deckName)
        viewModelScope.launch { repository.addDeck(deck) }
    }

    /**
     * Adds a list of [Card] objects with the specified [deckId] to the repository.
     *
     * @param deckId The ID of the [Deck] object to which the [Card] objects will be added.
     * @param selectedCards The list of [Card] objects to be added to the [Deck].
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
     * Returns a [LiveData] object that contains a [FullDeck] object that has the
     * [Deck] and all the [Card] objects associated with it.
     *
     * @param deckId The ID of the [Deck] object for which to retrieve the [FullDeck] object.
     * @return A [LiveData] object containing a [FullDeck] object for the specified [Deck] object.
     */
    fun getDeckWithCards(deckId: Long): LiveData<FullDeck> {
        val deck = Transformations.map(repository.getDeckWithCard(deckId)) {
            it
        }
        return deck
    }

    /**
     * Deletes the [Deck] object with the specified [deckId] from the repository.
     *
     * @param deckId The ID of the [Deck] object to be deleted.
     */
    fun deleteFullDeck(deckId: Long) {
        repository.deleteFullDeck(deckId)
    }
}