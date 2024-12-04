package com.example.ecommerceapp.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.theme.NeturalGrey
import com.example.ecommerceapp.core.theme.White
import com.example.ecommerceapp.core.theme.productAppBarstyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcommerceAppBar(
    title: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = productAppBarstyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_ios_24),
                    contentDescription = "Back",
                    tint = NeturalGrey
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = White,
            titleContentColor = White,
            navigationIconContentColor = White
        )
    )
}
