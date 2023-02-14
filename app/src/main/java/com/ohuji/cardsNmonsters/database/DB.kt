package com.ohuji.cardsNmonsters.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Database(entities = [Deck::class, Card::class, CardNDeckCrossRef::class], version = 2, exportSchema = false)
abstract class CardsNMonstersDatabase: RoomDatabase() {
    abstract val cardsNMonstersDao: CardsNMonstersDao
    companion object {
        @Volatile
        private var INSTANCE: CardsNMonstersDatabase? = null
        fun getInstance(context: Context): CardsNMonstersDatabase {
            if(INSTANCE == null) {
            synchronized(this) {
                INSTANCE =
                    Room.databaseBuilder(context, CardsNMonstersDatabase::class.java, "cardsNmonsters_database")
                        .createFromAsset("cardsNmonsters.db")
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}

@Dao
interface CardsNMonstersDao {

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
