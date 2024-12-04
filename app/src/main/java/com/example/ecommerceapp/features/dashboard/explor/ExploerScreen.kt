package com.example.ecommerceapp.features.dashboard.explor

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.Poppins
import com.example.ecommerceapp.core.theme.productAppBarstyle
import com.example.ecommerceapp.core.ui.AppButton
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.home.domain.model.CategoryUIModel
import com.example.ecommerceapp.features.dashboard.home.screens.HomeViewModel
import com.example.ecommerceapp.features.dashboard.home.screens.components.CategoryItem
import com.example.ecommerceapp.features.dashboard.home.screens.components.SearchBar
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ExploreScreen(
    navController: NavController
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val categoriesState by homeViewModel.categoriesState.collectAsState()

    // State lists to hold filtered categories
    val menCategories = remember { mutableStateListOf<CategoryUIModel>() }
    val womenCategories = remember { mutableStateListOf<CategoryUIModel>() }


    // Populate lists based on categories state
    LaunchedEffect(categoriesState) {
        when (categoriesState) {
            is Resource.Error -> {
                // Handle error state if needed
            }

            is Resource.Idle -> {
                // Handle idle state if needed
            }

            is Resource.Loading -> {
                // Handle loading state if needed
            }

            is Resource.Success -> {
                menCategories.clear()
                womenCategories.clear()
                val categories = (categoriesState as Resource.Success<List<CategoryUIModel>>).data
                categories?.forEach { category ->
                    if (category.name?.contains("Men") == true) {
                        menCategories.add(category)
                    } else if (category.name?.contains("Women") == true) {
                        womenCategories.add(category)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { SearchBar(
                  onSearchBarClick = {
                      navController.navigate(Routes.SearchScreen)
                  }
                ){

                } },
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
            )
        }
    ) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Men’s Fashion Section
                item {
                    Text(text = "Men's Fashion", style = productAppBarstyle)
                }

                // Men’s Categories Grid
                items(menCategories.chunked(4)) { chunkedCategories ->
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(chunkedCategories) { category ->
                            CategoryItem(category = category) {
                                // Handle category item click
                            }
                        }
                    }
                }
                item {
                    Divider()
                }
                // Women’s Fashion Section
                item {
                    Text(text = "Women's Fashion", style = productAppBarstyle)
                }

                // Women’s Categories Grid
                items(womenCategories.chunked(4)) { chunkedCategories ->
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(chunkedCategories) { category ->
                            CategoryItem(category = category) {}
                        }
                    }
                }
            }


    }
}

