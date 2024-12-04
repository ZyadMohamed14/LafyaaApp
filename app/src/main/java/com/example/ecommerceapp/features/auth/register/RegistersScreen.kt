package com.example.ecommerceapp.features.auth.register


import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.BuildConfig
import com.example.ecommerceapp.R
import com.example.ecommerceapp.core.local.SharedPreferencesHelper
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.MediumPrimaryTextStyle
import com.example.ecommerceapp.core.theme.MessageTextStyle
import com.example.ecommerceapp.core.theme.TitleTextStyle
import com.example.ecommerceapp.core.ui.AppAlertDialog
import com.example.ecommerceapp.core.ui.AppButton
import com.example.ecommerceapp.core.ui.AppTextField
import com.example.ecommerceapp.core.ui.OrDivider
import com.example.ecommerceapp.core.ui.SocialButton

import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.auth.login.domain.AppAuthProvider
import com.example.ecommerceapp.features.auth.register.domain.model.RegisterRequestModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    navController: NavController
) {
    val viewModel: RegisterViewModel = hiltViewModel()
    val context = LocalContext.current
    val sharedPreferences = SharedPreferencesHelper(context)
    val registerState by viewModel.registerState.collectAsState(initial = Resource.Idle())
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val isRegisterValid by viewModel.isRegisterIsValid.collectAsState(initial = false)
    var successDialogVisible by remember { mutableStateOf(false) }
    var submissionFailed by remember { mutableStateOf(false) }
    var progressDialogVisible by remember { mutableStateOf(false) }
    var errorDialogVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var hasAttemptedSubmission by remember { mutableStateOf(false) }
    // Initialize Google sign-in options
    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.clientServerId).requestId().requestEmail().requestProfile()
        .requestServerAuthCode(BuildConfig.clientServerId).build()
    val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {

            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken

            if (idToken != null) {
                sharedPreferences.saveUserEmail(account.email!!)
                viewModel.registerWithGoogle(idToken)
            } else {
                // Handle error if idToken is null
                Log.e("RegisterScreen", "Google sign-in failed: idToken is null")
            }
        } catch (e: ApiException) {
            Log.e("RegisterScreen", "Google sign-in failed", e)
        }
    }

    LaunchedEffect(registerState) {
        when (registerState) {
            is Resource.Loading -> {

                progressDialogVisible = true
            }

            is Resource.Success -> {
                progressDialogVisible = false
                viewModel.clearFields()
                hasAttemptedSubmission = false
                successDialogVisible = true
            }

            is Resource.Error -> {
                progressDialogVisible = false
                errorDialogVisible = true
                val errorResponse = (registerState as Resource.Error).errorResponse
                val exception = registerState.exception

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
                header = "Welcome to the Registration",
                message = "Sign up to start your journey!"
            )
            AppTextField(
                value = name,
                onValueChange = {
                    viewModel.updateName(it)
                },
                label = "Full Name",
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_name),
                        contentDescription = null
                    )

                },
                isError = hasAttemptedSubmission && !viewModel.isNameValid(),
                errorMessage = "Please enter a valid name"

            )
            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                value = email,
                onValueChange = { viewModel.updateEmail(it) },
                label = "Email",
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = null
                    )

                },
                isError = hasAttemptedSubmission && !viewModel.isEmailValid(),
                errorMessage = "Please enter a valid email"

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
            AppTextField(

                value = confirmPassword,
                onValueChange = { viewModel.updateConfirmPassword(it) },
                label = "Confirm PassWord",
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.password),
                        contentDescription = null
                    )

                }, isPassword = true,
                isError = hasAttemptedSubmission && !viewModel.isConfirmPasswordValid(),
                errorMessage = "Please enter a valid password"

            )
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(text = "sign Up")
            {
                hasAttemptedSubmission = true
                // Check if registration is valid when the button is clicked
                if (isRegisterValid) {
                    val registerModel = RegisterRequestModel(
                        fullName = name,
                        email = email,
                        password = password,
                        provider = AppAuthProvider.EMAIL.name
                    )
                    viewModel.registerUserWithEmailAndPassword(registerModel)

                } else {
                    submissionFailed = true
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            OrDivider()
            Spacer(modifier = Modifier.height(8.dp))
            SocialButton(
                text = stringResource(id = R.string.google_signup_btn_msg),
                icon = painterResource(id = R.drawable.ic_google),
                onClick = {
                    launcher.launch(googleSignInClient.signInIntent)
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            SocialButton(
                text = stringResource(id = R.string.facebook_signup_btn_msg),
                icon = painterResource(id = R.drawable.ic_facebook),
                onClick = { /* Handle Facebook sign-up action */ }
            )
            Spacer(modifier = Modifier.height(4.dp))
            DonotOrAlreadyHaveAccountMessage(
                message = stringResource(id = R.string.have_an_account_msg),
                actionText = stringResource(id = R.string.sign_in),
            ) {
                navController.navigate(Routes.Login)
            }
            Spacer(modifier = Modifier.height(4.dp))


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
                    title = "Registration Successful",
                    message = "Your account has been created successfully.",
                    imageResource = R.drawable.successful,
                    buttonText = "Go to Login",
                    onDismiss = {
                        successDialogVisible = false
                        navController.navigate(Routes.Login)
                    }
                )

            }
            if (errorDialogVisible) {
                AppAlertDialog(
                    title = "Submission Failed",
                    message = errorMessage,
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

@Composable
fun WelcomeHeader(
    header: String,
    message: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo Image
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(top = 40.dp) // Adding top margin
        )
//
        // Welcome Text
        Text(
            text = header, // replace with @string/register_welcome_msg
            style = TitleTextStyle, // Replace this with your TitleTextStyle
            modifier = Modifier
                .padding(top = 16.dp) // Adding top margin
        )

        // Sign Up Message Text
        Text(
            text = message, // replace with @string/sign_up_msg
            style = MessageTextStyle, // Replace this with your MessageTextStyle
            modifier = Modifier
                .padding(top = 8.dp) // Adding top margin
        )
    }
}

@Composable
fun DonotOrAlreadyHaveAccountMessage(
    message: String,
    actionText: String,
    onSignInClick: () -> Unit
) {
    // A horizontal row to replicate the LinearLayout
    Row(
        modifier = Modifier
            .wrapContentWidth() // Equivalent to wrap_content width
            .wrapContentHeight() // Equivalent to wrap_content height
            .padding(top = 4.dp), // Add top margin
        verticalAlignment = Alignment.CenterVertically, // Center vertically
        horizontalArrangement = Arrangement.Center // Center horizontally
    ) {
        // Message Text
        Text(
            text = message, // String resource for the message
            style = MessageTextStyle, // Apply your custom style or typography
            modifier = Modifier.wrapContentWidth() // Wrap content width
        )

        // Sign In Text (TextView)
        Text(
            text = actionText, // String resource for the sign-in text
            style = MediumPrimaryTextStyle,
            modifier = Modifier
                .padding(start = 4.dp) // Margin start for spacing
                .clickable { onSignInClick() } // Make it clickable with a lambda for handling actions
        )
    }
}