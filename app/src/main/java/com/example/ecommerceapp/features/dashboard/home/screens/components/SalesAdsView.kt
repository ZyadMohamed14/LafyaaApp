package com.example.ecommerceapp.features.dashboard.home.screens.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ecommerceapp.R
import com.example.ecommerceapp.features.dashboard.home.domain.model.SalesAdUIModel
import com.example.ecommerceapp.features.dashboard.home.screens.HomeViewModel
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.TitleTextStyle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@OptIn( ExperimentalFoundationApi::class)
@Composable
fun SalesAdsViewPager(
    salesAds: List<SalesAdUIModel>,
    viewModel: HomeViewModel

) {
    Log.d("SalesAdsViewPager", "Sales Ads Count: ${salesAds.size}")
    val pagerState = rememberPagerState(pageCount = {
        salesAds.size
    })

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            SalesAdItem(salesAd = salesAds[page])
        }


    }
}
@Composable
fun SalesAdItem(
    salesAd: SalesAdUIModel,
) {
    // Collect state for days, hours, minutes, and seconds
    val days by salesAd.days.collectAsState()
    val hours by salesAd.hours.collectAsState()
    val minutes by salesAd.minutes.collectAsState()
    val seconds by salesAd.seconds.collectAsState()

    // Ensure the countdown starts when the Composable is first launched
    LaunchedEffect(key1 = salesAd.endAt) {
        salesAd.startCountdown() // Start the countdown when the 'endAt' value changes
    }

    // Safe parsing: Ensure that empty or invalid values are handled gracefully
    val parsedDays = salesAd.safeParseInt(days)
    val parsedHours = salesAd.safeParseInt(hours)
    val parsedMinutes = salesAd.safeParseInt(minutes)
    val parsedSeconds = salesAd.safeParseInt(seconds)

    Box(
        modifier = Modifier
            .height(250.dp)
            .background(ColorsManager.PrimaryColor)
            .clip(RoundedCornerShape(8.dp))
    ) {
        // Background Image
        Image(
            painter = rememberAsyncImagePainter(model = salesAd.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        // Title Text
        Text(
            text = salesAd.title ?: "", // Handle nullability
            color = Color.White,
            fontSize = 32.sp,
            fontFamily = FontFamily(Font(R.font.poppins_bold)),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
        )

        // Timer Layout (Days:Hours:Minutes:Seconds)
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Days Text (only show if days remaining is greater than 0)
            if (parsedDays > 0) {
                Text(
                    text = "End in $parsedDays days",
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp)) // Rounded corners with 4dp radius
                        .background(Color.White)        // Solid white background
                        .padding(8.dp),
                    style = TitleTextStyle
                )
            }else{
                // Colon separator (only show if days are 0)
                if (parsedDays == 0) {
                    Text(
                        text = ":",
                        color = Color.White,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                // Hours Text
                Text(
                    text = "$parsedHours",
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp)) // Rounded corners with 4dp radius
                        .background(Color.White)        // Solid white background
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Black, // Text color inside box
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontSize = 24.sp
                    )
                )

                // Colon separator
                Text(
                    text = ":",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                // Minutes Text
                Text(
                    text = "$parsedMinutes",
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White)
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontSize = 24.sp
                    )
                )

                // Colon separator
                Text(
                    text = ":",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                // Seconds Text
                Text(
                    text = "$parsedSeconds",
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White)
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontSize = 24.sp
                    )
                )
            }


        }
    }
}
@Composable
fun SalesAdsShimmer(){
    Column(
        modifier = Modifier
            
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFEBF0FF),
                shape = RoundedCornerShape(4.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(4.dp))
            .padding(8.dp)
            .shimmer()
        ,verticalArrangement = Arrangement.Center
        ,horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Placeholder for Product Image
        Box(
            modifier = Modifier
                .fillMaxWidth().height(250.dp)
                .background(Color.Gray, shape = RoundedCornerShape(4.dp))
        )
    }

}