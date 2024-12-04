package com.example.ecommerceapp.features.auth.login

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.BuildConfig
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.local.SharedPreferencesHelper
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.ui.AppAlertDialog
import com.example.ecommerceapp.core.ui.AppButton
import com.example.ecommerceapp.core.ui.AppLoadingDialog
import com.example.ecommerceapp.core.ui.AppTextField
import com.example.ecommerceapp.core.ui.OrDivider
import com.example.ecommerceapp.core.ui.SocialButton
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.auth.register.DonotOrAlreadyHaveAccountMessage
import com.example.ecommerceapp.features.auth.register.WelcomeHeader
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val context = LocalContext.current


    // TODO Handle States For Login
    var hasAttemptedSubmission by remember { mutableStateOf(false) }
    var successDialogVisible by remember { mutableStateOf(false) }
    var submissionFailed by remember { mutableStateOf(false) }
    var progressDialogVisible by remember { mutableStateOf(false) }
    var errorDialogVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState(initial = Resource.Idle())
    // TODO Login with email and Password Data
    val email by viewModel.emailState.collectAsState()
    val password by viewModel.passwordState.collectAsState()
    // TODO Handle Login With Google
    // Initialize Google sign-in options
    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.clientServerId).requestEmail().requestProfile()
        .requestServerAuthCode(BuildConfig.clientServerId).build()
    val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)

    // Handle Google Sign-In result
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                viewModel.loginWithGoogle(idToken)


            } else {
                // Handle error if idToken is null
                Log.e("LoginScreen", "Google sign-in failed: idToken is null")
            }
        } catch (e: ApiException) {
            Log.e("LoginScreen", "Google sign-in failed", e)
        }
    }


    LaunchedEffect(loginState) {
        when (loginState) {
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
                val errorResponse = (loginState as Resource.Error).errorResponse
                val exception = loginState.exception

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
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WelcomeHeader(
                header = stringResource(id = R.string.welcome_to_lafyuu),
                message = stringResource(id = R.string.signin_to_continue)
            )

            AppTextField(
                value = email,
                onValueChange = {
                     viewModel.updateEmail(it)
                },
                label = "Full Name",
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_name),
                        contentDescription = null
                    )

                },
                isError = hasAttemptedSubmission && !viewModel.isEmailValid(),
                errorMessage = "Please enter a valid name"
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                value = password,
                onValueChange = { viewModel.updatePassword(it) },
                label = "PassWord",
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.password),
                        contentDescription = null
                    )

                },
                isPassword = true,
                isError = hasAttemptedSubmission && !viewModel.isPasswordValid(),
                errorMessage = "Please enter a valid password"

            )
            Spacer(modifier = Modifier.height(16.dp))
            // TODO Handle Login With Email And Password
            AppButton(text = "sign In") {

                hasAttemptedSubmission = true
                viewModel.loginWithEmailAndPassword()
            }
            Text(
                text = stringResource(id = R.string.forgot_password),
                color = ColorsManager.PrimaryColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.End).clickable {
                        navController.navigate(Routes.ForgotPassword)
                    } // Center align
            )
            Spacer(modifier = Modifier.height(8.dp))
            OrDivider()
            Spacer(modifier = Modifier.height(8.dp))

            // TODO Handle Login With Google
            SocialButton(
                text = stringResource(id = R.string.google_signup_btn_msg),
                icon = painterResource(id = R.drawable.ic_google),
                onClick = {
                    launcher.launch(googleSignInClient.signInIntent)
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            // TODO Handle Login With Facebook
            SocialButton(
                text = stringResource(id = R.string.facebook_signup_btn_msg),
                icon = painterResource(id = R.drawable.ic_facebook),
                onClick = { /* Handle Facebook sign-up action */ }
            )
            Spacer(modifier = Modifier.height(4.dp))
            DonotOrAlreadyHaveAccountMessage(
                message = stringResource(id = R.string.don_t_have_a_account),
                actionText = stringResource(id = R.string.register)
            ) {
                navController.navigate(Routes.Register)
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
                Toast.makeText(LocalContext.current, "Login Successful", Toast.LENGTH_SHORT).show()

                navController.popBackStack(Routes.Login, inclusive = true)
                successDialogVisible = false
            }
            if (errorDialogVisible) {
                AppAlertDialog(
                    title = "Failed To Login ",
                    message = errorMessage,
                    imageResource = R.drawable.error,
                    buttonText = "Close",
                    onDismiss = { errorDialogVisible = false }
                ){
                    errorDialogVisible = false
                }
            }
            if (progressDialogVisible) {
                AppLoadingDialog(){}

            }
        }
    }

}

