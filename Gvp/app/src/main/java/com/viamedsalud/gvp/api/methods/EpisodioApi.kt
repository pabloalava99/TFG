package com.viamedsalud.gvp.api.methods

import com.viamedsalud.gvp.api.response.EpisodioResponse
import com.viamedsalud.gvp.api.response.EpisodiosResponse
import retrofit2.Response
import retrofit2.http.*

interface EpisodioApi {

    @GET("episodios/")
    suspend fun getEpisodios(): Response<EpisodiosResponse>

    @GET("episodios/{episodio}")
    suspend fun getEpisodio(@Path("episodio") episodio : String): Response<EpisodioResponse>

}