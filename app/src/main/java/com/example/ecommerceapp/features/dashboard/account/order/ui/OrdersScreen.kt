package com.example.ecommerceapp.features.dashboard.account.order.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.local.SharedPreferencesHelper
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.BodyTextNormalRegular
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.PriceTextStyle
import com.example.ecommerceapp.core.theme.TitleTextStyle
import com.example.ecommerceapp.core.ui.AppButton
import com.example.ecommerceapp.core.ui.EcommerceAppBar
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.account.order.domain.Order

@Composable
fun OrdersScreen(navController: NavController, onBackClick: () -> Unit) {
    val orderViewModel: OrderViewModel = hiltViewModel()
    val orderState by orderViewModel.ordersState.collectAsState()
    val isLoading by remember { mutableStateOf(false) }
    var isOrdersFetched by orderViewModel.isOrdersFetched
    val userId = SharedPreferencesHelper(LocalContext.current).getUserId()
    LaunchedEffect(Unit) {
        if (userId != null) {
            if (!isOrdersFetched) {
                orderViewModel.getOrders(userId)
            }

        }

    }
    Scaffold(
        topBar = {
            EcommerceAppBar(title = "Orders", onBackClick = {

                onBackClick()
            })
        }
    ) { innerPadding ->

        when (orderState) {
            is Resource.Error -> {
                ErrorOrdersScreen() {
                    if (userId != null) {
                        orderViewModel.getOrders(userId)
                    }
                }
            }

            is Resource.Idle -> {
                if (userId == null) {
                    EmptyOrderScreen()
                }
            }

            is Resource.Loading -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {

                    CircularProgressIndicator(
                        color = ColorsManager.PrimaryColor,
                        strokeWidth = 4.dp
                    )
                }
            }

            is Resource.Success -> {

                val orders = (orderState as Resource.Success).data
                if (!orders.isNullOrEmpty()) {
                    orderViewModel.isOrdersFetched.value = true
                    isOrdersFetched = true
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        items(orders) { order ->
                            OrderCard(order = order, onOrderClick = {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "order",
                                    order
                                )
                                navController.navigate(Routes.OrderDetailsScreen)
                            })
                        }
                    }
                } else {
                    EmptyOrderScreen()
                }


            }
        }


    }

}


@Composable
fun OrderCard(order: Order, onOrderClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(1.dp, ColorsManager.NeutralLight)
            .clickable {
                onOrderClick()
            }
    ) {
        val noOfItems = order.products.size
        val items =
            if (noOfItems > 1) " ${noOfItems} items purchased" else "${noOfItems} item purchased"
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = order.customerData.customerName, style = TitleTextStyle)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Order at Lafyuu : ${order.createdAt} ")

            OrderCardItem(
                title = "Order Status",
                value = order.status,
                style = BodyTextNormalRegular.copy(color = ColorsManager.PrimaryDarkColor)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OrderCardItem(
                title = "Items",
                value = items,
                style = BodyTextNormalRegular.copy(color = ColorsManager.PrimaryDarkColor)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OrderCardItem(
                title = "Price",
                value = "${order.price}",
                style = PriceTextStyle.copy(fontSize = 12.sp)
            )
        }

    }

}

@Composable
fun OrderCardItem(title: String, value: String, style: TextStyle) {
    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = title, style = BodyTextNormalRegular, modifier = Modifier.weight(1f))
        Text(text = value, style = style)
    }
}

@Composable
fun EmptyOrderScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ordernow))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever // Loop animation indefinitely
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.size(300.dp)
        )
        Text(text = " you don't have any order yet", style = TitleTextStyle)

        Spacer(modifier = Modifier.height(16.dp))


    }
}

@Composable
fun ErrorOrdersScreen(onClick: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.errororders))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever // Loop animation indefinitely
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.size(300.dp)
        )
        Text(text = "Failed to load orders", style = TitleTextStyle)

        Spacer(modifier = Modifier.height(16.dp))
        AppButton(text = "Retry", onClick = {
            onClick()
        })


    }
}