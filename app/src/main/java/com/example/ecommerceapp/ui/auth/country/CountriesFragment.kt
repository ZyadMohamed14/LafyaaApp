package com.example.ecommerceapp.ui.auth.country

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentCountriesBinding
import com.example.ecommerceapp.ui.auth.country.model.CountryUIModel
import com.example.ecommerceapp.ui.auth.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint

class CountriesFragment : DialogFragment() , CountryClickListener{

    val viewModel: CountriesViewModel by viewModels()
   lateinit var binding: FragmentCountriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentCountriesBinding.inflate(inflater, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }
    private fun initViewModel() {

        lifecycleScope.launch {
            viewModel.countriesUIModelState.collectLatest {
                if(it.isEmpty()) return@collectLatest
                binding.progressBar.visibility = View.GONE
                binding.countriesLayout.visibility = View.VISIBLE

                val countriesAdapter = CountriesAdapter(it, this@CountriesFragment)
                binding.countriesRv.apply {
                    adapter = countriesAdapter
                    layoutManager = LinearLayoutManager(context)
                }
            }
        }
    }

    override fun onCountryClicked(country: CountryUIModel) {
        viewModel.saveUserCountry(country)
        dismiss()
    }
}