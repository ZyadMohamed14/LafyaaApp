package com.example.ecommerceapp

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.ui.dashboard.home.HomeScreen
import com.example.ecommerceapp.ui.products.ui.theme.ColorManagers

@Composable
fun DashBoardScreen() {
    // State for the currently selected screen
    var currentScreen by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    var context = LocalContext.current
    Scaffold(
        bottomBar = { BottomNavigationBar(currentScreen,) { currentScreen = it } },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                when (currentScreen) {
                    BottomNavItem.Home -> HomeScreen()
                    BottomNavItem.Explore -> ExploreScreen()
                    BottomNavItem.Cart -> CartScreen()
                    BottomNavItem.Offer -> OffersScreen()
                    BottomNavItem.Account -> AccountScreen()
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar(
    selectedScreen: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    val selectedItemColor = ColorManagers.PrimaryColor
    val unselectedItemColor =ColorManagers.NeutralGrey // Adjust as necessary
    val backgroundColor = ColorManagers.White // Change to your desired color

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
    Offer(R.string.offer,R.drawable.ic_offers),
    Cart(R.string.offer,R.drawable.ic_cart),
    Account(R.string.offer,R.drawable.ic_account),
}
@Composable
fun ExploreScreen() {
    Text("Explore Screen", style = MaterialTheme.typography.titleLarge)
}

@Composable
fun CartScreen() {
    Text("Cart Screen", style = MaterialTheme.typography.titleLarge)
}

@Composable
fun OffersScreen() {
    Text("Offers Screen", style = MaterialTheme.typography.titleLarge)
}

@Composable
fun AccountScreen() {
    Text("Account Screen", style = MaterialTheme.typography.titleLarge)
}