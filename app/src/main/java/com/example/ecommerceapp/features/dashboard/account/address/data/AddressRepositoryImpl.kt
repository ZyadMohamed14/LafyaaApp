package com.example.ecommerceapp.features.dashboard.account.address.data

import com.example.ecommerceapp.features.dashboard.account.address.domain.Address
import com.example.ecommerceapp.features.dashboard.account.address.domain.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val addressDao: AddressDao
) : AddressRepository {
    override suspend fun AddOrUpdateAddress(address: Address): Flow<Boolean> = flow {
        try {
            addressDao.insertOrUpdate(address)
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }

    }

    override suspend fun deleteAddress(address: Address) {
       addressDao.delete(address)
    }

    override suspend fun getAddresses(): Flow<List<Address>> = flow {
        try {
            val addresses = addressDao.getAllAddresses()
            emit(addresses)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getAddressById(addressId: Int): Flow<Address?> = flow {
        try {
            val addresses = addressDao.getAddressById(addressId)
            emit(addresses)
        } catch (e: Exception) {
            emit(null)
        }
    }

    override suspend fun resetAllAddressSelection(id: Int) {
        addressDao.resetAllAddresses()
        // Reset all addresses first
        addressDao.resetAllAddresses()

        // Set the selected address
        addressDao.setSelectedAddress(id)
    }


}