package com.example.ecommerceapp.ui.dashboard.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberImagePainter
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.ui.dashboard.home.model.SalesAdUIModel
import com.example.ecommerceapp.ui.dashboard.home.viewmodels.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val viewModel:HomeViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current.lifecycleScope

    val salesAds by viewModel.salesAdsState.collectAsState()
    val categories by viewModel.categoriesState.collectAsState()
    val flashSales by viewModel.flashSaleState.collectAsState()
    val megaSales by viewModel.megaSaleState.collectAsState()
    val isEmptyFlashSale by viewModel.isEmptyFlashSale.observeAsState(false)
    val isEmptyMegaSale by viewModel.isEmptyMegaSale.observeAsState(false)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    SearchBar(viewModel = viewModel)
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

            when(salesAds){
                is Resource.Error -> {
                  Log.d("salesAds","Errrrrrrrrrorrr")
                }
                is Resource.Loading -> {
                    Log.d("salesAds","Loading")
                    CircularProgressIndicator()
                }
                is Resource.Success -> {
                    Log.d("salesAds","Success")
                    // Sale Ads Section
                    Box(modifier = Modifier.fillMaxWidth()) {
                        salesAds.data?.let { SalesAdsViewPager(it) }
                    }
                }
            }


            // Category Section
            CategorySection(viewModel = viewModel)

            // Flash Sale Section
//            if (viewModel.isEmptyFlashSale()) {
//                FlashSaleSection(viewModel = viewModel)
//            }
//
//            // Mega Sale Section
//            if (viewModel.isEmptyMegaSale()) {
//                MegaSaleSection(viewModel = viewModel)
//            }
        }
    }
}

@Composable
fun SearchBar(viewModel: HomeViewModel) {
    OutlinedTextField(
        value = "",
        onValueChange = { /* Handle search input */ },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.ic_search),
                contentDescription = "Search"
            )
        },
        placeholder = {
            Text(text = stringResource(R.string.search_products))
        },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp)
    )
}
@OptIn( ExperimentalFoundationApi::class)
@Composable
fun SalesAdsViewPager(
    salesAds: List<SalesAdUIModel>,

) {

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

        // Custom page indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(salesAds.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
                            else Color.Gray
                        )
                )
            }
        }
    }
}
@Composable
fun SalesAdItem(
    salesAd: SalesAdUIModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clip(RoundedCornerShape(8.dp))
    ) {
        // Background Image
        Image(
            painter = rememberImagePainter(data = salesAd.imageUrl), // Coil or Glide equivalent
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        // Title Text
        Text(
            text = salesAd.title.toString(),
            color = Color.White,
            fontSize = 32.sp,
            fontFamily = FontFamily(Font(R.font.poppins_bold)),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
        )

        // Timer Layout (Hours:Minutes:Seconds)
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hours Text
            Text(
                text = salesAd.hours.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontSize = 24.sp
                )
            )

            // Colon
            Text(
                text = ":",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Minutes Text
            Text(
                text = salesAd.minutes.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontSize = 24.sp
                )
            )

            // Colon
            Text(
                text = ":",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Seconds Text
            Text(
                text = salesAd.seconds.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontSize = 24.sp
                )
            )
        }
    }
}

// Category Section
@Composable
fun CategorySection(viewModel: HomeViewModel) {


    @Composable
    fun FlashSaleSection(viewModel: HomeViewModel) {

    }

    @Composable
    fun MegaSaleSection(viewModel: HomeViewModel) {

    }

    @Composable
    fun ShimmerEffectContent() {

    }
}




