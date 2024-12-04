package com.example.ecommerceapp.core.di


import android.content.Context
import com.example.docappincompose.core.api.ApiConstants
import com.example.ecommerceapp.core.local.SharedPreferencesHelper
import com.example.ecommerceapp.features.user.data.UserApiServices
import com.example.ecommerceapp.features.user.domain.UseRepository
import com.example.ecommerceapp.features.user.data.UseRepositoryImpl
import com.example.ecommerceapp.features.auth.forgotpassword.ForgetPasswordRepository
import com.example.ecommerceapp.features.auth.forgotpassword.ForgetPasswordRepositoryImpl
import com.example.ecommerceapp.features.auth.login.data.LoginApiServices
import com.example.ecommerceapp.features.auth.login.domain.LoginRepository
import com.example.ecommerceapp.features.auth.login.data.LoginRepositoryImpl
import com.example.ecommerceapp.features.auth.register.data.RegisterApiServices
import com.example.ecommerceapp.features.auth.register.data.RegisterRepositoryImpl
import com.example.ecommerceapp.features.auth.register.domain.repo.RegisterRepository
import com.example.ecommerceapp.features.dashboard.account.address.data.AddressDao
import com.example.ecommerceapp.features.dashboard.account.address.data.AddressRepositoryImpl
import com.example.ecommerceapp.features.dashboard.account.address.domain.AddressRepository
import com.example.ecommerceapp.features.dashboard.account.order.data.OrderRepositoryImpl
import com.example.ecommerceapp.features.dashboard.account.order.data.OrdersApiServices
import com.example.ecommerceapp.features.dashboard.account.order.domain.OrderRepository
import com.example.ecommerceapp.features.dashboard.home.domain.ProductsRepository
import com.example.ecommerceapp.features.dashboard.home.data.products.ProductsRepositoryImpl
import com.example.ecommerceapp.features.dashboard.home.data.category.CategoriesRepository
import com.example.ecommerceapp.features.dashboard.home.data.category.CategoriesRepositoryImpl
import com.example.ecommerceapp.features.dashboard.home.data.products.ProductDao
import com.example.ecommerceapp.features.dashboard.home.data.products.ProductsApiServices
import com.example.ecommerceapp.features.dashboard.home.data.sales.SalesAdsRepository
import com.example.ecommerceapp.features.dashboard.home.data.sales.SalesAdsRepositoryImpl
import com.example.ecommerceapp.features.detailsproduct.data.ReviewApiService
import com.example.ecommerceapp.features.detailsproduct.data.ReviewRepository
import com.example.ecommerceapp.features.detailsproduct.data.ReviewRepositoryImpl
import com.example.ecommerceapp.features.payment.PaymentApiServices
import com.example.ecommerceapp.features.payment.PaymentRepository
import com.example.ecommerceapp.features.user.data.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


    @Provides
    @Singleton
    fun provideUserRepo(
        apiService: UserApiServices,
        sharedPreferencesHelper: SharedPreferencesHelper,
        userDao: UserDao
    ): UseRepository {
        return UseRepositoryImpl(apiService, userDao,sharedPreferencesHelper)
    }


    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiServices {
        return retrofit.create(UserApiServices::class.java)
    }


    @Provides
    @Singleton
    fun provideSalesAdsRepostory(
        firebaseFirestore: FirebaseFirestore
    ): SalesAdsRepository = SalesAdsRepositoryImpl(firebaseFirestore)


    @Provides
    @Singleton
    fun provideCategoryRepository(firebaseFirestore: FirebaseFirestore): CategoriesRepository {
        return CategoriesRepositoryImpl(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        firebaseFirestore: FirebaseFirestore,
        productDao: ProductDao,
        apiServices: ProductsApiServices
    ): ProductsRepository {
        return ProductsRepositoryImpl(firebaseFirestore, productDao, apiServices)
    }

    @Singleton

    private const val TIME_OUT = 60L

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            ).addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
    @Provides
    @Singleton
    fun providesOrderRepositoy(apis: OrdersApiServices): OrderRepository {
        return OrderRepositoryImpl(apis)
    }
    @Provides
    @Singleton
    fun provideOrderApiService(retrofit: Retrofit): OrdersApiServices {
        return retrofit.create(OrdersApiServices::class.java)
    }
    @Provides
    @Singleton
    fun providesPaymobRepositoy(apis: PaymentApiServices): PaymentRepository {
        return PaymentRepository(apis)
    }

    @Provides
    @Singleton
    fun providePayMobApiService(retrofit: Retrofit): PaymentApiServices {
        return retrofit.create(PaymentApiServices::class.java)
    }
@Provides
@Singleton
fun provideAddressRepo(addressDao: AddressDao): AddressRepository= AddressRepositoryImpl(addressDao)
    @Provides
    @Singleton
    fun provideReviewApiService(retrofit: Retrofit): ReviewApiService {
        return retrofit.create(ReviewApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesReviewsRepo(apiService: ReviewApiService): ReviewRepository {
        return ReviewRepositoryImpl(apiService)
    }


    @Provides
    @Singleton
    fun provideRegisterApiService(retrofit: Retrofit): RegisterApiServices {
        return retrofit.create(RegisterApiServices::class.java)
    }

    @Provides
    @Singleton
    fun providesRegisterRepo(
        apiService: RegisterApiServices,
        firebaseFirestore: FirebaseFirestore,
        auth: FirebaseAuth,
        sharedPreferencesHelper: SharedPreferencesHelper

    ): RegisterRepository {
        return RegisterRepositoryImpl(
            apiService,
            firebaseFirestore,
            auth,
            sharedPreferencesHelper
        )
    }

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferencesHelper =
        SharedPreferencesHelper(context)

    @Provides
    @Singleton
    fun providesForgetPasswordRepo(auth: FirebaseAuth): ForgetPasswordRepository {
        return ForgetPasswordRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun providesLoginRepo(
        auth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        userDao: UserDao,
        sh: SharedPreferencesHelper
    ): LoginRepository {
        return LoginRepositoryImpl(auth, firebaseFirestore,userDao, sh)
    }

    @Provides
    @Singleton
    fun providesLoginReApi(retrofit: Retrofit): LoginApiServices {
        return retrofit.create(LoginApiServices::class.java)
    }

    @Provides
    @Singleton
    fun providesProductApi(retrofit: Retrofit): ProductsApiServices {
        return retrofit.create(ProductsApiServices::class.java)
    }
}