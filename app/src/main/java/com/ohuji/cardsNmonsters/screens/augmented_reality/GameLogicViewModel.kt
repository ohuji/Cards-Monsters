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
      val dark = "Dark"

      return when {
          cardElement == fire && monsterElement == wind -> {
              true
          }
          cardElement == light && monsterElement == dark -> {
              true
          }
          cardElement == wind && monsterElement == earth -> {
              true
          }
          cardElement == earth && monsterElement == electricity -> {
              true
          }
          cardElement == electricity && monsterElement == water -> {
              true
          }
          cardElement == water && monsterElement == fire -> {
              true
          }
          cardElement == dark && monsterElement == light -> {
              true
          }
          else -> false
      }
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
