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
     * Returns a LiveData object containing a list of all collectables.
     *
     * @return A LiveData object containing a list of all collectables.
     */
    fun getAllCollectables(): LiveData<List<Collectable>> {
        return collectableRepo.allCollectables
    }

    /**
     * Updates the specified collectable asynchronously.
     *
     * @param collectable The collectable to update.
     */
    fun updateCollectable(collectable: Collectable) {
        viewModelScope.launch {
            collectableRepo.updateCollectable(collectable)
        }
    }

    /**
     * @return [LiveData] object containing [Player] stats.
     */
    fun getPlayerStats(): LiveData<Player> {
        return collectableRepo.playerStats
    }

    /**
     * @return [LiveData] object containing a list of all [Monster]s.
     */
    fun getAllMonsters(): LiveData<List<Monster>> {
        return monsterRepo.allMonsters
    }

    /**
     * Returns a [LiveData] object containing the [Monster] with the given [monsterId].
     * @param monsterId ID of the [Monster] to find.
     * @return [LiveData] object containing the [Monster] with the given [monsterId].
     */
    fun findMonsterById(monsterId: Long): LiveData<Monster> {

            val monster = Transformations.map(monsterRepo.findMonsterById(monsterId)) {
                it
            }
        return monster
    }
}

