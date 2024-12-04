package com.example.ecommerceapp.core.di
import android.content.Context
import androidx.room.Room
import com.example.ecommerceapp.core.local.AppDatabase
import com.example.ecommerceapp.core.local.MIGRATION_1_2
import com.example.ecommerceapp.features.dashboard.account.address.data.AddressDao
import com.example.ecommerceapp.features.dashboard.home.data.products.ProductDao
import com.example.ecommerceapp.features.user.data.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).addMigrations(MIGRATION_1_2).build()
    }

    @Singleton
    @Provides
    fun provideProductDao(appDatabase: AppDatabase): ProductDao {
        return appDatabase.productDao()
    }
    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
    @Singleton
    @Provides
    fun provideAddressDao(appDatabase: AppDatabase): AddressDao {
        return appDatabase.addressDao()
    }


}