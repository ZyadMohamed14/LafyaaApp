package com.example.ecommerceapp.ui.login


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.datasource.repository.auth.FirebaseAuthRepositoryImpl
import com.example.ecommerceapp.data.datasource.repository.user.UserPreferencesRepositoryImpl
import com.example.ecommerceapp.databinding.FragmentLoginBinding
import com.example.ecommerceapp.model.Resource
import com.example.ecommerceapp.utils.ProgressDialog
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }
 private lateinit var binding: FragmentLoginBinding
 private val loginViewModel :LoginViewModel by viewModels {
     LoginViewModelFactory(userPreferencesRepository = UserPreferencesRepositoryImpl(
         requireContext()
     ),
         authRepository = FirebaseAuthRepositoryImpl())
 }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.bind(inflater.inflate(R.layout.fragment_login, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner=viewLifecycleOwner
        binding.viewModel=loginViewModel
        initViewModel()
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
                        Log.d("benz", "initViewModelError: $msg")
                        Toast.makeText(requireContext(),msg,Toast.LENGTH_SHORT).show()
                        //view?.showSnakeBarError(msg)
                        //logAuthIssueToCrashlytics(msg, "Login Error")
                    }


                    else -> { Toast.makeText(requireContext(),"else",Toast.LENGTH_SHORT).show()}
                }
            }
        }
    }




}