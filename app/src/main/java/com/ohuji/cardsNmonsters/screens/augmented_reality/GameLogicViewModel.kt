package com.ohuji.cardsNmonsters.screens.augmented_reality

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ohuji.cardsNmonsters.repository.CollectableRepository
import kotlinx.coroutines.launch

class GameLogicViewModel(application: Application) : AndroidViewModel(application) {
    private val collectableRepo = CollectableRepository(application)

    /**
     * Based on monsterId returns the health value for monster
     */
    fun getStartingHealth(monsterId: Long): Int {
        val dragon = 1L
        val griffin = 2L
        val mammoth = 3L
        val mountainLion = 4L
        val skeleton = 5L
        val seaViking = 6L
        val youngDragon = 7L

        return when (monsterId) {
            dragon -> {
                2000
            }
            griffin -> {
                1200
            }
            mammoth -> {
                1500
            }
            mountainLion -> {
                800
            }
            skeleton -> {
                700
            }
            seaViking -> {
                750
            }
            youngDragon -> {
                1000
            }
            else -> 50
        }
    }

    fun doDamage(cardDamage: Int, status: Boolean, element: String, monsterElement: String?): Int {
        var damage = cardDamage
        var elementDamage = false

        if (elementCheck(element, monsterElement)) {
            elementDamage = true
        }

        if(status) {
            damage *= if (elementDamage){
                3
            } else {
                2
            }

            updateCollectableTypeDamage("Damage", damage)

            return damage
        }

        if (elementDamage) {
            updateCollectableTypeDamage("Element Damage", damage)

            return damage*2
        }

        updateCollectableTypeDamage("Damage", damage)

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
        viewModelScope.launch {
            val player = collectableRepo.findPlayer()
                player.currentLvlExp += exp
                collectableRepo.updatePlayerStats(player)

            if (player.currentLvlExp >= player.expRequirement) {
                    val excessExp = player.currentLvlExp - player.expRequirement
                    player.playerLevel += 1
                    player.expRequirement += 500
                    player.currentLvlExp = excessExp
                    collectableRepo.updatePlayerStats(player)
            }
        }
    }
}
