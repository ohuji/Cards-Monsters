package com.ohuji.cardsNmonsters.screens.augmented_reality

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ohuji.cardsNmonsters.repository.CardsNDeckRepository
import com.ohuji.cardsNmonsters.repository.CollectableRepository
import com.ohuji.cardsNmonsters.repository.MonsterRepository
import kotlinx.coroutines.launch

class GameLogicViewModel(application: Application) : AndroidViewModel(application) {
    private val collectableRepo = CollectableRepository(application)
    private val monsterRepo = MonsterRepository(application)
    private val cardsNDeckRepo = CardsNDeckRepository(application)

    fun doDamage(cardDamage: Int, status: Boolean, element: String): Int {
        var damage = cardDamage

        if(status) {
            damage *= 2
            Log.d("DBG", "Monsteri on dazed $damage")
            updateCollectableTypeDamage("Damage", damage)
            return damage
        }

        Log.d("DBG", "Paljos damagee tehtii $damage")
        return damage
    }

    private fun updateCollectableTypeDamage(type: String, damage: Int) {
        viewModelScope.launch {
            val collectable = collectableRepo.findCollectableType(type)
            for (i in collectable.indices) {
                collectable.let {
                    if (!it[i].unlocked) {
                        it[i].currentProgress += damage
                        collectableRepo.updateCollectable(it[i])
                        if (it[i].currentProgress >= it[i].requirements) {
                            it[i].unlocked = true
                            collectableRepo.updateCollectable(it[i])
                        }
                    }
                }
            }
        }
    }

    fun updateCollectableTypeKill(type: String) {
        viewModelScope.launch {
            val collectable = collectableRepo.findCollectableType(type)
            for (i in collectable.indices) {
                collectable.let {
                    if (!it[i].unlocked) {
                        it[i].currentProgress += 1
                        collectableRepo.updateCollectable(it[i])
                        if (it[i].currentProgress >= it[i].requirements) {
                            it[i].unlocked = true
                            collectableRepo.updateCollectable(it[i])
                        }
                    }
                }
            }
        }
    }
}

/*
  private fun updateCollectableProgress(collectableId: Long) {
        viewModelScope.launch {
            val collectable = collectableRepo.findCollectableById(collectableId)
            collectable.let {
                it.currentProgress += 1
                collectableRepo.updateCollectable(it)
                if(it.currentProgress >= it.requirements) {
                    it.unlocked = true
                    collectableRepo.updateCollectable(it)
                }
            }
        }
    }

    fun updateCollectable() {
        viewModelScope.launch {
            updateCollectableProgress(1L)
        }
    }
  fun doDamage2(cardId: Long): Int {

             val card = cardsNDeckRepo.findCardById(cardId).value
             val damage = card?.cardDamage ?: 100
             Log.d("DBG", "Paljos damagee tehtii $damage, kortin oikea damage ${card?.cardDamage}, id $cardId")
             return damage


    }

    fun doDamage(cardId: Long): LiveData<Int> {
       // val damageLiveData = MutableLiveData<Int>()
        var damageLiveData = MutableLiveData<Int>()
        cardsNDeckRepo.findCardById(cardId).observeForever { card ->
            if (card != null) {
                val damage = card.cardDamage ?: 100
                Log.d("DBG", "Paljos damagee tehtii $damage, kortin oikea damage ${card.cardDamage}, id $cardId")
                damageLiveData.value = damage
            }
        }

        return damageLiveData
    }
 */