package com.example.ecommerceapp.ui.products.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.R

val Poppins = FontFamily(
    Font(R.font.poppins_regular),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

val productAppBarstyle= TextStyle(
    color = NeuturalDark,
    fontFamily = Poppins,       // Poppins font family
    fontWeight = FontWeight.Bold, // Font weight 700
    fontSize = 16.sp,            // Font size 16px
    lineHeight = 24.sp,          // Line height 24px
    letterSpacing = 0.5.sp,      // Letter spacing 0.5px
    textAlign = TextAlign.Left   // Text alignment
)
val productTitleStyle=TextStyle(
    color = NeuturalDark,
    fontFamily = Poppins,       // Poppins font family
    fontWeight = FontWeight.Bold, // Font weight 700
    fontSize = 20.sp,            // Font size 16px
    lineHeight = 30.sp,          // Line height 24px
    letterSpacing = 0.5.sp,      // Letter spacing 0.5px
    textAlign = TextAlign.Left   // Text alignment
)
val PriceTextStyle = TextStyle(
    fontFamily = Poppins,
    color = PrimaryColor,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 30.sp,
    letterSpacing = 0.5.sp,
    textAlign = TextAlign.Left
)
val sizeProductStyle = TextStyle(
    color = NeuturalDark,
    fontFamily = FontFamily.Default,  // Replace with Poppins if you have it as a font asset
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold,  // Equivalent to 700
    lineHeight = 21.sp,
    textAlign = TextAlign.Center
)
val BodyTextNormalRegular = TextStyle(
    fontFamily = FontFamily(Font(resId = R.font.poppins)), // Add your Poppins font resource
    fontSize = 12.sp,
    color = NeuturalDark,
    fontWeight = FontWeight.W400, // Normal weight
    lineHeight = 21.6.sp, // 21.6px converted to sp 
    textAlign = TextAlign.Left
)
val SeeMoreTextStyle = TextStyle(
    fontFamily = Poppins,
    color = PrimaryColor,
    fontSize = 14.sp,
    fontWeight = FontWeight.W700, // Bold weight
    lineHeight = 21.sp, // Line height in sp
    textAlign = TextAlign.End // Align text to the right
)
