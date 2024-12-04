package com.example.ecommerceapp.features.detailsproduct.screen.compontents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.BodyTextNormalRegular
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.NeturalGrey
import com.example.ecommerceapp.core.theme.NeuturalDark
import com.example.ecommerceapp.core.theme.SeeMoreTextStyle
import com.example.ecommerceapp.features.detailsproduct.domain.Review
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductReviews(
    reviews: List<Review>,
    navController:NavController
) {
    val pagerState = rememberPagerState(pageCount = {
        reviews.size
    })
    var allReviewsRating: Float = 0.0f
    reviews.forEach { review ->
        allReviewsRating += review.rating/reviews.size
    }
    val coroutineScope = rememberCoroutineScope()

    val isReviewsExists = reviews.isNotEmpty()

    if (isReviewsExists) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Review Product",
                color = NeuturalDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "see More",
                style = SeeMoreTextStyle,
                modifier = Modifier.clickable {
                    navController
//                    val reviewsJson = Gson().toJson(reviews)
//                    val encodedReviewsJson = URLEncoder.encode(reviewsJson, StandardCharsets.UTF_8.toString())
//                    navController.navigate("${Routes.ReviewsScreen}/$encodedReviewsJson")
                    navController.currentBackStackEntry?.savedStateHandle?.set("reviews", reviews)
                    navController.navigate(Routes.ReviewsScreen)

                }
            )

        }
        Spacer(modifier = Modifier.height(5.dp))
        Row {
            RatingBar(
                rating = allReviewsRating,
                onRatingChanged = {}
            )

            Text(
                text = allReviewsRating.toInt().toString(),
                color = NeturalGrey,
                fontSize = 14.sp,
            )
            Text(
                text = "(${reviews.size} Reviews)",
                color = NeturalGrey,
                fontSize = 14.sp,
            )
        }
        Spacer(modifier = Modifier.height(7.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) { page ->
            ReviewItem(review = reviews[page])
        }
        LaunchedEffect(pagerState) {
            while (true) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % reviews.size)
                }
                delay(5000L) // Change slide every 3 seconds
            }
        }
    } else {
        Text(
            text = "This product does not have any reviews yet. Be the first to add your review!",
            style = BodyTextNormalRegular
        )
    }


}

@Composable
fun ReviewItem(review: Review) {
    val anonymousImage =
        "https://firebasestorage.googleapis.com/v0/b/onsale-7f4cb.appspot.com/o/person.png?alt=media&token=b4619a5f-c3fe-4f95-8021-80a3f5c221c1"
    val imageUrl =
        if (!review.userReview?.image.isNullOrEmpty()) review.userReview?.image else anonymousImage

    Row(

    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "userImage",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp) // Adjust size as needed
                .clip(CircleShape) // Apply circular shape
                .background(Color.White) // Optional: Background color in case of transparency
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(
                text = review.userReview?.name ?: "Unknown User",
                color = NeuturalDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            RatingBar(
                rating = review.rating,
                onRatingChanged = {}
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(text = review.comment, style = BodyTextNormalRegular)
            Spacer(modifier = Modifier.height(5.dp))
            if (!review.images.isNullOrEmpty()) {
                ProductReviewsImages(items = review.images)
            }
        }
    }


}

@Composable
fun ProductReviewsImages(items: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.forEachIndexed { index, item ->
            AsyncImage(
                model = items[index],
                contentDescription = "contentDescription",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp) // Adjust size as needed
            )
        }
    }
}

@Composable
fun NoReviewsSection(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Adds space between the text and the icon box
    ) {
        Text(
            text = "No reviews available, be the first to review this product.",
            style = BodyTextNormalRegular
        )
        // Add button with icon
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .background(ColorsManager.PrimaryColor, shape = CircleShape)
                .clickable {
                    // Handle the add review click action
                }
        ) {
            Icon(
                modifier = Modifier.size(24.dp).clickable {
                    onClick()
                },
                imageVector = Icons.Default.Add,
                contentDescription = "Add review",
                tint = Color.White
            )
        }
    }
}