package com.ohuji.cardsNmonsters.screens.augmented_reality

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ohuji.cardsNmonsters.repository.CollectableRepository
import kotlinx.coroutines.launch

/**
 * ViewModel class that handles game logic
 */
class GameLogicViewModel(application: Application) : AndroidViewModel(application) {
    private val collectableRepo = CollectableRepository(application)

    /**
     * Based on monsterId returns the health value for monster
     *
     * @param monsterId is the monsterId of the monster the player is fighting
     * @return the health value for monster
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

    /**
     * Takes the cards damage value, monsters current status, elements. Does the elementCheck and based on the result applies the damage.
     * Also executes collectables where type is "damage" based on the damage value
     *
     * @param cardDamage cards base damage value
     * @param status monsters current status
     * @param element card element type
     * @param monsterElement monsters element type
     * @return the damage value based monster card damage, monster status and elements
     */

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
            updateCollectableTypeDamage("Damage", damage)

            return damage*2
        }

        updateCollectableTypeDamage("Damage", damage)

        return damage
    }

     /**
     * Checks if the card used is of element type the monster has a weakness to.
     * Returns true if card used is strong against the monsters element type.
     * @param cardElement cards element type
     * @param monsterElement monsters element type
     * @return true if card used is strong against the monsters element type false if not
     */
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

    /**
     * Is called in doDamage function. Updates collectables of type damage.
     * Checks if collectable progress meets the requirement value and the unlocks it.
     * Won't update collectables where "unlocked" is true
     *
     * @param type collectable type
     * @param damage value that is updated to collectable
     */
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

    /**
     * Is called from AR screen when monsters health reaches 0. Updates collectables of type kill.
     * Check if progress meets the requirement value and then unlocks it.
     * Won't update if unlocked is true
     *
     * @param type collectables type
     */
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

    /**
     * Is called when a monster is killed. Updates players current exp and level if level requirement is met.
     * Also increases expRequirement after each level up.
     *
     * @param exp value that is updated to player currentLvlEXP
     */
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
