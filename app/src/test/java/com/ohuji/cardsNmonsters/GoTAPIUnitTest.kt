package com.ohuji.cardsNmonsters

import com.ohuji.cardsNmonsters.network.GoTAPI
import org.junit.Test
import retrofit2.Retrofit

class GoTAPIUnitTest {

    @Test
    fun testRetrofitInstance() {
        val url = "https://api.gameofthronesquotes.xyz/v1/"

        val instance: Retrofit = GoTAPI.retrofit

        assert(instance.baseUrl().toUrl().toString() == url)
    }
}