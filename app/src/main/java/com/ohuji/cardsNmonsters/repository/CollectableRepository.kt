package com.ohuji.cardsNmonsters.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ohuji.cardsNmonsters.database.CardsNMonstersDatabase
import com.ohuji.cardsNmonsters.database.entities.Collectable
import com.ohuji.cardsNmonsters.database.entities.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CollectableRepository( application: Application) {
    private val db = CardsNMonstersDatabase.getInstance(application)
    private val collectableDao = db.collectableDao
    private val playerDao = db.playerDao

    //Collectable related
    val allCollectables: LiveData<List<Collectable>> = collectableDao.getAllCollectables()

    fun addCollectable(vararg collectable: Collectable) {
        db.databaseWriteExecutor.execute { collectableDao.addCollectable(*collectable) }
    }

    fun updateCollectable(collectable: Collectable) {
        db.databaseWriteExecutor.execute { collectableDao.updateCollectable(collectable) }
    }

    suspend fun findCollectableType(type: String): List<Collectable> {
        return withContext(Dispatchers.IO) {
            collectableDao.findCollectableType(type)
        }
    }

suspend fun findCollectableById(collectableId: Long): Collectable {
    return withContext(Dispatchers.IO) {
        collectableDao.findCollectableById(collectableId)
    }
}

    //Player related
    val playerStats: LiveData<Player> = playerDao.getPlayer()

    suspend fun findPlayer(): Player {
        return withContext(Dispatchers.IO) {
            playerDao.findPlayer()
        }
    }

    fun updatePlayerStats(player: Player) {
        db.databaseWriteExecutor.execute { playerDao.updatePlayer(player) }
    }
}

