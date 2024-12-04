package com.example.ecommerceapp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.theme.Poppins

@Composable
fun OrDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        // Horizontal line in the background
        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            thickness = 1.dp,
            modifier = Modifier.align(Alignment.Center)
        )

        // Text in the center of the line
        Text(
            text = stringResource(id = R.string.or),
            color = MaterialTheme.colorScheme.secondary,
            fontFamily = Poppins,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 8.dp)
                .align(Alignment.Center),
            style = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.Center),
            textAlign = TextAlign.Center
        )
    }
}
