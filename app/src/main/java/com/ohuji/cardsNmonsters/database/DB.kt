package com.ohuji.cardsNmonsters.database

import android.content.Context
import androidx.room.*
import com.ohuji.cardsNmonsters.database.dao.CardsNDeckDao
import com.ohuji.cardsNmonsters.database.dao.CollectableDao
import com.ohuji.cardsNmonsters.database.dao.MonsterDao
import com.ohuji.cardsNmonsters.database.dao.PlayerDao
import com.ohuji.cardsNmonsters.database.entities.Card
import com.ohuji.cardsNmonsters.database.entities.Collectable
import com.ohuji.cardsNmonsters.database.entities.Monster
import com.ohuji.cardsNmonsters.database.entities.Player
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Database(entities = [Deck::class, Card::class, CardNDeckCrossRef::class, Monster::class, Collectable::class, Player::class], version = 4, exportSchema = false)
abstract class CardsNMonstersDatabase: RoomDatabase() {
    abstract val cardsNDecksDao: CardsNDeckDao
    abstract val monstersDao: MonsterDao
    abstract val collectableDao: CollectableDao
    abstract val playerDao: PlayerDao
    val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(4)

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
