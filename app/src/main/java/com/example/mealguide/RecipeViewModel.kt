package com.example.mealguide

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RecipeViewModel : ViewModel() {
    var currentRecipe = mutableStateOf<Recipe?>(null)
    var recipes = mutableStateListOf<Recipe>()
        private set
    var ingredients = listOf("Ingredient")
    lateinit var newRecipe : Recipe
        private set

    fun LoadRecipes(){
        for (i in 1..10){
            newRecipe = Recipe("recipe$i", ingredients)
            AddRecipe(newRecipe)
        }
    }

    fun AddRecipe(recipe: Recipe){
        recipes.add(recipe)
    }
    init {
        LoadRecipes()
    }
}