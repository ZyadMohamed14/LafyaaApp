package com.example.ecommerceapp.features.dashboard.search

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.BodyTextNormalRegular
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.MessageTextStyle
import com.example.ecommerceapp.core.theme.Poppins
import com.example.ecommerceapp.core.theme.productTitleStyle
import com.example.ecommerceapp.core.ui.AppButton

import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.home.domain.model.CategoryUIModel
import com.example.ecommerceapp.features.dashboard.home.screens.HomeViewModel
import com.example.ecommerceapp.features.dashboard.home.screens.components.SearchBar
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    var searchQuery by remember { mutableStateOf("") }
    val isVoiceSearchEnable by remember { mutableStateOf(false) }
    var showProductResultScreen by remember { mutableStateOf(false) }
    var showSearchFilter by remember { mutableStateOf(false) }
    val searchState by homeViewModel.productSearchState.collectAsState()

    // Reactive states for controlling UI visibility
    var isLoading by remember { mutableStateOf(false) }
    var noProductsFound by remember { mutableStateOf(false) }
    var isStartSearch by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // State list to hold products
    val products = remember { mutableStateListOf<ProductUIModel>() }
    val voiceRecognitionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val voiceQuery = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            voiceQuery?.let {
                searchQuery = it
                homeViewModel.searchProducts(searchQuery)  // Trigger the search with voice input
            }
        }
    }

    Log.d("showProductResultScreen", "showProductResultScreen: $showProductResultScreen")
    LaunchedEffect(searchState) {

        when (searchState) {

            is Resource.Error -> {
                isStartSearch = false
                isLoading = false
                noProductsFound = true
                isError = true
                val exception = searchState.exception

                // Create an error message based on the presence of exception or errorResponse
                errorMessage = when {
                    exception != null -> "Error fetching reviews: ${exception.message}"
                    else -> "Unknown error occurred"
                }

                products.clear()  // Clear any previous results
            }

            is Resource.Idle -> {
                isStartSearch = true
                isLoading = false
                noProductsFound = false
            }

            is Resource.Loading -> {
                isLoading = true
                noProductsFound = false
                products.clear()
            }

            is Resource.Success -> {
                isLoading = false
                isStartSearch = false
                noProductsFound = searchState.data.isNullOrEmpty()
                searchState.data?.let { products.addAll(it.filterNotNull()) }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    SearchBar(
                        searchQuery = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            isStartSearch = false  // Update to reflect user input
                            homeViewModel.searchProducts(searchQuery)
                        },
                        onConfirmSearch = {
                            showProductResultScreen= true
                        }
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.NavigateBefore,
                        contentDescription = "Voice Search",
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                },
                actions = {

                        IconButton(onClick = {
                            val speechRecognizerIntent =
                                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                    putExtra(
                                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                    )
                                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
                                }
                            voiceRecognitionLauncher.launch(speechRecognizerIntent)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice Search",
                                modifier = Modifier.size(50.dp)
                            )
                        }


                }
            )
        }
    ) { innerPadding ->
        when {
            isStartSearch -> {
                StartSearchScreen()
            }

            isLoading -> {
                LoadingShimmer()  // Show shimmer while loading
            }

            noProductsFound -> {
                NoProductsFound(navController)
            }

            showProductResultScreen -> {
                SearchResultsScreen(products = products,innerPadding)
            }

            isVoiceSearchEnable -> {
                VoiceSearchScreen(onVoiceSearchResult = { voiceQuery ->
                    searchQuery = voiceQuery
                    // Trigger your search logic with the spoken text
                })
            }

//            isError -> {
//                Text("Error: $errorMessage")
//            }
            else -> {
                if (products.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(10.dp)
                    ) {
                        items(products) { product ->
                            SearchProductItem(product) {

                            }
                        }
                    }
                }


            }
        }
    }
}

@Composable
fun NoProductsFound(
    navController: NavController
) {
    // Load and animate the Lottie composition
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.nodatafound))
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
            modifier = Modifier.size(150.dp)
        )




        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Products Found",
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Poppins,
                color = ColorsManager.Gray400
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        AppButton(text = "Back To Home") {
            navController.navigate(Routes.DashBoard)
        }
    }
}

@Composable
fun SearchProductItem(product: ProductUIModel, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onItemClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display the image on the left
        Image(
            painter = rememberImagePainter(data = product.images[0]),
            contentDescription = "${product.name} image",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(4.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = product.name,
            style = BodyTextNormalRegular.copy(fontSize = 20.sp),
            color = ColorsManager.NeutralGrey
        )
        // Display product name and other details on the right

    }
    Divider(modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
fun StartSearchScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "Start Search",
            modifier = Modifier.size(64.dp),
            tint = ColorsManager.PrimaryColor
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Search for products",
            style = productTitleStyle,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Type in the search bar above to find your favorite items",
            style = MessageTextStyle,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ShimmerLoadingItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth(0.5f)
                .placeholder(
                    visible = true,
                    color = Color.Gray,
                    shape = MaterialTheme.shapes.small,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun LoadingShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(5) {
            ShimmerLoadingItem()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

