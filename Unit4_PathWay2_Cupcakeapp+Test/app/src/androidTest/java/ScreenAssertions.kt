import androidx.navigation.NavController

fun NavController.assertCurrentRouteName(expectedRouteName: String) {
    assert(this.currentDestination?.route == expectedRouteName)
}