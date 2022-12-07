package com.viamedsalud.gvp.api.methods

import com.viamedsalud.gvp.api.request.HClinicaRequest
import com.viamedsalud.gvp.api.response.HCLinicasResponse
import com.viamedsalud.gvp.api.response.HClinicaResponse
import retrofit2.Response
import retrofit2.http.*

interface HClinicaApi {

    @POST("hclinica/crear")
    suspend fun createHClinica(@Body hclinicaRequest: HClinicaRequest): Response<HClinicaResponse>

    @GET("hclinica/{idUser}")
    suspend fun getHClinicasUser(@Path("idUser") idUser : Int): Response<HCLinicasResponse>

    @GET("hclinica/episodio/{episodio}")
    suspend fun getHClinicasEpisodio(@Path("episodio") episodio : String): Response<HCLinicasResponse>

    @GET("hclinica/")
    suspend fun getAllHClinicas(): Response<HCLinicasResponse>

}