package com.example.ecommerceapp.ui.dashboard.home.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.databinding.FragmentLoginBinding
import com.example.ecommerceapp.ui.dashboard.home.adapter.SalesAdAdapter
import com.example.ecommerceapp.ui.dashboard.home.model.SalesAdUIModel
import com.example.ecommerceapp.ui.dashboard.home.viewmodels.HomeViewModel
import com.example.ecommerceapp.utils.CircleView
import com.example.ecommerceapp.utils.DepthPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        iniViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
        return binding.root
    }

    private fun iniViewModel() {

        val url =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQrxEKpYtHdVZw4fywzZDni3OB8O4TJU2MNg&s";
        val salesAds = listOf(
            SalesAdUIModel(

                title = "Summer Sale",
                imageUrl = url,
                endAt = System.currentTimeMillis() + 7200000
            ),
            SalesAdUIModel(

                title = "Winter Clearance",

                imageUrl = url,
                endAt = System.currentTimeMillis() + 7200000
            )
        )
        initializeIndicators(salesAds.size)
        val salesAdapter = SalesAdAdapter(salesAds)
        binding.saleAdsViewPager.apply {
            adapter = salesAdapter
            setPageTransformer(DepthPageTransformer())
            registerOnPageChangeCallback(object :
                androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicators(position)
                }
            })
        }

        lifecycleScope.launch {
            delay(5000)
            binding.saleAdsViewPager.currentItem = 1
        }
    }

    private var indicators = mutableListOf<CircleView>()
    private fun initializeIndicators(count: Int) {
        for (i in 0 until count) {
            val circleView = CircleView(requireContext())
            val params = LinearLayout.LayoutParams(
                20, 20
            )
            params.setMargins(8, 0, 8, 0) // Margin between circles
            circleView.setLayoutParams(params)
            circleView.setRadius(10f) // Set radius
            circleView.setColor(
                if (i == 0) requireContext().getColor(R.color.primary_color) else requireContext().getColor(
                    R.color.neutral_grey
                )
            ) // First indicator is red
            circleView.setOnClickListener {
                binding.saleAdsViewPager.setCurrentItem(i, true)
            }
            indicators.add(circleView)
            binding.indicatorView.addView(circleView)
        }
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until indicators.size) {
            indicators[i].setColor(
                if (i == position) requireContext().getColor(R.color.primary_color) else requireContext().getColor(
                    R.color.neutral_grey
                )
            )
        }
    }
}