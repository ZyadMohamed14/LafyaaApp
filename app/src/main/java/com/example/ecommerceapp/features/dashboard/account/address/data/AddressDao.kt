package com.example.ecommerceapp.features.dashboard.account.address.data

import androidx.room.*
import com.example.ecommerceapp.features.dashboard.account.address.domain.Address
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(address: Address)



    @Delete
    suspend fun delete(address: Address)

    @Query("SELECT * FROM addresses WHERE id = :id")
    suspend fun getAddressById(id: Int): Address?

    @Query("SELECT * FROM addresses")
    suspend fun getAllAddresses(): List<Address>

    // Reset all addresses' updatedAllUserAddress to false
    @Query("UPDATE addresses SET isSelect = 0")
    suspend fun resetAllAddresses()
    @Query("UPDATE addresses SET isSelect = 1 WHERE id = :id")
    suspend fun setSelectedAddress(id: Int)
}
