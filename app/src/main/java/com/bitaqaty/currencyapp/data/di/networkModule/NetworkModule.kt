package com.bitaqaty.currencyapp.data.di.networkModule

import android.content.Context
import com.bitaqaty.currencyapp.CurrencyApp
import com.bitaqaty.currencyapp.data.remote.ApiRepoImpl
import com.bitaqaty.currencyapp.domain.usecase.ConvertUseCase
import com.bitaqaty.currencyapp.data.remote.APIs
import com.bitaqaty.currencyapp.domain.usecase.GetSymbolsUseCase
import com.bitaqaty.currencyapp.domain.usecase.RateUseCase
import com.bitaqaty.currencyapp.domain.usecase.TimeSeriesUseCase
import com.bitaqaty.currencyapp.utils.constants.Constants

import com.bitaqaty.currencyapp.utils.constants.Constants.ACCESS_TOKEN
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): CurrencyApp {
        return app as CurrencyApp
    }


    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): APIs {
        return retrofit.create(APIs::class.java)
    }


//    @Singleton
//    @Provides
//    fun getDatabaseOo(@ApplicationContext app: Context): CartDatabase {
//        return Room.databaseBuilder(
//            app.applicationContext,
//            CartDatabase::class.java,
//            "cart.db"
//        ).fallbackToDestructiveMigration()
//            //.addTypeConverter(GsonConverterFactory.create())
//            .build()
//    }


    private val READ_TIMEOUT = 30
    private val WRITE_TIMEOUT = 30
    private val CONNECTION_TIMEOUT = 10
    private val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB


    @Singleton
    @Provides
    fun provideOkHttpClient(
        headerInterceptor: HttpLoggingInterceptor,
        cache: Cache
    ): OkHttpClient {
        headerInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val header = HttpLoggingInterceptor()
        header.apply { header.level = HttpLoggingInterceptor.Level.HEADERS }
        val okHttpClientBuilder = OkHttpClient().newBuilder()
        okHttpClientBuilder.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.cache(cache)
        okHttpClientBuilder.addInterceptor(headerInterceptor)
        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor {
            val requestBuilder = it.request().newBuilder()
                //hear you can add all headers you want by calling 'requestBuilder.addHeader(name ,  value)'
//                .header("Content-Type", "application/json; charset=utf-8")
                .header("apikey", ACCESS_TOKEN)

                //  .header("Host", "")

            it.proceed(requestBuilder.build())
        }
    }

    @Singleton
    @Provides
    internal fun provideCache(context: Context): Cache {
        val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
        return Cache(httpCacheDirectory, CACHE_SIZE_BYTES)
    }


    @Singleton
    @Provides
    fun provideContext(application: CurrencyApp): Context {
        return application.applicationContext
    }
    @Singleton
    @Provides
    fun provideConvertUseCase(
            apiService: ApiRepoImpl,
    ): ConvertUseCase {
        return ConvertUseCase(
            apiService
        )
    }

    @Singleton
    @Provides
    fun provideGetSymbolsUseCase(
        apiService: ApiRepoImpl,
    ): GetSymbolsUseCase {
        return GetSymbolsUseCase(
            apiService
        )
    }
    @Singleton
    @Provides
    fun provideTimeSeriesUseCase(
        apiService: ApiRepoImpl,
    ): TimeSeriesUseCase {
        return TimeSeriesUseCase(
            apiService
        )
    }
    @Singleton
    @Provides
    fun provideRateUseCase(
        apiService: ApiRepoImpl,
    ): RateUseCase {
        return RateUseCase(
            apiService
        )
    }

//    @Singleton
//    @Provides
//    fun account(account: AccountData): AccountData {
//        return account
//    }
}