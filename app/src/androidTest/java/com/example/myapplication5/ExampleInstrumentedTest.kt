package com.example.myapplication5

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication5.ui.theme.MyApplication5Theme
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
            MyApplication5Theme {
                MainActivity()
            }
        }
        with(composeTestRule) {
            //onNodeWithText("Hello World").assertTextContains("Hello")
        }
    }
}