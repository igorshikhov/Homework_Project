package otus.project.mapapp

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import otus.project.mapapp.model.MapStyle
import otus.project.mapapp.model.MapViewModel

class ComposeInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun showStartScreenTest() {
        composeTestRule.onNodeWithText("Список").assertExists().assertHasClickAction()
        composeTestRule.onNodeWithText("Карта").assertExists().assertHasClickAction()
        composeTestRule.onNodeWithText("Настройки").assertExists().assertHasClickAction()
    }

    @Test
    fun goMapScreenTest() {
        composeTestRule.onNodeWithText("Карта").performClick()
        composeTestRule.waitUntil(5000, { true })
        composeTestRule.onNodeWithTag("ViewMap-Image").assertExists()
    }

    @Test
    fun goListScreenTest() {
        composeTestRule.onNodeWithText("Список").performClick()
        composeTestRule.waitUntil(5000, { true })
        composeTestRule.onNodeWithTag("ViewList-LazyColumn").assertExists()
        composeTestRule.onNodeWithTag("ItemLine-1").assertExists()
    }

    @Test
    fun goSettingsScreenTest() {
        composeTestRule.onNodeWithText("Настройки").performClick()
        composeTestRule.waitUntil(5000, { true })
        composeTestRule.onNodeWithText(" < ").assertExists().assertHasClickAction()
        composeTestRule.onNodeWithText("Сохранить").assertExists().assertHasClickAction()
        composeTestRule.onNodeWithText("Фильтр").assertExists()
        composeTestRule.onNodeWithText("Количество").assertExists()
    }

    @Test
    fun doStyleSelectTest() {
        composeTestRule.onNodeWithText("Настройки").performClick()
        composeTestRule.waitUntil(5000, { true })
        composeTestRule.onNodeWithText("Dark").performClick()
        composeTestRule.onNodeWithText("Сохранить").performClick()
        assertEquals(MapStyle.Dark, MapViewModel.Companion.currentStyle)
    }

    @Test
    fun doFilterInputTest() {
        composeTestRule.onNodeWithText("Настройки").performClick()
        composeTestRule.waitUntil(5000, { true })
        composeTestRule.onNodeWithTag("Settings-Filter").assertExists().performTextClearance()
        composeTestRule.onNodeWithTag("Settings-Filter").performTextInput("памятник")
        composeTestRule.onNodeWithText("Сохранить").performClick()
        assertEquals("памятник", MapViewModel.Companion.currentFilter)
    }

    @Test
    fun doLimitInputTest() {
        composeTestRule.onNodeWithText("Настройки").performClick()
        composeTestRule.waitUntil(5000, { true })
        composeTestRule.onNodeWithTag("Settings-Limit").assertExists().performTextClearance()
        composeTestRule.onNodeWithText("Сохранить").performClick()
        assertEquals(0, MapViewModel.Companion.currentLimit)
    }
}