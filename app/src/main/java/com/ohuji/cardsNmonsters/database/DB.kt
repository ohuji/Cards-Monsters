package com.ohuji.cardsNmonsters.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Database(entities = [Deck::class, Card::class, CardNDeckCrossRef::class, Monster::class, Collectable::class], version = 2, exportSchema = false)
abstract class CardsNMonstersDatabase: RoomDatabase() {
    abstract val cardsNMonstersDao: CardsNMonstersDao
    abstract val monstersDao: MonsterDao
    abstract val collectableDao: CollectableDao
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
