package com.ohuji.cardsNmonsters.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ohuji.cardsNmonsters.database.CardsNMonstersDatabase
import com.ohuji.cardsNmonsters.database.entities.Collectable

class CollectableRepository( application: Application) {
    private val db = CardsNMonstersDatabase.getInstance(application)
    private val collectableDao = db.collectableDao

    val allCollectables: LiveData<List<Collectable>> = collectableDao.getAllCollectables()

    fun addCollectable(vararg collectable: Collectable) {
        db.databaseWriteExecutor.execute { collectableDao.addCollectable(*collectable) }
    }

    fun updateCollectable(collectable: Collectable) {
        db.databaseWriteExecutor.execute { collectableDao.updateCollectable(collectable) }
    }

    fun findCollectableById(collectableId: Long): LiveData<Collectable> {
        return collectableDao.findCollectableById(collectableId )
    }
}
