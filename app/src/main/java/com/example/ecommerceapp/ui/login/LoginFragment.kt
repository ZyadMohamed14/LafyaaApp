package com.example.ecommerceapp.ui.login


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommerceapp.BuildConfig

import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.datasource.repository.auth.FirebaseAuthRepositoryImpl
import com.example.ecommerceapp.data.datasource.repository.user.UserPreferencesRepositoryImpl
import com.example.ecommerceapp.databinding.FragmentLoginBinding
import com.example.ecommerceapp.model.Resource
import com.example.ecommerceapp.utils.CrashlyticsUtils
import com.example.ecommerceapp.utils.LoginException
import com.example.ecommerceapp.utils.ProgressDialog
import com.example.ecommerceapp.utils.showSnakeBarError
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            userPreferencesRepository = UserPreferencesRepositoryImpl(
                requireContext()
            ),
            authRepository = FirebaseAuthRepositoryImpl()
        )
    }
    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            FragmentLoginBinding.bind(inflater.inflate(R.layout.fragment_login, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = loginViewModel
        initViewModel()
        initListeners()

    }

    private fun initViewModel() {
        lifecycleScope.launch {
            loginViewModel.loginState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        progressDialog.show()
                    }

                    is Resource.Success -> {
                        progressDialog.dismiss()
                        // goToHome()
                    }

                    is Resource.Error -> {
                        progressDialog.dismiss()
                        val msg = resource.exception?.message ?: getString(R.string.generic_err_msg)
                        view?.showSnakeBarError(msg)
                        logAuthIssueToCrashlytics(msg, "Login Error")
                    }

                }
            }
        }
    }

    // ActivityResultLauncher for the sign-in intent
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            } else {
                view?.showSnakeBarError(getString(R.string.generic_err_msg))
            }
        }

    private fun initListeners() {
        binding.googleSigninBtn.setOnClickListener {
            loginWithGoogleRequest()
        }

    }

    private fun loginWithGoogleRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.clientServerId).requestEmail().requestProfile()
            .requestServerAuthCode(BuildConfig.clientServerId).build()

        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: Exception) {
            view?.showSnakeBarError(e.message ?: getString(R.string.generic_err_msg))
            val msg = e.message ?: getString(R.string.generic_err_msg)
            logAuthIssueToCrashlytics(msg, "Google")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        loginViewModel.loginWithGoogle(idToken)
    }

    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<LoginException>(
            msg,
            CrashlyticsUtils.LOGIN_KEY to msg,
            CrashlyticsUtils.LOGIN_PROVIDER to provider,
        )
    }


}