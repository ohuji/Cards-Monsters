package com.ohuji.cardsNmonsters.screens.deck_building

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.ohuji.cardsNmonsters.database.Card
import com.ohuji.cardsNmonsters.database.CardNDeckCrossRef
import com.ohuji.cardsNmonsters.database.CardsNDeckRepository
import com.ohuji.cardsNmonsters.database.Deck
import com.ohuji.cardsNmonsters.database.FullDeck
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

            // Insert the new associations into the database
           repository.addCardNDeckCrossRefs(*cardNDeckCrossRefs.toTypedArray())
        }
    }

    fun getDeckWithCards(deckId: Long): LiveData<FullDeck> {
        val deck = Transformations.map(repository.getDeckWithCard(deckId)) {
            it
        }
        return deck
    }

}
/*
class DeckViewModel2(application: Application) : AndroidViewModel(application) {
    private val db = CardsNMonstersDatabase.getInstance(application)

    fun getAllDecks(): LiveData<List<Deck>> {
        val decks = Transformations.map(db.cardsNDecksDao.getAllDecks()) {
            it
        }
        return decks
    }

    fun findDeckById(deckId: Long): LiveData<Deck> {
        val deck = Transformations.map(db.cardsNDecksDao.findDeckById(deckId)) {
            it
        }
        return deck
    }

    fun getAllCards(): LiveData<List<Card>> {
        val cards = Transformations.map(db.cardsNDecksDao.getAllCards()) {
            it
        }
        return cards
    }

   fun addDeck(deckName: String) {
        val deck = Deck(0, deckName)
        viewModelScope.launch(Dispatchers.IO) { db.cardsNDecksDao.addDeck(deck) }
    }

    fun addCardsToDeck3(deckId: Long, selectedCards: List<Card>) {
        Log.d("DBG", "Tultiin addcardstodec3")
        viewModelScope.launch(Dispatchers.IO) {
            // Get a reference to the deck
            val deck = db.cardsNDecksDao.findDeckById(deckId).value?.deckId  //?: return@launch
            Log.d("DBG","tollanen deck $deck")
            // Create CardNDeckCrossRef objects to associate the selected cards with the deck
            val cardNDeckCrossRefs = selectedCards.map { CardNDeckCrossRef(deckId, it.cardId) }
            Log.d("DBG", "tollanen refse $cardNDeckCrossRefs")
            // Print out the selected cards and card IDs
            Log.d("DBG", "Selected Cards:")
            selectedCards.forEach { Log.d("DBG", "${it.cardName} - ${it.cardId}") }
            Log.d("DBG", "Card IDs:")
            cardNDeckCrossRefs.forEach { Log.d("DBG", "${it.cardId}") }

            // Insert the new associations into the database
            db.cardsNDecksDao.addCardNDeckCrossRefs(*cardNDeckCrossRefs.toTypedArray())
        }
    }

    fun getDeckWithCards(deckId: Long): LiveData<FullDeck> {
        val deck = Transformations.map(db.cardsNDecksDao.getDeckWithCard(deckId)) {
            it
        }
        return deck

    }
}

 */

/*
fun newFullDeck(deckId: Long): LiveData<FullDeck> {
        val fullDeck = MutableLiveData<FullDeck>()
        viewModelScope.launch(Dispatchers.IO) {
            val deck = db.cardsNMonstersDao.findDeckById(deckId).value
            val cards = db.cardsNMonstersDao.getFullDeck().value
                ?.find { it.deck.deckId == deckId }?.cards ?: emptyList()
            fullDeck.postValue(deck?.let { FullDeck(deck = it, cards = cards) })
        }
        return fullDeck
    }

     fun addCard(cardName: String, cardModel: String, cardElement: String, cardDamage: Int) {
        val card = Card(0, cardName, cardModel, cardElement, cardDamage)
        viewModelScope.launch(Dispatchers.IO) { db.cardsNMonstersDao.addCard(card) }
    }
 */