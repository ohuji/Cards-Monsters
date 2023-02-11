package com.ohuji.cardsNmonsters.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Entity
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val cardName: String,
    val cardModel: String,
    var cardElement: String,
    var cardDamage: Int,
    var decksIn: Long,
    )
