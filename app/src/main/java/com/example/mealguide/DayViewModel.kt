package com.example.mealguide

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.lifecycle.ViewModel

class DayViewModel : ViewModel() {
    var days = mutableStateListOf(
        DayCardItem("Monday", "monday"),
        DayCardItem("Tuesday", "tuesday"),
        DayCardItem("Wednesday", "wednesday"),
        DayCardItem("Thursday", "thursday"),
        DayCardItem("Friday", "friday"),
        DayCardItem("Saturday", "saturday"),
        DayCardItem("Sunday", "sunday")
    )

    fun getDayRecipes(): SnapshotStateList<Recipe> {
        val recipes: SnapshotStateList<Recipe> = mutableStateListOf<Recipe>()
        days.forEach { day->
            if (day.recipe != null)
                recipes.add(day.recipe!!)
        }
        return recipes
    }

    fun getUniqueIngredients(): SnapshotStateSet<String>{
        val ingredients: SnapshotStateSet<String> = mutableStateSetOf<String>()
        getDayRecipes().forEach { recipe->
            recipe.ingredients.forEach { ingredient->
                ingredients.add(ingredient)
            }
        }
        return ingredients
    }

    fun updateDayRecipe(day: String, recipe: Recipe) {
        val idx = days.indexOfFirst { it.name == day }
        if (idx != -1) {

            days[idx] = days[idx].copy(recipe = recipe)
        }
    }
}