package com.ohuji.cardsNmonsters.screens.collectables

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ohuji.cardsNmonsters.database.CardsNMonstersDatabase
import com.ohuji.cardsNmonsters.database.Collectable
import com.ohuji.cardsNmonsters.database.Monster

class CollectablesViewModel(application: Application) : AndroidViewModel(application) {
    private val db = CardsNMonstersDatabase.getInstance(application)

    fun getAllCollectables(): LiveData<List<Collectable>> {
        val collectables = Transformations.map(db.collectableDao.getAllCollectables()) {
            it
        }
        return collectables
    }

    //Monsterien testausta varten
    fun getAllMonsters(): LiveData<List<Monster>> {
        val monsters = Transformations.map(db.monstersDao.getAllMonsters()) {
            it
        }
        return monsters
    }

}
