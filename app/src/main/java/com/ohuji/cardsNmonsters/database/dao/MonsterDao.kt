package com.ohuji.cardsNmonsters.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ohuji.cardsNmonsters.database.entities.Monster

@Dao
interface MonsterDao{

    @Insert
    fun addMonster(vararg monster: Monster)

    @Query("select * from Monster")
    fun getAllMonsters(): LiveData<List<Monster>>

    @Update
    fun updateMonster(monster: Monster)

    @Query("SELECT * FROM Monster WHERE monsterId = :monsterId")
    fun findMonsterById(monsterId: Long): LiveData<Monster>

}