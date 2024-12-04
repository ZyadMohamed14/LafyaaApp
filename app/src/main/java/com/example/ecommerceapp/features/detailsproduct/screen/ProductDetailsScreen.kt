package com.example.ecommerceapp.features.detailsproduct.screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.PrimaryColor
import com.example.ecommerceapp.core.theme.White
import com.example.ecommerceapp.core.ui.EcommerceAppBar
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.cart.ui.CartViewModel
import com.example.ecommerceapp.features.dashboard.home.screens.HomeViewModel
import com.example.ecommerceapp.features.detailsproduct.domain.ProductOrder
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel
import com.example.ecommerceapp.features.detailsproduct.screen.compontents.ImageSlider
import com.example.ecommerceapp.features.detailsproduct.screen.compontents.NoReviewsSection
import com.example.ecommerceapp.features.detailsproduct.screen.compontents.ProductDetials
import com.example.ecommerceapp.features.detailsproduct.screen.compontents.ProductReviews

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailsScreen(
  //  productUIModel: ProductUIModel,
    productId: String="",
    navController: NavController,
    navigateBack: () -> Unit
) {

    val reviewViewModel: ReviewsViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val prdouctState by homeViewModel.prdouctIdState.collectAsState()
    // Collect reviews as state
    val reviewsState by reviewViewModel.reviewsStates.collectAsState(initial = Resource.Loading())
    val isPrductAdded by homeViewModel.isAddSuccessfully.collectAsState(initial = false)
    var selectedSize by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("") }
    var isColorEmpty by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        homeViewModel.getProductById(productId)

    }
    Scaffold(
        topBar = {
            EcommerceAppBar(title = "Product Details", onBackClick = {
                navigateBack()
            })
        },

        ) { innerPadding ->
       when(prdouctState){
           is Resource.Error -> {
               Text(text = "Error")
           }
           is Resource.Idle -> {}
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
               val productUIModel = (prdouctState as Resource.Success).data!!
               LaunchedEffect(Unit) {
                   if (productUIModel.id.isNotEmpty()) {
                       reviewViewModel.resetReviewsCollection()
                       reviewViewModel.getReviewsByProductId(productUIModel.id)
                   }
               }
               Column(
                   modifier = Modifier
                       .fillMaxSize()
                       .padding(innerPadding)
                       .padding(16.dp)
                       .verticalScroll(
                           rememberScrollState()
                       )
               ) {


                   ImageSlider(productUIModel.images)
                   ProductDetials(productUIModel, onSizeSelected = {
                       selectedSize= it
                   }){
                       selectedColor= it
                   }
                   Spacer(modifier = Modifier.height(16.dp))
                   when (reviewsState) {
                       is Resource.Loading -> {
                           // Show a loading indicator
                           CircularProgressIndicator()
                       }

                       is Resource.Success -> {
                           val reviews = (reviewsState as Resource.Success).data
                           if (reviews.isNullOrEmpty()) {
                               NoReviewsSection(){

                                   navController.currentBackStackEntry?.savedStateHandle?.set("productId", productUIModel.id)
                                   navController.navigate(Routes.WriteReview)
                               }
                           } else {

                               ProductReviews(reviews, navController)
                           }
                       }

                       is Resource.Error -> {
                           val errorResponse = (reviewsState as Resource.Error).errorResponse
                           val exception = reviewsState.exception

                           // Create an error message based on the presence of exception or errorResponse
                           val message = when {
                               exception != null -> "Error fetching reviews: ${exception.message}"
                               errorResponse != null -> "Error fetching reviews: ${errorResponse.message}"
                               else -> "Unknown error occurred"
                           }

                           Text("No reviews available.")
                       }

                       is Resource.Idle -> {}
                   }

                   Button(
                       onClick = {
                           if (selectedColor.isEmpty()) {
                               isColorEmpty = true
                               return@Button
                           }
                           val productOrder = ProductOrder(
                               name = productUIModel.name,
                               price = productUIModel.price,
                               image = productUIModel.images[0],
                               size = selectedSize,
                               category = productUIModel.category,
                               color = selectedColor
                           )
                           homeViewModel.addToCart(productOrder)
                       },
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(16.dp)
                           .height(52.dp),
                       colors = ButtonDefaults.buttonColors(

                           containerColor = PrimaryColor
                       ),
                       shape = RoundedCornerShape(16.dp)
                   ) {
                       Text(
                           text = "Add to Cart",
                           style = TextStyle(
                               fontSize = 16.sp,
                               color = White,
                               fontWeight = FontWeight.Bold
                           )
                       )
                   }
                   if (isPrductAdded) {
                       Toast.makeText(
                           LocalContext.current,
                           "Product Added Successfully",
                           Toast.LENGTH_SHORT
                       ).show()
                   }
                   if (isColorEmpty) {
                       Toast.makeText(
                           LocalContext.current,
                           "Please select a size",
                           Toast.LENGTH_SHORT
                       ).show()
                       isColorEmpty = false
                   }
               }
           }
       }
    }
}
