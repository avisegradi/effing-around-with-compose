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
                tasks = listOf(
                    Task("whisk", "Whisk eggs with salt, let it sit for 5 minutes."),
                    Task("melt", "Melt the butter in a saucepan, over LOW heat."),
                    Task("addEggs",
                         "Pour eggs in butter, whisking constantly over LOW heat, for 4 minutes."),
                    Task("done", "Give up and throw it in the rubbish bin.")
                )
            ),
            Recipe(
                "Nokedli",
                "Whatever",
                arrayOf(
                    Ingredient("egg", 1f, MeasUnit.Pieces),
                    Ingredient("flour", 600f, MeasUnit.Gram),
                    Ingredient("salt", 1f, MeasUnit.Teaspoon),
                    Ingredient("water", 550f, MeasUnit.Milliliter),
                    Ingredient("oil or butter", 0f, MeasUnit.Custom),
                    Ingredient(
                        "herbs",
                        0f,
                        MeasUnit.Custom,
                        description = "Arbitrary green herbs and/or spices like whatever",
                        optional = true,
                    ),
                ),
                tasks = listOf(
                    Task("boil", "Start boiling a ton of water with a bit of salt"),
                    Task("combine", "Combine all ingredients in a tub"),
                    Task("cook",
                         "When the water boils, cook teh stuff; fish out with a spider and add a bit of oil so it won't stick"),
                ),
                persons = 6,
            )
        )
}