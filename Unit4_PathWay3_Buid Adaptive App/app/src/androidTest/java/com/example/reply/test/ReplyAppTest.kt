package com.example.reply.test

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.reply.MainActivity
import com.example.reply.ui.ReplyApp
import org.junit.Rule
import org.junit.Test
import com.example.reply.R
import androidx.activity.ComponentActivity

class ReplyAppTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun compatDevice_verifyUsingBottomNavigation() {
        composeTestRule.setContent {
            ReplyApp(
                windowSize = WindowWidthSizeClass.Compact,
            )
        }
        composeTestRule.onNodeWithTagForStringId(R.string.navigation_bottom)
            .assertExists()
    }

    @Test
    fun mediumDevice_verifyUsingNavigationRail() {
        composeTestRule.setContent {
            ReplyApp(
                windowSize = WindowWidthSizeClass.Medium,
            )
        }
        composeTestRule.onNodeWithTagForStringId(R.string.navigation_rail)
            .assertExists()
    }

    @Test
    fun expandedDevice_verifyUsingNavigationDrawer() {
        composeTestRule.setContent {
            ReplyApp(
                windowSize = WindowWidthSizeClass.Expanded,
            )
        }
        composeTestRule.onNodeWithTagForStringId(R.string.navigation_drawer)
            .assertExists()
    }


}