package com.example.ecommerceapp.features.dashboard.account.order.ui

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.core.local.SharedPreferencesHelper
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.ui.EcommerceAppBar
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.account.order.domain.Order
import com.example.ecommerceapp.features.dashboard.account.order.ui.component.OrderDetails
import com.example.ecommerceapp.features.dashboard.account.order.ui.component.OrderProgressState

@Composable

fun OrderDetailsScreen(order: Order, onBackClick: () -> Unit) {

    Scaffold(
        topBar = {
            EcommerceAppBar(title = "Orders", onBackClick = {

                onBackClick()
            })
        }
    ) { innerPadding ->

        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ) {
            Divider()

            OrderProgressState(order.status)
            Spacer(modifier = Modifier.height(16.dp))
            OrderDetails(order = order)

        }
    }

}
