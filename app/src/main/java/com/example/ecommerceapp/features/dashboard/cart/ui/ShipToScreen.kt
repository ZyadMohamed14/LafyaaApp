package com.example.ecommerceapp.features.dashboard.cart.ui

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.ui.AppButton
import com.example.ecommerceapp.core.ui.EcommerceAppBar
import com.example.ecommerceapp.features.dashboard.account.address.domain.Address
import com.example.ecommerceapp.features.dashboard.account.address.ui.AddressItem
import com.example.ecommerceapp.features.dashboard.account.address.ui.AddressViewModel
import com.example.ecommerceapp.features.dashboard.account.address.ui.NoAddressFound
import com.example.ecommerceapp.features.dashboard.account.order.domain.CustomerData
import com.example.ecommerceapp.features.dashboard.account.order.domain.Order
import com.example.ecommerceapp.features.payment.PaymentActivity
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ShipToScreen(navController: NavController, order: Order, onBackClick: () -> Unit) {
    val addressViewModel: AddressViewModel = hiltViewModel()

    val addresses by addressViewModel.addresses.collectAsState()
     val context = LocalContext.current
     var selectedAddress by remember {
         mutableStateOf(Address())
     }
    LaunchedEffect(Unit) {
        addressViewModel.getAddresses()
    }

    Scaffold(
        topBar = {
            EcommerceAppBar(title = "ShipTo", onBackClick = {

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
                   if(address.isSelect){
                       selectedAddress=address
                   }
                    AddressItem(
                        address = address,
                        onAddressClick = {
                            // see if there are any other address selected to make it unselected
                            selectedAddress=address
                            addressViewModel.selectAddress(address)

                        },
                        onEditClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set("address", address)
                            navController.navigate(Routes.AddOrEditAddressScreen)

                        },
                        onDeleteClick = {
                            addressViewModel.deleteAddress(address)
                        },

                        )
                }
                Spacer(modifier = Modifier.height(16.dp))
                AppButton(text = "Pay $${order.price}") {


                    if(selectedAddress.isSelect==false){
                        Toast.makeText(context, "Please select an address", Toast.LENGTH_SHORT).show()
                        return@AppButton
                    }

                    val customerData = CustomerData(
                        customerName = selectedAddress.userFullName,
                        customerPhone = selectedAddress.phone,
                        address = selectedAddress.details,
                    )

                     val updatedOrder = order.copy(customerData = customerData)
                    Log.d("betgrtfgnz", "Checkout order ${updatedOrder}")
                    val intent = Intent(context, PaymentActivity::class.java)
                    intent.putExtra("order", updatedOrder)

                    context.startActivity(intent)

                }
            }

        } else {
            NoAddressFound(navController)
        }


    }
}