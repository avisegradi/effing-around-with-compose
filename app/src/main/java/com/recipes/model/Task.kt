package com.recipes.model

class Task(
    //image
    //timer -> task mapping
    val description: String,
    val ingredients: Array<Ingredient> = emptyArray(),
    val nextTask: Task? = null, // When null, go to next in array
    var active: Boolean = false,
    var done: Boolean = false,
    var uiIndex: Int? = null,
)

