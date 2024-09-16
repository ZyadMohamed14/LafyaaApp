package com.example.ecommerceapp.ui.auth.country

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.databinding.CountryItemBinding
import com.example.ecommerceapp.ui.auth.country.model.CountryUIModel

class CountriesAdapter(
    private val countries: List<CountryUIModel>,
    private val countryClickListener: CountryClickListener
) : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(private val binding: CountryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CountryUIModel) {
            binding.country = category
            binding.root.setOnClickListener {
                countryClickListener.onCountryClicked(category)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding =
            CountryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int = countries.size

}

interface CountryClickListener {
    fun onCountryClicked(country: CountryUIModel)
}