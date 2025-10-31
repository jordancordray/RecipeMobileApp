package com.example.mealguide

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Recipe(
    val name: String,
    val ingredients: List<String>,
    var selected: MutableState<Boolean> = mutableStateOf(false)
)
