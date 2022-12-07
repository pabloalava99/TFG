package com.viamedsalud.gvp.api.request


import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("username")
    var username: String,
    @SerializedName("password")
    var password: String
)