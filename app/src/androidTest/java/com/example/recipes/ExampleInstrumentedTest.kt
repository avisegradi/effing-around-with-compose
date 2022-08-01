package com.recipes

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.recipes.ui.theme.RecipesTheme
import com.recipes.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun useAppContext() {
        composeTestRule.setContent {
            RecipesTheme {
                MainActivity()
            }
        }
        with(composeTestRule) {
            //onNodeWithText("Hello World").assertTextContains("Hello")
        }
    }
}