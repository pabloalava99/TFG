package com.viamedsalud.gvp.api

import com.viamedsalud.gvp.api.methods.EpisodioApi
import com.viamedsalud.gvp.api.methods.HClinicaApi
import com.viamedsalud.gvp.api.methods.UserApi
import com.viamedsalud.gvp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiClient {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    //Log de cliente http
    var mHttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.HEADERS)

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor)
            .addInterceptor(mHttpLoggingInterceptor).build()
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): UserApi {
        return retrofitBuilder.client(okHttpClient).build().create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun providesEpisodioAPI(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): EpisodioApi {
        return retrofitBuilder.client(okHttpClient).build().create(EpisodioApi::class.java)
    }

    @Singleton
    @Provides
    fun providesHClinicaAPI(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): HClinicaApi {
        return retrofitBuilder.client(okHttpClient).build().create(HClinicaApi::class.java)
    }


}