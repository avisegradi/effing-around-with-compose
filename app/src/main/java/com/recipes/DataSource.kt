package com.recipes

import androidx.lifecycle.LiveData
import com.recipes.model.Ingredient
import com.recipes.model.MeasUnit
import com.recipes.model.Recipe
import com.recipes.model.Task

class DataSource : LiveData<List<Recipe>>() {
    val recipes =
        listOf(
            Recipe(
                "Omelette",
                "Best omelette ever",
                arrayOf(
                    Ingredient("egg", 8f, MeasUnit.Pieces),
                    Ingredient("butter", 100f, MeasUnit.Gram),
                    Ingredient.pinchOf("salt"),
                ),
                tasks = arrayOf(
                    Task("Whisk eggs with salt, let it sit for 5 minutes."),
                    Task("Melt the butter in a saucepan, over LOW heat."),
                    Task("Pour eggs in butter, whisking constantly over LOW heat, for 4 minutes."),
                    Task("Give up and throw it in the rubbish bin.")
                )
            )
        )
}