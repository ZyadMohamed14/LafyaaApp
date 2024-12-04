package com.example.ecommerceapp.features.dashboard.home.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.ecommerceapp.R

import com.example.ecommerceapp.features.dashboard.home.domain.model.CategoryUIModel
import com.example.ecommerceapp.core.theme.BodyTextNormalRegular
import com.example.ecommerceapp.core.theme.ColorsManager
import com.valentinilk.shimmer.shimmer

@Composable
fun CategoriesList(
    categories: List<CategoryUIModel>,
    onCategoryClick: (CategoryUIModel) -> Unit
) {
    HeaderTitle(
        title = stringResource(R.string.category),
        seeMoreTitle = stringResource(R.string.more_categories)
    ) {}
    Spacer(modifier = Modifier.height(4.dp))
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category = category, onClick = onCategoryClick)
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryUIModel,
    onClick: (CategoryUIModel) -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .padding(8.dp)
            .clickable { onClick(category) }, // Handle item click
        horizontalAlignment = Alignment.CenterHorizontally // Center the content
    ) {
        // ImageView equivalent in Compose with a background
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(ColorsManager.NeutralLight, CircleShape)
                .padding(24.dp)
        ) {
            Image(
                painter = rememberImagePainter(category.icon), // Assuming category.icon is a URL
                contentDescription = "Category Icon",
                modifier = Modifier.fillMaxSize()
            )
        }

        // TextView equivalent in Compose
        category.name?.let {
            Text(
                text = it,
                style = BodyTextNormalRegular,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally) // Align text to the center
            )
        }
    }
}
@Composable
fun CategoryItemListShimmer() {
    Row {
        repeat(5) {
            CategoryItemShimmer()
        }
    }

}

@Composable
fun CategoryItemShimmer() {
    Column(
       modifier = Modifier
           .wrapContentWidth()
           .padding(8.dp)
           .shimmer()
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.Gray, CircleShape)
                .padding(24.dp)
        )

    }
}
