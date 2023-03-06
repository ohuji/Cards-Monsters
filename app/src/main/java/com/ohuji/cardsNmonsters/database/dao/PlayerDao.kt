package com.ohuji.cardsNmonsters.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ohuji.cardsNmonsters.database.entities.Player

@Dao
interface PlayerDao {

    @Insert
    fun addPlayer(vararg player: Player)

    @Query("select * from Player")
    fun getPlayer(): LiveData<Player>

    @Query("SELECT * FROM Player WHERE playerId = 1")
    fun findPlayer(): Player

    @Update
    fun updatePlayer(player: Player)
}