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

    fun getPlayerStats(): LiveData<Player> {
        return collectableRepo.playerStats
    }

    /**
     * Monster related
     */
    fun getAllMonsters(): LiveData<List<Monster>> {
        return monsterRepo.allMonsters
    }

    fun findMonsterById(monsterId: Long): LiveData<Monster> {

            val monster = Transformations.map(monsterRepo.findMonsterById(monsterId)) {
                it
            }
        return monster
    }
}

