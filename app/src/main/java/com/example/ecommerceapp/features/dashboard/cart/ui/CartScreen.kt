package com.example.ecommerceapp.features.dashboard.cart.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*


import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.local.SharedPreferencesHelper
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.Poppins
import com.example.ecommerceapp.core.theme.productTitleStyle
import com.example.ecommerceapp.core.ui.AppAlertDialog
import com.example.ecommerceapp.core.ui.AppButton
import com.example.ecommerceapp.core.ui.AppTextField
import com.example.ecommerceapp.features.dashboard.account.order.domain.CustomerData
import com.example.ecommerceapp.features.dashboard.account.order.domain.Order
import com.example.ecommerceapp.features.dashboard.cart.ui.components.CartItem
import com.example.ecommerceapp.features.dashboard.cart.ui.components.EmptyCartScreen
import com.example.ecommerceapp.features.dashboard.cart.ui.components.PriceSection
import com.example.ecommerceapp.features.user.presentation.UserViewModel

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun CartScreen(cartNavController: NavController, ) {

    val cartViewModel: CartViewModel = hiltViewModel()
    val userViewModel : UserViewModel = hiltViewModel()
    var coupon by remember { mutableStateOf("") }
    val price by cartViewModel.totalPrice.collectAsState()
    val shippingPrice = 10
    val sharedPreferences = SharedPreferencesHelper(LocalContext.current)
    val currentUser by userViewModel.userDetails.collectAsState()
    var navigateToLogin by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        cartViewModel.getProducts()
       userViewModel.loadUserDetails()
    }
    val products by cartViewModel.productsState.collectAsState()
    Log.d("CartScreen", "Products: $products")
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(text = "Your Cart", style = productTitleStyle)
            Spacer(modifier = Modifier.height(32.dp))
            Divider(color = ColorsManager.NeutralLight, thickness = 2.dp)
            Spacer(modifier = Modifier.height(16.dp))
            if (products.isNotEmpty()) {
                products.forEach {
                    CartItem(it, cartViewModel)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min) // Makes the Row take the height of the tallest child
                ) {
                    AppTextField(
                        value = coupon,
                        label = "Enter Coupon Code",
                        onValueChange = { coupon = it },
                        modifier = Modifier
                            .weight(2f)

                    )
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 15.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorsManager.PrimaryColor),
                        shape = RectangleShape
                    ) {
                        Text(
                            text = "Apply",
                            style = TextStyle(
                                color = Color.White,
                                fontFamily = Poppins, // Poppins font family
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                PriceSection(cartViewModel)
                Spacer(modifier = Modifier.height(16.dp))
                AppButton(text = "Checkout") {
                    if (currentUser != null) {
                        val order =
                            Order(
                                products = products,
                                userId = currentUser!!.id,
                                customerData = CustomerData(
                                    "",
                                    "",
                                    ""
                                ), // Adjust to your default customer data structure
                                isPaid = false,
                                price = price+shippingPrice,
                            )

                        cartNavController.currentBackStackEntry?.savedStateHandle?.set("order", order)
                        cartNavController.navigate(Routes.ShipToScreen)
                    }else{
                        navigateToLogin = true
                    }

                }
                // navigate to login screen if user is not logged in
                if (navigateToLogin) {

                    AppAlertDialog(
                        title = "Login Required",
                        message = "You need to login to continue.",
                        imageResource = R.drawable.baseline_login_24,
                        buttonText ="Go To Login",
                        onDismiss = {
                            navigateToLogin = false
                        }
                    ) {
                        navigateToLogin = false
                        sharedPreferences.saveRoute(Routes.CartScreen)
                        cartNavController.navigate(Routes.Login)
                    }

                }

            } else {
                // if the cart is empty, show an empty cart screen
                EmptyCartScreen()
            }


        }
    }
}





