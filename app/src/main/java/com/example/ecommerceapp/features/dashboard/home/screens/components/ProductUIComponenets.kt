package com.example.ecommerceapp.features.dashboard.home.screens.components

import android.net.Uri
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.MediumPrimaryTextStyle
import com.example.ecommerceapp.core.theme.MediumTitleTextStyle
import com.example.ecommerceapp.core.theme.MessageTextStyle
import com.example.ecommerceapp.core.theme.SeeMoreTextStyle
import com.example.ecommerceapp.core.theme.TitleTextStyle
import com.example.ecommerceapp.features.dashboard.home.screens.HomeViewModel
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel
import com.example.ecommerceapp.features.detailsproduct.screen.compontents.RatingBar
import com.google.gson.Gson
import com.valentinilk.shimmer.shimmer

@Composable
fun FlashSaleSection(
    products: List<ProductUIModel>,
    navController: NavController

) {
    HeaderTitle(
        title = stringResource(R.string.flash_sale),
        seeMoreTitle = stringResource(R.string.see_more)
    ) {}
    Spacer(modifier = Modifier.height(4.dp))
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductItem(product) {

                navController.currentBackStackEntry?.savedStateHandle?.set("productId", product.id)
                navController.navigate(Routes.DetailsProducts)
            }
        }
    }
}
@Composable
fun MegaSaleSection(
    products: List<ProductUIModel>,
    navController: NavController

) {
    HeaderTitle(
        title = stringResource(R.string.mega_sale),
        seeMoreTitle = stringResource(R.string.see_more)
    ) {}
    Spacer(modifier = Modifier.height(4.dp))
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductItem(product) {
                val productJson = Uri.encode(Gson().toJson(product))
                navController.navigate("${Routes.DetailsProducts}?productJson=$productJson")
            }
        }
    }
}

@Composable
fun AllProductsSection(
    products: List<ProductUIModel>,
    navController: NavController,
    onLoadMore: () -> Unit
) {
    val isLoadingMore = remember { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier
            .heightIn(max = 1000.dp) // add this 😃
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductItem(product) {

                navController.navigate("${Routes.DetailsProducts}/${product.id}")
            }
        }

        // Load more when scrolled to the bottom
//        item {
//            LaunchedEffect(Unit) {
//                onLoadMore()
//            }
//        }
    }
}



@Composable
fun ProductItem(product: ProductUIModel, showRating: Boolean = false, onProductClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .padding(8.dp)
            .border(
                width = 1.dp, // Border width
                color = Color(0xFFEBF0FF), // Border color in hexadecimal
                shape = RoundedCornerShape(4.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(4.dp))
            .clickable { onProductClicked() }
            .padding(8.dp)
        ,verticalArrangement = Arrangement.Center
        ,horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Product Image
        Image(
            painter = rememberImagePainter(data = product.getFirstImage()),
            contentDescription = null,
            modifier = Modifier
                .size(110.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Product Name
        Text(
            text = product.name,
            style = MediumTitleTextStyle.copy(
                fontSize = 12.sp
            ), // Equivalent to MediumTitleTextStyle
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (showRating){
            Spacer(modifier = Modifier.height(4.dp))
            // Rating Bar
            RatingBar(
                rating = product.rate,
                modifier = Modifier.padding(top = 4.dp),
                onRatingChanged = {}
            )
        }


        Spacer(modifier = Modifier.height(4.dp))
        // Product Price
        Text(
            text = product.getFormattedPriceAfterSale(),
            maxLines = 2,
            style = MediumPrimaryTextStyle, // Equivalent to MediumPrimaryTextViewStyle
            modifier = Modifier.padding(top = 8.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Old Price & Sale Percentage Row
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Old Price with line-through
            Text(
                text = product.getFormattedPrice(),
                style = MessageTextStyle,
                fontSize = 10.sp,
                textDecoration = TextDecoration.LineThrough

            )

            // Sale Percentage
            Text(
                text = "${product.getFormattedSale()} off",
                color = ColorsManager.PrimaryRed,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun ShimmerProductList(){
    Row {
        repeat(6){
            ShimmerProductItem()
        }
    }
}
@Composable
fun ShimmerProductItem() {
    Column(
        modifier = Modifier
            .width(160.dp)
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
                .size(110.dp)
                .background(Color.Gray, shape = RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Placeholder for Product Name
        Box(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(0.8f)
                .background(Color.Gray, shape = RoundedCornerShape(4.dp))
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Placeholder for Product Price
        Box(
            modifier = Modifier
                .height(14.dp)
                .fillMaxWidth(0.5f)
                .background(Color.Gray, shape = RoundedCornerShape(4.dp))
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Placeholder for Old Price & Sale Percentage Row
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for Old Price
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(40.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Placeholder for Sale Percentage
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(40.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(4.dp))
            )
        }
    }
}




@Composable
fun HeaderTitle(
    title: String,
    seeMoreTitle: String,
    seeMoreAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title Text
        Text(
            text = title,
            style = TitleTextStyle, // Replace with your custom TextStyle equivalent to TitleTextStyle
            modifier = Modifier.weight(1f)
        )

        // More Categories Text
        Text(
            text = seeMoreTitle,
            style = SeeMoreTextStyle, // Replace with your custom TextStyle equivalent to MediumPrimaryTextViewStyle
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable {
                    seeMoreAction()
                }
        )
    }
}
