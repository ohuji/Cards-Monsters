package com.ohuji.cardsNmonsters.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ohuji.cardsNmonsters.repository.GoTRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoTViewModel : ViewModel() {
    private val repository = GoTRepository()

    var quote = ""
    var name = ""

    fun getQuote() {
        viewModelScope.launch(Dispatchers.IO) {
            val serverResp = repository.quote()

            quote = serverResp.sentence
            name = serverResp.character.name
        }
    }
}