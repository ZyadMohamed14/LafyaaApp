package com.example.ecommerceapp.features.user.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerceapp.features.user.domain.UserDetailsModel

@Dao
interface UserDao {

    // Insert or update user details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserDetailsModel)

    // Retrieve user details by ID
    @Query("SELECT * FROM user_details WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserDetailsModel?

    // Optional: Delete user details by ID
    @Query("DELETE FROM user_details WHERE id = :userId")
    suspend fun deleteUserById(userId: String)
}