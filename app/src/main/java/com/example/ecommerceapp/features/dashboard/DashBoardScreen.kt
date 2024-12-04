package com.example.ecommerceapp.features.dashboard

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.routing.AccountNavGraph
import com.example.ecommerceapp.core.routing.CartNavGraph
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.features.dashboard.account.AccountScreen
import com.example.ecommerceapp.features.dashboard.cart.ui.CartScreen
import com.example.ecommerceapp.features.dashboard.explor.ExploreScreen
import com.example.ecommerceapp.features.dashboard.home.screens.HomeScreen
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel
import com.example.ecommerceapp.features.detailsproduct.domain.Review
import com.example.ecommerceapp.features.detailsproduct.screen.ProductDetailsScreen
import com.example.ecommerceapp.features.detailsproduct.screen.ReviewScreen
import com.example.ecommerceapp.features.detailsproduct.screen.WriteReviewScreen
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun DashBoardScreen(
    navController: NavController
) {

    // State for the currently selected screen
    var currentScreen by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }

    Scaffold(
        bottomBar = { BottomNavigationBar(currentScreen,navController) { currentScreen = it } },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                when (currentScreen) {
                    BottomNavItem.Home -> HomeScreen(navController = navController)
                    BottomNavItem.Explore -> ExploreScreen(navController = navController)
                    BottomNavItem.Cart -> CartNavGraph()
                    BottomNavItem.Offer -> OffersScreen()
                    BottomNavItem.Account -> AccountNavGraph()
                }
            }
        }
    )
}




@Composable
fun BottomNavigationBar(
    selectedScreen: BottomNavItem,
    navController: NavController,
    onItemSelected: (BottomNavItem) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Log.d("Benzosss", "BottomNavigationBar: ${currentDestination?.route}")
    val selectedItemColor = ColorsManager.PrimaryColor
    val unselectedItemColor = ColorsManager.NeutralGrey // Adjust as necessary
    val backgroundColor = ColorsManager.White // Change to your desired color

    // Material 3 NavigationBar
    NavigationBar(containerColor = backgroundColor) {
        // Iterate over all enum entries
        BottomNavItem.entries.forEach { item ->
            NavigationBarItem(
                icon = {
                    // Wrap the icon and text in a Column
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            modifier = Modifier.size(20.dp),
                            contentDescription = stringResource(id = item.title)
                        )
                        Spacer(modifier = Modifier.height(4.dp)) // Adjust the height for spacing
                        Text(
                            stringResource(id = item.title),
                            modifier = Modifier.padding(10.dp) // Adjust padding if needed
                        )
                    }
                },
                //  icon = { Icon( painter = painterResource(id = item.icon),  modifier = Modifier.size(20.dp),contentDescription = stringResource(id = item.title)) },

                selected = item == selectedScreen,
                onClick = { onItemSelected(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedItemColor,
                    unselectedIconColor = unselectedItemColor,
                    selectedTextColor = selectedItemColor,
                    unselectedTextColor = unselectedItemColor,
                    // Setting the selectedBackgroundColor to transparent
                    indicatorColor = Color.White,
                )
            )
        }
    }
}
enum class BottomNavItem(@StringRes val title: Int, @DrawableRes val icon: Int) {
    Home(R.string.home, R.drawable.ic_home),
    Explore(R.string.explore, R.drawable.ic_search),
    Offer(R.string.offer, R.drawable.ic_offers),
    Cart(R.string.cart, R.drawable.ic_cart),
    Account(R.string.account, R.drawable.ic_account),
}




@Composable
fun OffersScreen() {
    Text("Offers Screen", style = MaterialTheme.typography.titleLarge)
}

