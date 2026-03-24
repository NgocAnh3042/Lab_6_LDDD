/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.cupcake.R
import com.example.cupcake.data.OrderUiState
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen
import org.junit.Rule
import org.junit.Test

class CupcakeOrderScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    /**
     * Test for StartOrderScreen to verify content.
     */
    @Test
    fun startOrderScreen_verifyContent() {
        // When StartOrderScreen is loaded
        composeTestRule.setContent {
            StartOrderScreen(
                quantityOptions = listOf(
                    Pair(R.string.one_cupcake, 1),
                    Pair(R.string.six_cupcakes, 6),
                    Pair(R.string.twelve_cupcakes, 12)
                ),
                onNextButtonClicked = {}
            )
        }

        // Then all options are displayed
        composeTestRule.onNodeWithStringId(R.string.one_cupcake).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.six_cupcakes).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.twelve_cupcakes).assertIsDisplayed()
    }

    /**
     * Test for SelectOptionScreen to verify content.
     */
    @Test
    fun selectOptionScreen_verifyContent() {
        // Given list of options
        val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        // And subtotal
        val subtotal = "$100"

        // When SelectOptionScreen is loaded
        composeTestRule.setContent {
            SelectOptionScreen(subtotal = subtotal, options = flavors)
        }

        // Then all the options are displayed on the screen.
        flavors.forEach { flavor ->
            composeTestRule.onNodeWithText(flavor).assertIsDisplayed()
        }

        // And then the subtotal is displayed correctly.
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                subtotal
            )
        ).assertIsDisplayed()

        // And then the next button is disabled
        composeTestRule.onNodeWithStringId(R.string.next).assertIsNotEnabled()
    }

    /**
     * Test for SelectOptionScreen to verify that next button is enabled when an option is selected.
     */
    @Test
    fun selectOptionScreen_optionSelected_nextButtonEnabled() {
        // Given list of options
        val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        // And subtotal
        val subtotal = "$100"

        // When SelectOptionScreen is loaded
        composeTestRule.setContent {
            SelectOptionScreen(subtotal = subtotal, options = flavors)
        }

        // And one option is selected
        composeTestRule.onNodeWithText("Vanilla").performClick()

        // Then the next button is enabled
        composeTestRule.onNodeWithStringId(R.string.next).assertIsEnabled()
    }

    /**
     * Test for OrderSummaryScreen to verify content.
     */
    @Test
    fun summaryScreen_verifyContent() {
        // Given an order state
        val uiState = OrderUiState(
            quantity = 6,
            flavor = "Chocolate",
            date = "Wed Oct 18",
            price = "$12.00"
        )

        // When OrderSummaryScreen is loaded
        composeTestRule.setContent {
            OrderSummaryScreen(
                orderUiState = uiState,
                onCancelButtonClicked = {},
                onSendButtonClicked = { _, _ -> }
            )
        }

        // Then the content is displayed correctly
        composeTestRule.onNodeWithText("6").assertIsDisplayed()
        composeTestRule.onNodeWithText("Chocolate").assertIsDisplayed()
        composeTestRule.onNodeWithText("Wed Oct 18").assertIsDisplayed()
        
        val subtotal = composeTestRule.activity.getString(R.string.subtotal_price, uiState.price)
        composeTestRule.onNodeWithText(subtotal).assertIsDisplayed()
    }
}
