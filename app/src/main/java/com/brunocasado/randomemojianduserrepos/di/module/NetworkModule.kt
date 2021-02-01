package com.brunocasado.randomemojianduserrepos.di.module

import android.app.Application
import com.brunocasado.randomemojianduserrepos.BuildConfig
import com.brunocasado.randomemojianduserrepos.datasource.network.ApiService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val TIMEOUT = 60L
    }

    @Provides
    @Singleton
    fun providesOkHttpCache(application: Application): Cache {
        val cacheSize = 10L * 1024L * 1024L
        return Cache(application.cacheDir, cacheSize)
    }

    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val level: HttpLoggingInterceptor.Level = if (BuildConfig.BUILD_TYPE == "debug") {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        return HttpLoggingInterceptor().setLevel(level)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        cache: Cache,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
        client
            .cache(cache)
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiService.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}