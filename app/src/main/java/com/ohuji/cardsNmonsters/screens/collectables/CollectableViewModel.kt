package com.ohuji.cardsNmonsters.screens.collectables

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.ohuji.cardsNmonsters.database.entities.Collectable
import com.ohuji.cardsNmonsters.database.entities.Monster
import com.ohuji.cardsNmonsters.database.entities.Player
import com.ohuji.cardsNmonsters.repository.CollectableRepository
import com.ohuji.cardsNmonsters.repository.MonsterRepository
import kotlinx.coroutines.launch

class CollectablesViewModel(application: Application) : AndroidViewModel(application) {
    private val collectableRepo = CollectableRepository(application)
    private val monsterRepo = MonsterRepository(application)

    /**
     * Collectable related
     */

    /**
     * Returns all collectables in LiveData list
     */
    fun getAllCollectables(): LiveData<List<Collectable>> {
        return collectableRepo.allCollectables
    }

    fun updateCollectable(collectable: Collectable) {
        viewModelScope.launch {
            collectableRepo.updateCollectable(collectable)
        }
    }

    /**
     * Player related
     */

    /**
     * Returns  player as LiveData
     */

    fun getPlayerStats(): LiveData<Player> {
        return collectableRepo.playerStats
    }

    /**
     * Monster related
     */

    /**
     * Returns all monsters in a LiveData list
     */
    fun getAllMonsters(): LiveData<List<Monster>> {
        return monsterRepo.allMonsters
    }

    /**
     * Returns a monster based on its id
     */
    fun findMonsterById(monsterId: Long): LiveData<Monster> {

            val monster = Transformations.map(monsterRepo.findMonsterById(monsterId)) {
                it
            }
        return monster
    }
}

