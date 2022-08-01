package com.recipes.model

class Recipe(
    val title: String,
    val description: String,
    val ingredients: Array<Ingredient>,
    val persons: Int = 4,
    val tasks: Array<Task>,
    val parts: Array<Recipe> = emptyArray(),
) {
    fun allIngredients() { throw NotImplementedError() } // placeholder, collect all recursively
}