package com.recipes.model

import java.util.*

typealias Amount = Float

enum class MeasUnit {
    Pieces,
    Teaspoon,
    Tablespoon,
    Pinch,
    Gram,
    Kilogram,
    Dekagram,
    Liter,
    Milliliter,
    FluidOunces,
    Ounces,
    Cups,
    Custom,
    //...
}

class Ingredient(
    val name: String,
    // List of amount+unit pairs would allow 1 cups and 10 teaspoons and stuff
    val amount: Amount,
    val unit: MeasUnit,
    val description: String? = null,
    val customUnit: String? = null,
) {

    companion object {
        fun pinchOf(name: String) = Ingredient(name, 1f, MeasUnit.Pinch)
    }
}