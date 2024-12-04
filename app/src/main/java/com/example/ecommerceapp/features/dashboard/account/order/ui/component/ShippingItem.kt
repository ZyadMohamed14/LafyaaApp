package com.example.ecommerceapp.features.dashboard.account.order.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.example.ecommerceapp.core.theme.BodyTextNormalRegular
import com.example.ecommerceapp.core.theme.ColorsManager


@Composable
fun ShippingDetailsitem(
    title: String,
    value: String,
    textStyle: TextStyle = BodyTextNormalRegular,
    valueTextStyle: TextStyle = BodyTextNormalRegular.copy(color = ColorsManager.PrimaryDarkColor)
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, style = textStyle)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = value, style = valueTextStyle)
    }
}