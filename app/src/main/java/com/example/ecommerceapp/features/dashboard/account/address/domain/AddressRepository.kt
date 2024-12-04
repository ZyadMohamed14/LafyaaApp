package com.example.ecommerceapp.features.dashboard.account.address.domain

import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    suspend fun AddOrUpdateAddress(address: Address): Flow<Boolean>
    suspend fun deleteAddress(address: Address)
    suspend fun getAddresses(): Flow<List<Address>>
    suspend fun getAddressById(addressId: Int): Flow<Address?>
    suspend fun resetAllAddressSelection(id: Int)
}