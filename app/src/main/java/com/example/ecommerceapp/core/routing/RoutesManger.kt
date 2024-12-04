package com.example.ecommerceapp.core.routing

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.ecommerceapp.core.routing.Routes.DeepLinkDetailsProducts
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.DashBoardScreen
import com.example.ecommerceapp.features.auth.forgotpassword.ForgotPasswordScreen
import com.example.ecommerceapp.features.auth.login.LoginScreen
import com.example.ecommerceapp.features.auth.register.RegisterScreen
import com.example.ecommerceapp.features.dashboard.account.AccountScreen
import com.example.ecommerceapp.features.dashboard.account.address.domain.Address
import com.example.ecommerceapp.features.dashboard.account.address.ui.AddOrEditAddressScreen
import com.example.ecommerceapp.features.dashboard.account.address.ui.AddressEvent
import com.example.ecommerceapp.features.dashboard.account.address.ui.AddressScreen
import com.example.ecommerceapp.features.dashboard.account.profile.ProfileScreen
import com.example.ecommerceapp.features.dashboard.account.profile.ProfileUpdateEvent
import com.example.ecommerceapp.features.dashboard.account.profile.UpdateUserProfile
import com.example.ecommerceapp.features.dashboard.account.order.domain.Order
import com.example.ecommerceapp.features.dashboard.account.order.ui.OrderDetailsScreen
import com.example.ecommerceapp.features.dashboard.account.order.ui.OrdersScreen
import com.example.ecommerceapp.features.dashboard.cart.ui.CartScreen
import com.example.ecommerceapp.features.dashboard.cart.ui.ShipToScreen
import com.example.ecommerceapp.features.dashboard.home.screens.HomeViewModel
import com.example.ecommerceapp.features.dashboard.search.SearchScreen
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
fun AppNavigation(){
    val navController = rememberNavController()

    RouteManager(navController).SetupNavHost()
}

class RouteManager(private val navController: NavHostController) {

    @Composable
    fun SetupNavHost() {
        NavHost(navController = navController, startDestination = Routes.DashBoard) {

            composable(Routes.DashBoard) {
                DashBoardScreen(navController)
            }

            composable(Routes.SearchScreen) {
                SearchScreen(navController)
            }

            authNavGraph(navController)

            composable(DeepLinkDetailsProducts,
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern =
                            "https://ecomerceincompose.page.link/jTpt/product_id={productId}"
                        action = Intent.ACTION_VIEW
                    },

                ),
                arguments = listOf(
                    navArgument("productId") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )

                ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")

                if (!productId.isNullOrEmpty()) {
                    ProductDetailsScreen(productId, navController) {
                        navController.navigate(Routes.DashBoard) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true // Clear the stack to avoid duplicate destinations
                            }
                        }
                    }
                } else {
                    Log.e("DeepLinkError", "productId is null or empty")
                }
            }
            composable(
                route = "${Routes.DetailsProducts}/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                Toast.makeText(navController.context, "Navigated to $productId", Toast.LENGTH_SHORT).show()
                if (!productId.isNullOrEmpty()) {
                    ProductDetailsScreen(productId, navController) {
                        navController.popBackStack()
                    }
                }
            }
            composable(Routes.ReviewsScreen) {
                val reviews =
                    navController.previousBackStackEntry?.savedStateHandle?.get<List<Review>>("reviews")
                if (reviews != null) {
                    ReviewScreen(reviews = reviews, navController = navController) {
                        navController.popBackStack()
                    }

                }
            }
            composable(Routes.WriteReview) {
                val productId =
                    navController.previousBackStackEntry?.savedStateHandle?.get<String>("productId")
                if (productId != null) {
                    WriteReviewScreen(
                        productId = productId,
                        navController
                    ) { navController.popBackStack() }
                }
            }




        }

    }
}

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Routes.AuhthScreen,
        startDestination = Routes.Login
    ) {
        composable(Routes.Register) {
            RegisterScreen(navController)
        }
        composable(Routes.ForgotPassword) {
            ForgotPasswordScreen(navController) {
                navController.popBackStack()
            }
        }
        composable(Routes.Login) {
            LoginScreen(navController)
        }
    }
}



