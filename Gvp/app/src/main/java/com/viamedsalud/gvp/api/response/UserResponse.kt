package com.viamedsalud.gvp.api.response


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    var id: Int,
    @SerializedName("username")
    var username: String,
    @SerializedName("token")
    var token: String
)