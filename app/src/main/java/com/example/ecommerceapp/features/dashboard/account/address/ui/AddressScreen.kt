package com.example.ecommerceapp.features.dashboard.account.address.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
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
import com.example.ecommerceapp.core.theme.TitleTextStyle
import com.example.ecommerceapp.core.ui.AppButton
import com.example.ecommerceapp.core.ui.EcommerceAppBar
import com.example.ecommerceapp.features.dashboard.account.address.domain.Address
import com.google.gson.Gson
import okhttp3.Route
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AddressScreen(navController: NavController, onBackClick: () -> Unit) {
    val addressViewModel: AddressViewModel = hiltViewModel()
    val addresses by addressViewModel.addresses.collectAsState()
    var selectedAddressId by remember { mutableStateOf<Int?>(null) } // Track selected address

    LaunchedEffect(Unit) {
        addressViewModel.getAddresses()
    }

    Scaffold(
        topBar = {
            EcommerceAppBar(title = "Address", onBackClick = {

                onBackClick()
            })
        }
    ) { innerPadding ->

        if (addresses.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                addresses.forEach { address ->
                    AddressItem(
                        address = address,
                        onAddressClick = {
                            // see if there are any other address selected to make it unselected
                        addressViewModel.selectAddress(address)

                        },
                        onEditClick = {
                            val addressJson = Gson().toJson(address)
                            val encodedAddressJson = URLEncoder.encode(addressJson, StandardCharsets.UTF_8.toString())
                            navController.navigate("${Routes.AddOrEditAddressScreen}/$encodedAddressJson")
                        },
                        onDeleteClick = {
                            addressViewModel.deleteAddress(address)
                        },

                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                AppButton(text = "Add Address") {
                    val encodedAddressJson = null
                    navController.navigate("${Routes.AddOrEditAddressScreen}/$encodedAddressJson")
                }
            }

        } else {
            NoAddressFound(navController)
        }


    }
}

@Composable
fun NoAddressFound( navController: NavController) {
    // Load and animate the Lottie composition

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.noaddress))
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
        // Lottie animation instead of static image

        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Spacer(modifier = Modifier.height(16.dp))
        AppButton(text = "Add Address") {
            navController.navigate("${Routes.AddOrEditAddressScreen}?")

        }


    }
}

@Composable
fun AddressItem(
    address: Address,
    onAddressClick: () -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit
) {
    Log.d("AddressItem", "Address: ${address.toString()}")
    val isSelected = address.isSelect
    val color = if (isSelected) ColorsManager.PrimaryColor else ColorsManager.NeutralLight
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(2.dp, color = color)
            .clickable {
                onAddressClick()
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = address.userFullName, style = TitleTextStyle)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = buildString {
                append(address.details)
                append(" , ")
                append(address.city)
            }, style = BodyTextNormalRegular)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = address.phone, style = BodyTextNormalRegular)
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                TextButton(
                    onClick = {onEditClick()},
                    modifier = Modifier.background(color = ColorsManager.PrimaryColor)
                ) {
                    Text(text = "Edit", color = ColorsManager.White)
                }
                Icon(imageVector = Icons.Default.Delete, contentDescription = "delet Address",
                    tint = ColorsManager.NeutralGrey, modifier = Modifier
                        .size(40.dp)

                        .clickable {
                            onDeleteClick()
                        })
            }
        }
    }
}