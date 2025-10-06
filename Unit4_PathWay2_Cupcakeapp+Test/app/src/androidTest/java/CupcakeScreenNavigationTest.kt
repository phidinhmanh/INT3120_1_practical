import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.cupcake.CupcakeApp
import com.example.cupcake.CupcakeScreen
import com.example.cupcake.MainActivity
import com.example.cupcake.R
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@get:Rule
val composeTestRule = createAndroidComposeRule<MainActivity>()

class CupcakeScreenNavigationTest {
    private lateinit var navController: TestNavHostController


    private  fun getFormattedDate() : String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        calendar.add(Calendar.DATE, 1)
        return formatter.format(calendar.time)
    }

    private fun navigateToPickupScreen() {
        navigateToFlavorScreen()
        composeTestRule.onNodeWithStringId(R.string.next).performClick()
    }

    private fun navigateToFlavorScreen() {
        composeTestRule.onNodeWithStringId(R.string.one_cupcake)
            .performClick()
    }

    private fun navigateToSummaryScreen() {
        navigateToPickupScreen()
        composeTestRule.onNodeWithStringId(R.string.next).performClick()
    }

    private fun performNavigateUp() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).performClick()
    }


    fun setupCupcakeNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            CupcakeApp(navController = navController)
        }
    }

    @Test
    fun cupcakeNavHost_verifyStartDestination() {
        setupCupcakeNavHost()
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
        composeTestRule.onNodeWithStringId(R.string.order_cupcakes)

    }

    @Test
    fun cupcakeNavHost_verifyBackNavigationNotShownOnStartOrderScreen() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }


    @Test
    fun cupcakeNavHost_clickNextOnFlavorScreen_navigatesToPickupScreen() {
        navigateToFlavorScreen()
        composeTestRule.onNodeWithStringId(R.string.next)
            .performClick()
        navController.assertCurrentRouteName(CupcakeScreen.Flavor.name)
    }

    @Test
    fun cupcakeNavHost_clickBackOnFlavorScreen_navigatesToStartScreen() {
        navigateToFlavorScreen()
        performNavigateUp()
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    @Test
    fun cupcakeNavHost_clickCancelOnFlavorScreen_navigatesToStartScreen() {
        navigateToFlavorScreen()
        composeTestRule.onNodeWithStringId(R.string.cancel)
            .performClick()
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }


    @Test
    fun cupcakeNavHost_clickNextOnPickupScreen_navigatesToSummaryScreen() {
        navigateToPickupScreen()
        composeTestRule.onNodeWithStringId(R.string.next)
            .performClick()
        navController.assertCurrentRouteName(CupcakeScreen.Summary.name)
    }


    @Test
    fun cupcakeNavHost_clickCancelOnPickupScreen_navigatesToStartOrderScreen() {
        navigateToPickupScreen()
        composeTestRule.onNodeWithStringId(R.string.cancel)
            .performClick()
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    @Test
    fun cupcakeNavHost_clickCancelOnSummaryScreen_navigatesToStartOrderScreen() {
        navigateToSummaryScreen()
        composeTestRule.onNodeWithStringId(R.string.cancel)
            .performClick()
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }


}