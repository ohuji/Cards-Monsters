package com.ohuji.cardsNmonsters.screens.collectables

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.ohuji.cardsNmonsters.database.entities.Collectable
import com.ohuji.cardsNmonsters.repository.CollectableRepository
import com.ohuji.cardsNmonsters.database.entities.Monster
import com.ohuji.cardsNmonsters.repository.MonsterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CollectablesViewModel(application: Application) : AndroidViewModel(application) {
    private val collectableRepo = CollectableRepository(application)
    private val monsterRepo = MonsterRepository(application)

    fun getAllCollectables(): LiveData<List<Collectable>> {
        return collectableRepo.allCollectables
    }
/*
    fun getCollectableById(collectableId: Long): LiveData<Collectable> {
        return  collectableRepo.findCollectableById(collectableId)
    }

 */

    fun updateCollectable(collectable: Collectable) {
        viewModelScope.launch {
            collectableRepo.updateCollectable(collectable)
        }
    }

    //For Monster Testing

    fun getAllMonsters(): LiveData<List<Monster>> {
        return monsterRepo.allMonsters
    }

    fun findMonsterById(monsterId: Long): LiveData<Monster> {

            val monster = Transformations.map(monsterRepo.findMonsterById(monsterId)) {
                it
            }
        return monster
    }


/*
    fun findMonsterById(monsterId: Long): LiveData<Monster> {

          return monsterRepo.findMonsterById(monsterId)

    }

 */

}


/*
class CollectablesViewModel2(application: Application) : AndroidViewModel(application) {
    private val db = CardsNMonstersDatabase.getInstance(application)

    fun getAllCollectables(): LiveData<List<Collectable>> {
        val collectables = Transformations.map(db.collectableDao.getAllCollectables()) {
            it
        }
        return collectables
    }

    //Monsterien testausta varten
    fun getAllMonsters(): LiveData<List<Monster>> {
        val monsters = Transformations.map(db.monstersDao.getAllMonsters()) {
            it
        }
        return monsters
    }

}
 */
