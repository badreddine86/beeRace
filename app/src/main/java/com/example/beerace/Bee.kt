package com.example.beerace

import com.google.gson.annotations.SerializedName

data class Bee(
    val name: String,
    val color: String
)

data class BeeResponse(
    val beeList: List<Bee>
)

data class RaceDurationReponse(
    @SerializedName("timeInSeconds")
    val timeInSeconds: Int
)