package com.recipes.model

import androidx.core.graphics.PathUtils.flatten

class Recipe(
    val title: String,
    val description: String,
    val tasks: List<Task>,
    val ingredients: List<Ingredient> = listOf(),
    val persons: Int = 4,
    val parts: List<Recipe> = emptyList(),
) {
    init {
        require(tasks.hasUniqueIds())
    }

    fun allIngredients(): List<Ingredient> {
        return tasks.flatMap { it.ingredients }.sortedBy { it.name }
    }
}