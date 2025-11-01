package com.example.mealguide

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RecipeViewModel : ViewModel() {
    var currentRecipe = mutableStateOf<Recipe?>(null)
    var recipes = mutableStateListOf<Recipe>()
        private set
    lateinit var loadedRecipe : Recipe
        private set

    fun loadRecipes(){
        for (i in 1..10){
            loadedRecipe = Recipe("recipe$i", listOf("Ingredient$i"))
            addRecipe(loadedRecipe)
        }
    }
    fun addRecipe(recipe: Recipe){
        recipes.add(recipe)
    }
    init {
        loadRecipes()
    }
}