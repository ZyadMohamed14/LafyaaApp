package com.example.ecommerceapp.features.payment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ecommerceapp.R
import com.example.ecommerceapp.app.MainActivity
import com.example.ecommerceapp.core.ui.AppButton
import com.example.ecommerceapp.core.ui.AppLoadingDialog
import com.example.ecommerceapp.features.dashboard.account.order.domain.Order
import com.example.ecommerceapp.features.dashboard.account.order.ui.OrderViewModel
import com.example.ecommerceapp.features.dashboard.cart.ui.CartViewModel
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : ComponentActivity() {


    private val viewModel: PaymentViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()
    lateinit var order: Order


    private lateinit var paymentSheet: PaymentSheet
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        order = intent.getParcelableExtra<Order>("order")!!
        paymentSheet = PaymentSheet(this, ::onPaymentResult)

        setContent {
            PaymentScreen(order,viewModel = viewModel,orderViewModel = orderViewModel, paymentSheet = paymentSheet)
        }
    }
    private fun onPaymentResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed ->{
                orderViewModel.createOrder(order = order)
                viewModel.onPaymentSuccess()
            }
            is PaymentSheetResult.Canceled -> viewModel.onPaymentCancelled()
            is PaymentSheetResult.Failed -> viewModel.onPaymentFailed(paymentSheetResult.error.message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    order: Order?,
    viewModel: PaymentViewModel,
    orderViewModel: OrderViewModel,
    paymentSheet: PaymentSheet
) {
    val navController = rememberNavController()

    val paymentResult by viewModel.paymentResult.collectAsState()
    val cartViewModel : CartViewModel = hiltViewModel()
    val createOrderState by orderViewModel.createOrderState.collectAsState()
    val context = LocalContext.current
val isOrderCreated by orderViewModel.isOrderCreated
    var isLoaded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Order Details") })
        },
        content = { innerPadding ->
            when (paymentResult) {
                is PaymentState.Success -> {
                    // Observe the order creation state
                    isLoaded = true
                    when (createOrderState) {

                        true -> {
                            order!!.products.forEach {
                                cartViewModel.removeFromCart(it)
                            }
                            isLoaded = false
                            PaymentSuccessScreen(onNavigateBack = {
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(intent)
                            })


                        }
                        false -> {

                            if(isOrderCreated){
                                Toast.makeText(LocalContext.current, "failed to create order \n we will try create one again", Toast.LENGTH_SHORT).show()
                                orderViewModel.createOrder(order = order!!)
                            }

                        }
                    }
                }

                is PaymentState.Error -> {
                    val errorMessage = (paymentResult as PaymentState.Error).message
                    PaymentFailureScreen(errorMessage = errorMessage, onNavigateBack = {
                        viewModel.onPaymentCancelled()
                    })
                }

                else -> {
                    PaymentOrderDetailsScreen(order!!,navController, innerPadding,paymentSheet,viewModel)
                }
            }
            if (isLoaded) {
                AppLoadingDialog {

                }
            }
        }
    )
}



@Composable
fun PaymentSuccessScreen(onNavigateBack: () -> Unit) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.paymentsucesss))
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
         Text(text = "Payment Successful", style = MaterialTheme.typography.headlineMedium, color = Color.Green)

        Spacer(modifier = Modifier.height(16.dp))
        AppButton(text = "Go Home") {
            onNavigateBack()

        }


    }

}

@Composable
fun PaymentFailureScreen(errorMessage: String, onNavigateBack: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.faildpayment))
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
         Text(text = "an error occured while processing your Payment ", style = MaterialTheme.typography.headlineMedium, color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))
        AppButton(text = "Try Again") {
            onNavigateBack()

        }


    }
}


@Preview(showBackground = true)
@Composable
fun PaymentFailureScreenPreview() {
    PaymentFailureScreen(
        errorMessage = "An error occurred while processing your payment.",
        onNavigateBack = { /* No-op for preview */ }
    )
}