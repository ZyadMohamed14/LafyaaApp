package com.example.ecommerceapp.features.dashboard.account.address.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.core.local.SharedPreferencesHelper
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.TitleTextStyle
import com.example.ecommerceapp.core.ui.AppButton
import com.example.ecommerceapp.core.ui.AppTextField
import com.example.ecommerceapp.core.ui.EcommerceAppBar
import com.example.ecommerceapp.features.dashboard.account.address.domain.Address

@Composable
fun AddOrEditAddressScreen(
    navController: NavController,
    event: AddressEvent,
    onBackClick: () -> Unit,
    editAddress: Address? = null
) {
    val route = SharedPreferencesHelper(LocalContext.current).getRoute()
    val addressViewModel: AddressViewModel = hiltViewModel()
    val addOrUpdateAddress by addressViewModel.addOrUpdateAddress.collectAsState()
    var isSubmitsuccessful by remember { mutableStateOf(false) }
    val addressEvent = listOf(AddressEvent.ADD, AddressEvent.Edit)
    val addAddressEvents = listOf(
        AddAddress("Full Name", AddAddressEvent.NAME),
        AddAddress("Phone Number", AddAddressEvent.PHONE),
        AddAddress("Description", AddAddressEvent.DESCRIPTION),
        AddAddress("City", AddAddressEvent.CITY)
    )
    var isError by remember { mutableStateOf(false) }
    LaunchedEffect(addOrUpdateAddress) {
        isSubmitsuccessful = when (addOrUpdateAddress) {
            true -> {
                true
            }

            false -> {
                false
            }
        }
    }
    LaunchedEffect(editAddress) {
        editAddress?.let {
            addressViewModel.updateName(it.userFullName)
            addressViewModel.updatePhone(it.phone)
            addressViewModel.updateAddressDetails(it.details)
            addressViewModel.updateCity(it.city)
        }
    }
    Scaffold(
        topBar = {
            EcommerceAppBar(
                title = if (event == AddressEvent.ADD) "Add Address" else "Edit Address",
                onBackClick = onBackClick
            )

        }
    ) { innerPadding ->
        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = ColorsManager.NeutralLight, thickness = 2.dp)
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {


            addAddressEvents.forEach { addressEvent ->
                when (addressEvent.event) {
                    AddAddressEvent.NAME -> {
                        AddAddressItem(
                            title = addressEvent.title,
                            isSubmit = isError,
                            value = addressViewModel.name.value
                        ) {
                            addressViewModel.updateName(it)
                        }
                    }

                    AddAddressEvent.PHONE -> {
                        AddAddressItem(
                            title = addressEvent.title,
                            isSubmit = isError,
                            value = addressViewModel.phone.value
                        ) {
                            addressViewModel.updatePhone(it)
                        }
                    }

                    AddAddressEvent.DESCRIPTION -> {
                        AddAddressItem(
                            title = addressEvent.title,
                            isSubmit = isError,
                            value = addressViewModel.addressDetails.value
                        ) {
                            addressViewModel.updateAddressDetails(it)
                        }
                    }

                    AddAddressEvent.CITY -> {
                        AddAddressItem(
                            title = addressEvent.title,
                            isSubmit = isError,
                            value = addressViewModel.city.value
                        ) {
                            addressViewModel.updateCity(it)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            AppButton(text = "Save") {
                if (addressViewModel.validateFields()) {
                    if (editAddress != null) {
                        val updatedAddress = editAddress.copy(
                            userFullName = addressViewModel.name.value,
                            phone = addressViewModel.phone.value,
                            details = addressViewModel.addressDetails.value,
                            city = addressViewModel.city.value
                        )
                        addressViewModel.addOrUpdateAddress(updatedAddress)
                    } else {
                        val address = Address(
                            userFullName = addressViewModel.name.value,
                            phone = addressViewModel.phone.value,
                            details = addressViewModel.addressDetails.value,
                            city = addressViewModel.city.value
                        )
                        addressViewModel.addOrUpdateAddress(address)
                    }


                } else {
                    isError = true
                }

            }

            if (isSubmitsuccessful) {
                Toast.makeText(
                    LocalContext.current,
                    "Address Added Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                isSubmitsuccessful = false
                navController.popBackStack()


            }


        }


    }

}


@Composable
fun AddAddressItem(
    title: String,
    isSubmit: Boolean = false,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(text = title, style = TitleTextStyle)
        Spacer(modifier = Modifier.height(8.dp))
        AppTextField(
            value = value,
            onValueChange = onValueChange,
            isError = value.isEmpty() && isSubmit,
            errorMessage = " This field is required"
        )

    }
}

data class AddAddress(
    val title: String,
    val event: AddAddressEvent
)

enum class AddAddressEvent {
    NAME,
    PHONE,
    DESCRIPTION,
    CITY,

}

enum class AddressEvent {
    ADD,
    Edit,

}