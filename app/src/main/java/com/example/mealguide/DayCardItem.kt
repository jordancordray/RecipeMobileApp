package com.example.mealguide

data class DayCardItem(
    val name: String,
    val route: String,
    var recipe: Recipe? = null
)
