package com.recipes.model

class Recipe(
    val title: String,
    val description: String,
    val ingredients: Array<Ingredient>,
    val persons: Int = 4,
    val tasks: List<Task>,
    val parts: List<Recipe> = emptyList(),
) {
    init {
        require(tasks.hasUniqueIds())
    }

    fun allIngredients(): List<Ingredient> {
        throw NotImplementedError()
    } // placeholder, collect all recursively
}