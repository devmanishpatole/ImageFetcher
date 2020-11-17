package com.devmanishpatole.imagesearcher.di

import android.content.Context
import com.devmanishpatole.imagesearcher.BuildConfig
import com.devmanishpatole.imagesearcher.gallery.list.service.ImgurService
import com.devmanishpatole.imagesearcher.network.Networking
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    private const val AUTHORIZATION = "Authorization"
    private const val AUTHORIZATION_VALUE = "Client-ID 86793b5df56e115"

    private fun getAuthenticatorInterceptor() = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val request = original.newBuilder()
                .header(
                    AUTHORIZATION,
                    AUTHORIZATION_VALUE
                )
                .build()
            return chain.proceed(request)
        }
    }

    private fun getLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    @Singleton
    fun provideNetwork(
        @ApplicationContext appContext: Context
    ): Retrofit =
        Networking.create(
            BuildConfig.BASE_URL,
            appContext.cacheDir,
            10 * 1024 * 1024, // 10MB
            getLoggingInterceptor(),
            getAuthenticatorInterceptor()
        )

    @Provides
    @Singleton
    fun provideImgurService(retrofit: Retrofit): ImgurService =
        retrofit.create(ImgurService::class.java)

}