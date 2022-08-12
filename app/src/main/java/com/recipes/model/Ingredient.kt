package com.recipes.model

typealias Amount = Float

fun Amount.asAmountString(): String {
    return this.toInt().toString()
}

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
    val optional: Boolean = false,
) {

    companion object {
        fun pinchOf(name: String) = Ingredient(name, 1f, MeasUnit.Pinch)
    }
}
