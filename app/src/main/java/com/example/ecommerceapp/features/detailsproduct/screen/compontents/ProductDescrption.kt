package com.example.ecommerceapp.features.detailsproduct.screen.compontents

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.core.theme.BodyTextNormalRegular
import com.example.ecommerceapp.core.theme.NeutralGrey
import com.example.ecommerceapp.core.theme.NeuturalDark

@Composable
fun ProductDescrption(
    description:String
){
    val des ="The Nike Air Max 270 React ENG combines a full-length React foam midsole with a 270 Max Air unit for unrivaled comfort and a striking visual experience."
    Text(
        text = "Specification",
        color = NeuturalDark,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "Style:",
       style = BodyTextNormalRegular
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = description,
        style = BodyTextNormalRegular.copy(
            color = NeutralGrey,

        )
    )

}