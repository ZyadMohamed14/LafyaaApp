package com.example.ecommerceapp.features.dashboard.account.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PhoneIphone
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.local.SharedPreferencesHelper

import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.BodyTextNormalRegular
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.NeuturalDark
import com.example.ecommerceapp.core.theme.Poppins
import com.example.ecommerceapp.core.theme.productTitleStyle
import com.example.ecommerceapp.core.ui.AppAlertDialog
import com.example.ecommerceapp.core.ui.AppButton
import com.example.ecommerceapp.core.ui.EcommerceAppBar
import com.example.ecommerceapp.features.user.domain.UserDetailsModel
import com.example.ecommerceapp.features.user.presentation.UserViewModel

@Composable
fun ProfileScreen(navController: NavController,onBackClick: () -> Unit) {




     val userViewModel:UserViewModel = hiltViewModel()
    val userDetailsState by userViewModel.userDetails.collectAsState()
    val isDataLoaded by userViewModel.isDataUpdated.collectAsState()
    LaunchedEffect(userDetailsState) {

        userViewModel.loadUserDetails()
    }
    val currentUser by userViewModel.userDetails.collectAsState()
    LaunchedEffect(isDataLoaded) {

        userViewModel.loadUserDetails()

    }
    Scaffold(
        topBar = {
            EcommerceAppBar(title = "Profile", onBackClick = {
                Log.d("TApdlpfdfG", "ProfileScreen: back")
                onBackClick()
            })
        }
    ) { innerPadding ->

        if (currentUser != null) {
            Log.d("TApdlpfdfG", "ProfileScreen: ${currentUser.toString()}")
            ShowUserData(innerPadding,currentUser!!,navController)
        }else{
          NeedToLoginScreen(innerPadding,navController)
        }


    }
}
@Composable
fun ShowUserData(innerPadding: PaddingValues,userDetails: UserDetailsModel,navController: NavController){
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
         val email = userDetails.email?:" Add your Email"
        val phone = userDetails.phone?:" Add your Phone"
        Spacer(modifier = Modifier.height(32.dp))
        Divider(color = ColorsManager.NeutralLight, thickness = 2.dp)
        Spacer(modifier = Modifier.height(32.dp))
        ProfileImage(userDetails)
        Spacer(modifier = Modifier.height(32.dp))
        ProfileItem(icon = Icons.Outlined.Email, title = "Email" , data =email ){

            navController.navigate("update_profile_screen/Email/$email")
        }
        Spacer(modifier = Modifier.height(32.dp))
        ProfileItem(icon = Icons.Outlined.PhoneIphone, title = "Phone Number", data =phone ){
            navController.navigate("update_profile_screen/Phone/$phone")
        }
        Spacer(modifier = Modifier.height(32.dp))
        ProfileItem(icon = Icons.Outlined.Lock, title = "ChangePassword", data ="**************" )

    }
}
@Composable
fun NeedToLoginScreen(innerPadding: PaddingValues,navController: NavController) {
    // Load and animate the Lottie composition
    val sharedPreferences = SharedPreferencesHelper(LocalContext.current)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever // Loop animation indefinitely
    )
    var navigateToLogin by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Lottie animation instead of static image

        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = " Login Required ",
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Poppins,
                color = ColorsManager.Gray400
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(text = "Login", ){
            navigateToLogin = true
        }
        if (navigateToLogin) {

            AppAlertDialog(
                title = "Login Required",
                message = "You need to login to submit a review.",
                imageResource = R.drawable.baseline_login_24,
                buttonText ="Go To Login"
            ) {
                navigateToLogin = false
                sharedPreferences.saveRoute(Routes.ProfileScreen)
                navController.navigate(Routes.Login)
            }

        }

    }
}
@Composable
fun ProfileImage(
    userDetails: UserDetailsModel,
    imageUrl: String? = null
) {
    val anonymousImage =
        "https://firebasestorage.googleapis.com/v0/b/onsale-7f4cb.appspot.com/o/person.png?alt=media&token=b4619a5f-c3fe-4f95-8021-80a3f5c221c1"
      val userImage = imageUrl ?: anonymousImage

    Row(
    verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = userImage,
            contentDescription = "userImage",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(72.dp) // Adjust size as needed
                .clip(CircleShape) // Apply circular shape
                .background(Color.White) // Optional: Background color in case of transparency
        )
        Spacer(modifier = Modifier.width(5.dp))

        userDetails.name?.let {
            Text(
                text = it,
                color = NeuturalDark,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

    @Composable
    fun ProfileItem(
        icon: ImageVector,
        title: String,
        data: String,
        onClick: () -> Unit = {}
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon
                ,
                contentDescription = "" ,
                tint = ColorsManager.PrimaryColor,
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                title,
                style = productTitleStyle.copy(fontSize = 12.sp),
                modifier = Modifier.weight(1f)
            )
            Text("${data}  >", style = BodyTextNormalRegular,modifier = Modifier.clickable {
                onClick()
            })


        }
    }
