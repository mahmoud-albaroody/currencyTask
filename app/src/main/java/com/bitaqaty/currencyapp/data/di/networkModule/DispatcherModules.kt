package com.bitaqaty.currencyapp.data.di.networkModule

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModules {
    @IODispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.IO


}