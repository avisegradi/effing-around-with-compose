package com.recipes.model

class Task(
    //image
    //timer -> task mapping
    val description: String,
    val ingredients: Array<Ingredient> = emptyArray(),
    val nextTask: Task? = null, // When null, go to next in array
)

