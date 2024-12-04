package com.example.ecommerceapp.features.detailsproduct.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.CaptionLargeBold
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.ui.AppButton
import com.example.ecommerceapp.core.ui.EcommerceAppBar
import com.example.ecommerceapp.features.detailsproduct.domain.Review
import com.example.ecommerceapp.features.detailsproduct.screen.compontents.ReviewItem


@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun ReviewScreen(
    reviews: List<Review>, // Pass reviews directly as a list
    navController: NavController,
    onBackPressed: () -> Unit
) {
    var selectedRating by remember { mutableStateOf(0) } // Default to show all ratings (0 means no filter)

    // Filter the reviews based on the selected rating
    val filteredReviews = if (selectedRating == 0) {
        reviews // Show all reviews
    } else {
        reviews.filter { it.rating.toInt() == selectedRating } // Filter by selected rating
    }



    Scaffold(
        topBar = {
            EcommerceAppBar(title = "Reviews", onBackClick = onBackPressed)
        },
        floatingActionButton = {

            AppButton("Write a Review") {
                navController.currentBackStackEntry?.savedStateHandle?.set("productId", reviews[0].productId)
                navController.navigate(Routes.WriteReview)

            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(8.dp)
            //  .verticalScroll(rememberScrollState())
        ) {
            // Rating filter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                // "All Reviews" option to show all reviews
                Box(
                    modifier = Modifier
                        .background(ColorsManager.NeutralLight)
                        .height(50.dp)
                        .width(100.dp)
                        .clickable { selectedRating = 0 }, // Set to 0 to show all reviews
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "All Reviews", style = CaptionLargeBold)
                }
                Spacer(modifier = Modifier.width(8.dp))

                // Render star rating filters from 1 to 5
                for (i in 1..5) {
                    SortReviewItem(
                        rating = i,
                        isSelected = i == selectedRating, // Highlight selected rating
                        onClick = { selectedRating = i } // Set filter to selected rating
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 72.dp)
            ) {
                items(filteredReviews) { review ->
                    ReviewItem(review = review)
                }
            }


        }
    }
}


@Composable
fun SortReviewItem(
    rating: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF29B6F6) else Color.Transparent
    Box(
        modifier = Modifier
            .height(50.dp)
            .width(90.dp)
            .border(width = 3.dp, color = borderColor, shape = RectangleShape)
            .clickable { onClick() }, // Handle click to update filter
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = ColorsManager.PrimaryYellow
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = rating.toString())
        }
    }
}
