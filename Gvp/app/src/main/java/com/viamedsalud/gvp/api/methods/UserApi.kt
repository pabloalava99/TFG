package com.viamedsalud.gvp.api.methods

import com.viamedsalud.gvp.api.request.UserRequest
import com.viamedsalud.gvp.api.response.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @POST("users/authenticate")
    suspend fun loginUser(@Body userRequest: UserRequest): Response<UserResponse>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id : Int): Response<UserResponse>

}