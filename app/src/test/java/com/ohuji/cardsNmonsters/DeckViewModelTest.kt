package com.ohuji.cardsNmonsters

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ohuji.cardsNmonsters.database.CardNDeckCrossRef
import com.ohuji.cardsNmonsters.database.Deck
import com.ohuji.cardsNmonsters.database.FullDeck
import com.ohuji.cardsNmonsters.database.entities.Card
import com.ohuji.cardsNmonsters.repository.CardsNDeckRepository
import com.ohuji.cardsNmonsters.screens.deck_building.DeckViewModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class DeckViewModelTest {
    private lateinit var deckViewModel: DeckViewModel
    private lateinit var mockRepository: CardsNDeckRepository
    private lateinit var mockApplication: Application

    @Before
    fun setUp() {
        mockApplication = mock(Application::class.java)
        mockRepository = mock(CardsNDeckRepository::class.java)
        deckViewModel = DeckViewModel(mockApplication)
        deckViewModel.repository = mockRepository
    }

    @Test
    fun testGetAllDecks() {
        // Create a list of decks to be returned by the mock repository
        val decks = listOf(Deck(1, "Deck 1"), Deck(2, "Deck 2"))
        val liveDataDecks = MutableLiveData<List<Deck>>()
        liveDataDecks.value = decks
        `when`(mockRepository.allDecks).thenReturn(liveDataDecks)

        // Observe the LiveData returned by getAllDecks
        val observer = mock(Observer::class.java) as Observer<List<Deck>>
        deckViewModel.getAllDecks().observeForever(observer)

        // Verify that the observer's onChanged method was called with the correct data
        verify(observer).onChanged(decks)
    }

    @Test
    fun testFindDeckById() {
        // Create a deck to be returned by the mock repository
        val deck = Deck(1, "Deck 1")
        val liveDataDeck = MutableLiveData<Deck>()
        liveDataDeck.value = deck
        `when`(mockRepository.findDeckById(1)).thenReturn(liveDataDeck)

        // Observe the LiveData returned by findDeckById
        val observer = mock(Observer::class.java) as Observer<Deck>
        deckViewModel.findDeckById(1).observeForever(observer)

        // Verify that the observer's onChanged method was called with the correct data
        verify(observer).onChanged(deck)
    }

    @Test
    fun testAddDeck() {
        val deckName = "New Deck"

        // Call the addDeck method
        deckViewModel.addDeck(deckName)

        // Verify that the repository's addDeck method was called with the correct parameters
        verify(mockRepository).addDeck(Deck(0, deckName))
    }

    @Test
    fun testAddCardsToDeck() {
        // Create a list of selected cards
        val selectedCards = listOf(
            Card(1, "Card 1", "kuva", "Fire", 100),
            Card(2, "Card 2", "kuva", "Fire", 100)
        )

        // Call the addCardsToDeck method
        deckViewModel.addCardsToDeck(1, selectedCards)

        // Verify that the repository's addCardNDeckCrossRefs method was called with the correct parameters
        val expectedCrossRefs = selectedCards.map { CardNDeckCrossRef(1, it.cardId) }
        verify(mockRepository).addCardNDeckCrossRefs(*expectedCrossRefs.toTypedArray())
    }

    @Test
    fun testGetDeckWithCards() {
        // Create a FullDeck object to be returned by the mock repository
        val fullDeck = FullDeck(
            deck = Deck(1, "Deck 1"),
            cards = listOf(
                Card(1, "Card 1", "kuva", "Fire", 100),
                Card(2, "Card 2", "kuva", "Water", 200)
            )
        )
        val liveDataFullDeck = MutableLiveData<FullDeck>()
        liveDataFullDeck.value = fullDeck
        `when`(mockRepository.getDeckWithCard(1)).thenReturn(liveDataFullDeck)}}

        // Observe the LiveData returned by getDeckWithCards
       // val observer = mock
