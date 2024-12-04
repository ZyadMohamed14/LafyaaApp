package com.example.ecommerceapp.features.dashboard.search

import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.features.dashboard.home.screens.components.ProductItem
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel

@Composable
fun SearchResultsScreen(products: List<ProductUIModel>, paddingValues: PaddingValues) {
    var filteredItems = remember {
        mutableStateOf("")
    }
    products.forEach {
        Log.d("SearchResultsScreen", it.salePercentage.toString())
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${products.size} results",
                color = ColorsManager.NeutralGrey,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = {

            }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter Search",
                    tint = ColorsManager.NeutralGrey,
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Defines a grid with 2 fixed columns
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(products) { product ->
                // Use your ProductItem composable to display each product
                ProductItem(product = product, onProductClicked = { /* Handle click */ })
            }
        }
    }

}