@Composable
fun CartNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Routes.CartScreen
    ) {
        authNavGraph(navController)

        composable(Routes.CartScreen) {
            CartScreen(cartNavController = navController)
        }
        composable(Routes.ShipToScreen) {
            val order = navController.previousBackStackEntry?.savedStateHandle?.get<Order>("order")
            if (order != null) {
                ShipToScreen(navController, order) {
                    navController.popBackStack()
                }
            }
        }
        composable(Routes.AddOrEditAddressScreen)
        {
            val address =
                navController.previousBackStackEntry?.savedStateHandle?.get<Address>("address")
            AddOrEditAddressScreen(
                navController = navController,
                event = if (address != null) AddressEvent.Edit else AddressEvent.ADD,
                onBackClick = { navController.popBackStack() },
                editAddress = address
            )
        }

    }
}

@Composable
fun AccountNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Routes.AccountScreen
    ) {
        composable(Routes.AccountScreen) {
            AccountScreen(navController)
        }
        composable(Routes.ProfileScreen) {
            ProfileScreen(navController) {
                // navController.navigate(Routes.AccountScreen)
                navController.popBackStack()
            }
        }
        composable(Routes.OrderScreen) {
            OrdersScreen(navController) {
                navController.popBackStack()
            }

        }
        composable(Routes.OrderDetailsScreen) {
            val order = navController.previousBackStackEntry?.savedStateHandle?.get<Order>("order")
            if (order != null) {
                OrderDetailsScreen(order) {
                    navController.popBackStack()
                }
            }
        }

        composable(
            route = "${Routes.UpdateProfileScreen}/{title}/{data}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("data") { type = NavType.StringType }
            )
        )
        { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val data = backStackEntry.arguments?.getString("data") ?: ""

            val event = when (title) {
                "Email" -> ProfileUpdateEvent.OnUpdateEmail(email = data)
                "Phone" -> ProfileUpdateEvent.OnUpdatePhone(name = data)
                "Password" -> ProfileUpdateEvent.onUpdatePassword(phone = data)
                else -> throw IllegalArgumentException("Unknown ProfileUpdateEvent")
            }

            UpdateUserProfile(
                title = title,
                event = event,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.AddressScreen) {

            AddressScreen(navController) {
                navController.popBackStack()
            }
        }
        composable(
            Routes.AddOrEditAddressScreen + "?addressJson={addressJson}",
            arguments = listOf(navArgument("addressJson") {
                type = NavType.StringType; nullable = true
            })
        ) {
            val encodedAddressJson = it.arguments?.getString("addressJson")
            val addressJson = encodedAddressJson?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }
            val address = addressJson?.let { Gson().fromJson(it, Address::class.java) }

            AddOrEditAddressScreen(
                navController = navController,
                event = if (address != null) AddressEvent.Edit else AddressEvent.ADD,
                onBackClick = { navController.popBackStack() },
                editAddress = address
            )
        }

    }
}


/*
 composable("DeepLink",
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern =
                            "https://ecomerceincompose.page.link/jTpt/product_id={productId}"
                        action = Intent.ACTION_VIEW
                    },

                ),
                arguments = listOf(
                    navArgument("productId") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )

                ) { backStackEntry ->
                val homeViewModel :HomeViewModel= hiltViewModel()
                lateinit var product: ProductUIModel
                var productId = backStackEntry.arguments?.getString("productId")
                val productUIModel = navController.previousBackStackEntry?.savedStateHandle?.get<ProductUIModel>("product")
                val prdouctIdState by homeViewModel.prdouctIdState.collectAsState()
                Log.d("TAdfdfdfdfdfdfdfdfdfdfdfG", "productId:$productId")
                LaunchedEffect(productId) {
                    Log.d("DeepLink", "Launching with productId: $productId")
                    if (!productId.isNullOrEmpty()) {
                        homeViewModel.getProductById(productId!!)
                    }
                }
                when(prdouctIdState){
                    is Resource.Error -> {
                        productId= null
                        Toast.makeText(LocalContext.current, "Error", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Idle -> {}
                    is Resource.Loading -> {
                        Toast.makeText(LocalContext.current, "Loading benz", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        Log.d("DeepghgfhgfhgfhLink", "Success with productId: ${prdouctIdState.data?.name}")
                        Toast.makeText(LocalContext.current, "Success", Toast.LENGTH_SHORT).show()
                        productId =prdouctIdState.data?.id
                        product = prdouctIdState.data!!

                    }
                }

               when{
                   productId != null -> {
                       product = prdouctIdState.data!!
                       ProductDetailsScreen(product, navController) {
                           navController.popBackStack()
                       }
                   }
                   productUIModel != null -> {
                       ProductDetailsScreen(productUIModel, navController) {
                           navController.popBackStack()
                       }
                   }
                   else -> {
                       navController.navigate(Routes.DashBoard)
                   }
               }

            }
 */