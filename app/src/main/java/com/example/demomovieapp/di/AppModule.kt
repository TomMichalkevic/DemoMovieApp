package com.example.demomovieapp.di

import com.example.demomovieapp.BuildConfig
import com.example.demomovieapp.data.remote.api.TmdbApi
import com.example.demomovieapp.data.remote.api.TmdbApiKeyInterceptor
import com.example.demomovieapp.data.repository.HistoryRepositoryImpl
import com.example.demomovieapp.data.repository.MovieRepositoryImpl
import com.example.demomovieapp.domain.repository.HistoryRepository
import com.example.demomovieapp.domain.repository.MovieRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(TmdbApiKeyInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        val mediaType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(mediaType))
            .build()
    }

    @Provides
    @Singleton
    fun provideTmdbApi(retrofit: Retrofit): TmdbApi {
        return retrofit.create(TmdbApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(tmdbApi: TmdbApi): MovieRepository {
        return MovieRepositoryImpl(tmdbApi)
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(): HistoryRepository {
        return HistoryRepositoryImpl()
    }
}
