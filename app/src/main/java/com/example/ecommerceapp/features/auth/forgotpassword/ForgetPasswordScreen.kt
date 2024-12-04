package com.example.ecommerceapp.features.auth.forgotpassword

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.ui.AppAlertDialog
import com.example.ecommerceapp.core.ui.AppButton
import com.example.ecommerceapp.core.ui.AppTextField
import com.example.ecommerceapp.core.ui.EcommerceAppBar
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.auth.register.WelcomeHeader
import com.example.ecommerceapp.core.utils.isValidEmail


@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    navigateBack: () -> Unit
) {
    val viewModel = hiltViewModel<ForgetPasswordViewModel>()
    val passwordResetEmailSent by viewModel.forgetPasswordState.collectAsState(initial = Resource.Idle())
    var hasAttemptedSubmission by remember { mutableStateOf(false) }
    var successDialogVisible by remember { mutableStateOf(false) }
    var submissionFailed by remember { mutableStateOf(false) }
    var progressDialogVisible by remember { mutableStateOf(false) }
    var errorDialogVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var email = viewModel.email.collectAsState()

    LaunchedEffect(passwordResetEmailSent) {
        when (passwordResetEmailSent) {
            is Resource.Loading -> {

                progressDialogVisible = true
            }

            is Resource.Success -> {
                progressDialogVisible = false

                successDialogVisible = true
            }

            is Resource.Error -> {
                progressDialogVisible = false
                errorDialogVisible = true
                val errorResponse = (passwordResetEmailSent as Resource.Error).errorResponse
                val exception = passwordResetEmailSent.exception

                // Create an error message based on the presence of exception or errorResponse
                errorMessage = when {
                    exception != null -> {
                        "An error occurred: ${exception.message}"
                    }

                    errorResponse != null -> {
                        "Error: ${errorResponse.message}"
                    }

                    else -> {
                        "Unknown error occurred"
                    }

                }
                Log.d("LoginScreen", "Error message: $errorMessage")
            }

            is Resource.Idle -> {

            }
        }
    }
    Scaffold(
        topBar = {
            EcommerceAppBar(title = "", onBackClick = {
                navigateBack()
            })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            WelcomeHeader(
                header = "Forgot Password",
                message = "provide your email to receive a link to reset your password"
            )

            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                value = email.value,
                onValueChange = { viewModel.updateEmail(it) },
                label = "Email",
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.password),
                        contentDescription = null
                    )

                },
                isPassword = true,
                isError = hasAttemptedSubmission && !email.value.isValidEmail(),
                errorMessage = "Please enter a valid Email"

            )
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(text = "Send") {

                hasAttemptedSubmission = true
                viewModel.sendUpdatePasswordEmail()
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
                    title = "Password Reset Email Sent",
                    message = "A password reset link has been sent to your email. Please check your inbox and follow the instructions to reset your password.",
                    imageResource = R.drawable.successful,
                    buttonText = "go back to login",
                    onDismiss = {
                        successDialogVisible = false
                    },{
                        successDialogVisible = false
                        navController.navigate(Routes.Login)
                    }
                )


            }
            if (errorDialogVisible) {
                AppAlertDialog(
                    title = "Failed to Send Email",
                    message = "We were unable to send the password reset email. $errorMessage Please try again later.",
                    imageResource = R.drawable.error,
                    buttonText = "Close",
                    onDismiss = { errorDialogVisible = false }
                )

            }
            if (progressDialogVisible) {
                AppAlertDialog(
                    title = "Registering...",
                    message = "Please wait while we register your account.",
                    imageResource = R.drawable.ic_logo,
                    buttonText = "Close",
                    onDismiss = { successDialogVisible = false }
                )
            }
        }
    }
}