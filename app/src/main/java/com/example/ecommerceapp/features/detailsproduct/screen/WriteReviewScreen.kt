package com.example.ecommerceapp.features.detailsproduct.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.ui.EcommerceAppBar

import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.Poppins
import com.example.ecommerceapp.core.ui.AppAlertDialog
import com.example.ecommerceapp.core.ui.AppButton


import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.ecommerceapp.core.local.SharedPreferencesHelper
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.ui.AppLoadingDialog
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.detailsproduct.domain.Review
import com.example.ecommerceapp.features.detailsproduct.screen.compontents.RatingBar
import com.example.ecommerceapp.features.user.presentation.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteReviewScreen(
    productId: String,
    navController: NavController,
    onBackPressed: () -> Unit
) {
    val viewModel: ReviewsViewModel = hiltViewModel()
    val userViewModel : UserViewModel = hiltViewModel()
    val addReviewState by viewModel.addReviewStates.collectAsState(initial = Resource.Idle())
    val rate = viewModel.rate.floatValue
    var submissionFailed by remember { mutableStateOf(false) }
    var successDialogVisible by remember { mutableStateOf(false) }
    var progressDialogVisible by remember { mutableStateOf(false) }
    var errorDialogVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var navigateToLogin by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sharedPreferences = SharedPreferencesHelper(context)
    val currentUser by userViewModel.userDetails.collectAsState()

    LaunchedEffect(addReviewState) {
        when (addReviewState) {
            is Resource.Loading -> {

                progressDialogVisible = true
            }

            is Resource.Success -> {
                progressDialogVisible = false

                viewModel.clearReviewData()
                successDialogVisible = true
            }

            is Resource.Error -> {
                progressDialogVisible = false

                errorDialogVisible = true
                val errorResponse = (addReviewState as Resource.Error).errorResponse
                val exception = addReviewState.exception

                // Create an error message based on the presence of exception or errorResponse
                errorMessage = when {
                    exception != null -> "Error fetching reviews: ${exception.message}"
                    errorResponse != null -> "Error fetching reviews: ${errorResponse.message}"
                    else -> "Unknown error occurred"
                }
                Log.d("WriteReviewScreen", "errorMessage: $errorMessage")
            }

            is Resource.Idle -> {
                Log.d("WriteReviewScreen", "addReviewState: $addReviewState")
            }
        }
    }
    LaunchedEffect(Unit) {
        userViewModel.loadUserDetails()
    }


    Scaffold(
        topBar = {
            EcommerceAppBar(title = "Write Review", onBackClick = {
                onBackPressed()
            })
        },

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp)
        ) {
            Text(
                text = "Please write Overall level of satisfaction with your shipping / Delivery Service",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    color = ColorsManager.PrimaryDarkColor
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            RatingBar(
                modifier = Modifier.fillMaxWidth(),
                rating = rate,
                onRatingChanged = { newRate ->
                    viewModel.updateRate(newRate)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Write Your Review", style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    color = ColorsManager.PrimaryDarkColor
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel.comment.value,
                onValueChange = {
                    viewModel.updateComment(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(10.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = ColorsManager.PrimaryColor, // Primary color for cursor
                    focusedBorderColor = ColorsManager.PrimaryColor, // Border color when focused

                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Add Photo", style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    color = ColorsManager.PrimaryDarkColor
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OpenGallery(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(text = "Submit") {
                if (currentUser != null) {
                    if (viewModel.checkReviewData()) {

                        //ToDo add review to database
                        val review = Review(
                            productId = productId,
                            rating = rate,
                            userId = currentUser!!.id,
                            comment = viewModel.comment.value,
                            images = mutableListOf<String>().apply {
                                // Convert Uri to String and add to list
                                addAll(viewModel.selectedImages.value.map { it.toString() })
                            }
                        )
                        viewModel.addReview(review)
                    } else {
                        submissionFailed = true
                    }
                } else {
                    navigateToLogin = true

                }

            }

            if (submissionFailed) {
                submissionFailed = false
                Toast.makeText(
                    LocalContext.current,
                    "Please fill all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (successDialogVisible) {
                AppAlertDialog(
                    title = "Review Submitted",
                    message = "Your review has been successfully submitted!",
                    imageResource = R.drawable.successful,
                    buttonText = "Close",
                    onDismiss = { successDialogVisible = false }
                ){
                    successDialogVisible = false
                }
                //  successDialogVisible =false
            }
            if (errorDialogVisible) {
                AppAlertDialog(
                    title = "Submission Failed",
                    message = errorMessage,
                    imageResource = R.drawable.error,
                    buttonText = "Close",
                    onDismiss = { errorDialogVisible = false }

                ){
                    errorDialogVisible = false
                }
            }
            if (progressDialogVisible) {
                AppLoadingDialog {

                }
            }
            if (navigateToLogin) {

                AppAlertDialog(
                    title = "Login Required",
                    message = "You need to login to submit a review.",
                    imageResource = R.drawable.baseline_login_24,
                    buttonText ="Go To Login"
                ) {
                        navigateToLogin = false
                    sharedPreferences.saveRoute(Routes.WriteReview)
                    navController.currentBackStackEntry?.savedStateHandle?.set("productId", productId)

                    navController.navigate(Routes.Login)
                    }

                }
            }
        }


    }





@Composable
fun OpenGallery(reviewsViewModel: ReviewsViewModel) {

    val selectedImages by reviewsViewModel.selectedImages.collectAsState()
    val context = LocalContext.current

    // Launcher to open the gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        reviewsViewModel.updateSelectedImages(uris)
    }

    // Launcher to request permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            galleryLauncher.launch("image/*") // Open gallery if permission is granted
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to check permission and open gallery
    fun checkPermissionAndOpenGallery() {
        // Determine the correct permission based on the Android version
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when (ContextCompat.checkSelfPermission(context, permission)) {
            PackageManager.PERMISSION_GRANTED -> {
                // Permission is granted, open gallery
                galleryLauncher.launch("image/*")
            }

            else -> {
                // Request the appropriate permission
                permissionLauncher.launch(permission)
            }
        }
    }


    if (selectedImages.isEmpty()) {
        // Show the button if no images are selected

        OpenGalleryButton() {
            checkPermissionAndOpenGallery()
        }
    } else {
        // Display selected images in a horizontal row

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White, shape = MaterialTheme.shapes.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(selectedImages.size) { index ->
                val imageUri = selectedImages[index]
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Selected Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(72.dp)
                        //.padding(4.dp)
                        .background(Color.White, shape = MaterialTheme.shapes.small)
                )
            }
            item {
                Spacer(modifier = Modifier.width(8.dp))
                OpenGalleryButton() {
                    checkPermissionAndOpenGallery()
                }
            }

        }

    }
}

@Composable
private fun OpenGalleryButton(onClick: () -> Unit = {}) {
    IconButton(
        onClick = { onClick() },
        modifier = Modifier
            .size(72.dp)
            .shadow(8.dp, shape = MaterialTheme.shapes.small)
            .background(Color.White, shape = MaterialTheme.shapes.small)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Open Gallery",
            tint = Color.Gray
        )
    }
}