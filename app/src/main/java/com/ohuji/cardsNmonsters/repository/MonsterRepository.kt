package com.ohuji.cardsNmonsters.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ohuji.cardsNmonsters.database.CardsNMonstersDatabase
import com.ohuji.cardsNmonsters.database.entities.Monster

class MonsterRepository( application: Application) {
    private val db = CardsNMonstersDatabase.getInstance(application)
    private val monsterDao = db.monstersDao

    val allMonsters: LiveData<List<Monster>> = monsterDao.getAllMonsters()

    fun addMonster(vararg monster: Monster) {
        db.databaseWriteExecutor.execute { monsterDao.addMonster(*monster) }
    }

    fun updateMonster(monster: Monster) {
        db.databaseWriteExecutor.execute { monsterDao.updateMonster(monster) }
    }

    fun findMonsterById(monsterId: Long): LiveData<Monster> {
        return monsterDao.findMonsterById(monsterId)
    }

}
