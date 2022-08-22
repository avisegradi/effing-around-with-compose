package com.recipes.model

import kotlin.time.Duration

typealias TaskId = String

internal fun Collection<Task>.hasUniqueIds(): Boolean =
    this.groupBy { it.id }.size == this.size

class Task(
    //image
    //timer -> task mapping
    val id: TaskId,
    val title: String,
    val description: String,
    val ingredients: List<Ingredient> = emptyList(),
    val nextTask: TaskId? = null, // When null, go to next in array
    val timer: Duration? = null,
)

