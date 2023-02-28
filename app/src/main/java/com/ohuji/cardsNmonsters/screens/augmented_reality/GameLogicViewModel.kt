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

    fun doDamage(cardDamage: Int, status: Boolean, element: String, monsterElement: String?): Int {
        var damage = cardDamage
        var elementDamage = false

        if (elementCheck(element, monsterElement)) {
            elementDamage = true
        }

        if(status) {
            damage *= if (elementDamage){
                Log.d("DBG", "Triple damage $damage")
                3
            } else {
                2
            }
            Log.d("DBG", "Monsteri on dazed $damage")
            updateCollectableTypeDamage("Damage", damage)
            return damage
        }
        if (elementDamage) {
            updateCollectableTypeDamage("Element Damage", damage)
            Log.d("DBG", "Element damagea tehtii")
            return damage*2
        }
        updateCollectableTypeDamage("Damage", damage)
        Log.d("DBG", "Paljos damagee tehtii $damage")
        return damage
    }

  private fun elementCheck(cardElement: String, monsterElement: String?): Boolean {
      val fire = "Fire"
      val light = "Light"
      val wind = "Wind"
      val earth = "Earth"
      val electricity = "Electricity"
      val water = "Water"
      val phys = "Phys"
      val dark = "Dark"


return true
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

    fun updatePlayerStats(exp: Int) {
        Log.d("DBG", "updatePlayer $exp")
        viewModelScope.launch {
            val player = collectableRepo.findPlayer()
                player.currentLvlExp += exp
                collectableRepo.updatePlayerStats(player)
            Log.d("DBG", "Playerii päivitetään ${player.currentLvlExp}")
            if (player.currentLvlExp >= player.expRequirement) {
                    val excessExp = player.currentLvlExp - player.expRequirement
                    player.playerLevel += 1
                    player.expRequirement += 500
                    player.currentLvlExp = excessExp
                Log.d("DBG", "Nyt tuli leveli, lvl: ${player.playerLevel}, ja exp ${player.currentLvlExp} ja exp tarttee nyt ${player.expRequirement}")
                    collectableRepo.updatePlayerStats(player)

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