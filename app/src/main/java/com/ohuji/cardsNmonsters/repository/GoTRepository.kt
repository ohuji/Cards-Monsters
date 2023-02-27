package com.ohuji.cardsNmonsters.repository

import com.ohuji.cardsNmonsters.network.GoTAPI

class GoTRepository {
    private val call = GoTAPI.service

    suspend fun quote() = call.quote()
}