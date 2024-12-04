package com.example.ecommerceapp.features.dashboard.home.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.local.SharedPreferencesHelper

import com.example.ecommerceapp.features.dashboard.home.screens.components.CategoriesList
import com.example.ecommerceapp.features.dashboard.home.screens.components.SalesAdsViewPager
import com.example.ecommerceapp.features.dashboard.home.screens.components.SearchBar
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.MediumPrimaryTextStyle
import com.example.ecommerceapp.core.theme.MediumTitleTextStyle
import com.example.ecommerceapp.core.theme.MessageTextStyle
import com.example.ecommerceapp.core.theme.SeeMoreTextStyle
import com.example.ecommerceapp.core.theme.TitleTextStyle
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.home.screens.components.AllProductsSection
import com.example.ecommerceapp.features.dashboard.home.screens.components.CategoryItemListShimmer
import com.example.ecommerceapp.features.dashboard.home.screens.components.FlashSaleSection
import com.example.ecommerceapp.features.dashboard.home.screens.components.MegaSaleSection
import com.example.ecommerceapp.features.dashboard.home.screens.components.SalesAdsShimmer
import com.example.ecommerceapp.features.dashboard.home.screens.components.ShimmerProductList
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel
import com.example.ecommerceapp.features.detailsproduct.domain.toProductUIModel
import com.example.ecommerceapp.features.detailsproduct.screen.compontents.RatingBar
import com.google.gson.Gson
import com.valentinilk.shimmer.shimmer

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val salesAds by viewModel.salesAdsState.collectAsState()
    val categories by viewModel.categoriesState.collectAsState()
    val flashSaleState by viewModel.flashSaleState.collectAsState()
    val megaSaleState by viewModel.megaSaleState.collectAsState()
    val allProductsState by viewModel.getAllProductsState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getAllProducts(10,null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    SearchBar(onSearchBarClick = {
                        navController.navigate(Routes.SearchScreen)
                    }){}
                },
                actions = {
                    IconButton(onClick = { /* Handle Favorite Click */ }) {
                        Icon(
                            painterResource(id = R.drawable.ic_favorite),
                            contentDescription = "Favorite"
                        )
                    }
                    IconButton(onClick = { /* Handle Notification Click */ }) {
                        Icon(
                            painterResource(id = R.drawable.ic_notification),
                            contentDescription = "Notification"
                        )
                    }
                },
                //colors = MaterialTheme.colorScheme.background
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            // TODo for sales Ads
            when (salesAds) {
                is Resource.Error -> {
                    Log.d("salesAds", "Errrrrrrrrrorrr")
                }

                is Resource.Loading -> {

                    Box(modifier = Modifier.fillMaxWidth()) {
                        SalesAdsShimmer()
                    }

                }

                is Resource.Success -> {
                    Log.d("salesAds", "Success${salesAds.data}")
                    // Sale Ads Section
                    Box(modifier = Modifier.fillMaxWidth()) {
                        salesAds.data?.let { SalesAdsViewPager(it, viewModel) }
                    }
                }

                is Resource.Idle -> TODO()
            }
            Spacer(modifier = Modifier.height(8.dp))
           when(categories){
               is Resource.Error -> {
                   Text(
                       text = "Error: ${(categories as Resource.Error).exception?.message ?: "Unknown error"}",
                       modifier = Modifier.align(Alignment.CenterHorizontally)
                   )
               }
               is Resource.Idle -> {}
               is Resource.Loading -> {
                   CategoryItemListShimmer()
               }
               is Resource.Success -> {
                   categories.data?.let { CategoriesList(categories = it, {}) }
               }
           }
            Spacer(modifier = Modifier.height(8.dp))
            // TODo for Flash Sale
            when (flashSaleState) {
                is Resource.Loading -> {
                    ShimmerProductList()
                }
                is Resource.Success -> {
                    val products = (flashSaleState as Resource.Success).data
                    if (!products.isNullOrEmpty()) {

                        FlashSaleSection(products.filterNotNull(),navController)
                    }

                }
                is Resource.Error -> {
                    Text(
                        text = "Error: ${(flashSaleState as Resource.Error).exception?.message ?: "Unknown error"}",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                is Resource.Idle ->{}
            }
            Spacer(modifier = Modifier.height(8.dp))
            // TODo for Mega Sale
            when (megaSaleState) {
                is Resource.Loading -> {
                    ShimmerProductList()

                }
                is Resource.Success -> {
                    val products = (megaSaleState as Resource.Success).data
                    if (!products.isNullOrEmpty()) {

                        MegaSaleSection(products.filterNotNull(),navController)
                    }

                }
                is Resource.Error -> {
                    Text(
                        text = "Error: ${(flashSaleState as Resource.Error).exception?.message ?: "Unknown error"}",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                is Resource.Idle ->{}
            }

            // get all Products
            when (allProductsState) {
                is Resource.Error -> {
                    // Handle error
                }
                is Resource.Idle -> {
                    // Handle idle
                }
                is Resource.Loading -> {
                    // Handle loading
                }
                is Resource.Success -> {
                    val productsResponse = (allProductsState as Resource.Success).data

                    if (productsResponse != null) {
                        val products = productsResponse.data
                        viewModel.lastVisibleId = products.last().id // Correctly aligned with the surrounding block

                        AllProductsSection(
                            products = products.map { it.toProductUIModel() },
                            navController = navController
                        ) {
                            viewModel.getAllProducts(10, viewModel.lastVisibleId)
                        }
                    }
                }
            }


        }
    }
}





