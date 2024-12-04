package com.example.ecommerceapp.features.detailsproduct.screen.compontents


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ecommerceapp.core.theme.ColorsManager


import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ImageSlider(images: List<String>) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp) // Fixed height for the slider
    ) {
        // Main Image Slider using HorizontalPager
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth() // Take available width
                .weight(1f) // Allow Column to resize
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = "Image $page",
                contentScale = ContentScale.Crop, // Preserves the aspect ratio
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)) // Clips the image to rounded corners
            )
        }

        // Indicator below the pager
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            activeColor = ColorsManager.PrimaryColor,
            inactiveColor = Color.Gray
        )
    }

    // Optional: Automatically scroll the pager every few seconds
    LaunchedEffect(pagerState) {
        while (true) {
            coroutineScope.launch {
                pagerState.animateScrollToPage((pagerState.currentPage + 1) % images.size)
            }
            delay(3000L) // Change slide every 3 seconds
        }
    }
}



