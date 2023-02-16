package com.ohuji.cardsNmonsters.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CollectableDao{

        @Insert
        fun addCollectable(vararg collectable: Collectable)

        @Query("select * from Collectable")
        fun getAllCollectables(): LiveData<List<Collectable>>

        @Update
        fun updateCollectable(collectable: Collectable)

        @Query("SELECT * FROM Collectable WHERE collectableId = :collectableId")
        fun findCollectableById(collectableId: Long): LiveData<Collectable>

    }

