package com.ohuji.cardsNmonsters.network

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object GoTAPI {
    const val base_url = "https://api.gameofthronesquotes.xyz/v1/"

    object Model {
        data class Base(
            @SerializedName("sentence") val sentence: String,
            @SerializedName("character") val character: Character
        )

        data class Character(
            @SerializedName("name") val name: String,
            @SerializedName("slug") val slug: String,
            @SerializedName("house") val house: House
        )

        data class House(
            @SerializedName("name") val name: String,
            @SerializedName("slug") val slug: String
        )
    }

    interface Service {
        @GET("random")
        suspend fun quote() : Model.Base
    }


    private val retrofit = Retrofit.Builder()
        .baseUrl(base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: Service by lazy {
        retrofit.create(Service::class.java)
    }

}