package com.ohuji.cardsNmonsters.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Database(entities = [Deck::class, Card::class], version = 2, exportSchema = false)
abstract class CardsNMonstersDatabase: RoomDatabase() {
    abstract val cardsNMonstersDao: CardsNMonstersDao
    companion object {
        @Volatile
        private var INSTANCE: CardsNMonstersDatabase? = null
        fun getInstance(context: Context): CardsNMonstersDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(context,
                        CardsNMonstersDatabase::class.java, "movie_database")
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

@Dao
interface CardsNMonstersDao {

    @Insert
    fun addCard(vararg card: Card)

    @Delete
    fun deleteCard(vararg card: Card)

    @Query("select * from Card")
    fun getAllCards(): LiveData<List<Card>>

    @Query("select * from Card where decksIn = :deckId")
    fun getCardsFromADeck(deckId: Long): LiveData<List<Card>>
    data class CardIn(val cardName: String?)

    @Query("SELECT * FROM Deck WHERE Id = :deckId")
    fun findDeckById(deckId: Long): LiveData<Deck>

    @Insert
    fun addDeck(vararg deck: Deck)

    @Delete
    fun deleteDeck(deck: Deck)

    @Query("select * from Deck")
    fun getAllDecks(): LiveData<List<Deck>>
}
