import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.cupcake.MainActivity
import com.example.cupcake.R
import com.example.cupcake.ui.SelectOptionScreen
import org.junit.Rule
import org.junit.Test


class CupcakeOrderScreenTest {
    @get:Rule
    private val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun selectOptionScreen_verifyContent() {
        // Given list of options
        val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        // And subtotal
        val subtotal = "$100"
        composeTestRule.setContent {
            SelectOptionScreen(
                subtotal = subtotal,
                options = flavors
            )
        }

        flavors.forEach { flavor ->
            composeTestRule.onNodeWithText(flavor)
                .assertIsDisplayed()
        }
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                subtotal
            )
        ).assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.next).assertIsNotEnabled()
    }

}