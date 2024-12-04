package com.example.ecommerceapp.core.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.R

val Poppins = FontFamily(
    Font(R.font.poppins_regular),
    Font(R.font.poppins_bold, FontWeight.Bold)
)
val CaptionLargeBold = TextStyle(
    fontFamily = FontFamily(Font(R.font.poppins)), // Ensure the Poppins font is added in res/font
    fontSize = 12.sp,
    fontWeight = FontWeight.Bold,
    color = ColorsManager.PrimaryColor,

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
  //  lineHeight = 30.sp,          // Line height 24px
  //  letterSpacing = 0.5.sp,      // Letter spacing 0.5px
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
val TitleTextStyle = TextStyle(
    color = ColorsManager.PrimaryDarkColor, // Replace with your color resource or use ColorResource directly
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold,
    fontFamily = Poppins
)
val MessageTextStyle = TextStyle(
    color = Color(0xFFB0BEC5), // Replace with the appropriate color value for @color/neutral_grey
    fontSize = 12.sp,
    fontFamily = Poppins
)
val MediumPrimaryTextStyle = TextStyle(
    color = ColorsManager.PrimaryColor, // Make sure this color is defined in resources
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold,
    fontFamily = Poppins
)
val MediumTitleTextStyle = TextStyle(
    fontSize = 14.sp,
    color = ColorsManager.PrimaryDarkColor,
    fontFamily = Poppins,
    fontWeight = FontWeight.Bold // Optional, based on your needs
)
